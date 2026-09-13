package com.crispytwig.naturalist.world.entity.animal.ostrich;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.EggLayingBreedGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.HideGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.LayEggGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.crispytwig.naturalist.world.entity.EggLayingAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.HidingAnimal;
import com.crispytwig.naturalist.world.entity.IKMount;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class Ostrich extends TamableAnimal implements EggLayingAnimal, HidingAnimal, FollowingPet, DyeableAnimal, PlayerRideableJumping, IKMount, NeutralMob, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.OSTRICH_FOOD_ITEMS);

    private static final double EGG_DEFEND_RADIUS = 10.0D;
    private static final int MAX_TRACKED_EGGS = 16;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_DYE = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.INT);

    private boolean followingOwner = true;
    private int layEggCounter;
    private float playerJumpPendingScale;
    private boolean isJumping;
    private boolean hideCache;
    private long hideCacheTick = -1L;

    private final Set<BlockPos> ownedEggs = new LinkedHashSet<>();
    private long persistentAngerEndTime = NO_ANGER_END_TIME;
    @Nullable
    private EntityReference<LivingEntity> persistentAngerTarget;

    private final AnimationTimer attackAnimTimer = new AnimationTimer(9);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flapAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState buryAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack WALK_SOUNDS = AnimationSoundTrack.builder(1.0F, true)
            .at(0.0F, NaturalistSoundEvents.OSTRICH_STEP, 0.35F, 1.0F)
            .at(0.54F, NaturalistSoundEvents.OSTRICH_STEP, 0.75F, 1.0F)
            .build();
    private static final AnimationSoundTrack RUN_SOUNDS = AnimationSoundTrack.builder(0.6667F, true)
            .at(0.0F, NaturalistSoundEvents.OSTRICH_STEP, 0.35F, 1.0F)
            .at(0.33F, NaturalistSoundEvents.OSTRICH_STEP, 0.75F, 1.0F)
            .build();
    private static final AnimationSoundTrack BURY_SOUNDS = AnimationSoundTrack.builder(0.7917F, false)
            .at(0.04F, NaturalistSoundEvents.OSTRICH_BURY, 1.0F, 1.0F)
            .build();
    private static final AnimationSoundTrack ATTACK_SOUNDS = AnimationSoundTrack.builder(0.4583F, false)
            .at(0.0F, NaturalistSoundEvents.OSTRICH_ATTACK, 1.0F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.walkAnimationState, WALK_SOUNDS)
            .add(this.runAnimationState, RUN_SOUNDS)
            .add(this.buryAnimationState, BURY_SOUNDS)
            .add(this.attackAnimationState, ATTACK_SOUNDS);

    public Ostrich(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.STEP_HEIGHT, 1.0625D)
                .add(Attributes.JUMP_STRENGTH, 0.75D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(SADDLED, false);
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
        builder.define(DATA_DYE, -1);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/ostrich/ostrich.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public Block getEggBlock() {
        return NaturalistRegistry.OSTRICH_EGG.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistBlockTags.OSTRICH_EGG_LAYABLE_ON;
    }

    @Nullable
    @Override
    public DyeColor getDyeColor() {
        int id = this.entityData.get(DATA_DYE);
        return id < 0 ? null : DyeColor.byId(id);
    }

    @Override
    public void setDyeColor(@Nullable DyeColor color) {
        this.entityData.set(DATA_DYE, color == null ? -1 : color.getId());
    }

    @Override
    public boolean isFollowingOwner() {
        return this.followingOwner;
    }

    @Override
    public void setFollowingOwner(boolean following) {
        this.followingOwner = following;
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.persistentAngerEndTime = endTime;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void onEggLaid(@NotNull BlockPos pos) {
        if (this.isTame()) {
            return;
        }
        if (this.ownedEggs.size() >= MAX_TRACKED_EGGS) {
            this.ownedEggs.remove(this.ownedEggs.iterator().next());
        }
        this.ownedEggs.add(pos.immutable());
    }

    private boolean isEggAt(@NotNull BlockPos pos) {
        return this.level().isLoaded(pos)
                && this.level().getBlockState(pos).is(NaturalistRegistry.OSTRICH_EGG.get());
    }

    private boolean isNearOwnedEgg(@NotNull LivingEntity entity) {
        double radiusSqr = EGG_DEFEND_RADIUS * EGG_DEFEND_RADIUS;
        for (BlockPos pos : this.ownedEggs) {
            if (entity.distanceToSqr(Vec3.atCenterOf(pos)) <= radiusSqr && this.isEggAt(pos)) {
                return true;
            }
        }
        return false;
    }

    private void pruneOwnedEggs() {
        this.ownedEggs.removeIf(pos -> this.level().isLoaded(pos) && !this.isEggAt(pos));
    }

    public void onOwnedEggDestroyed(@NotNull BlockPos pos, @NotNull Player culprit) {
        this.ownedEggs.remove(pos);
        if (this.isTame() || this.isBaby() || !this.canAttack(culprit)) {
            return;
        }
        this.setPersistentAngerTarget(EntityReference.of(culprit));
        this.startPersistentAngerTimer();
        this.setTarget(culprit);
    }

    public boolean owns(@NotNull BlockPos pos) {
        return this.ownedEggs.contains(pos);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("Saddled", this.isSaddled());
        output.putBoolean("HasEgg", this.hasEgg());
        long[] eggs = new long[this.ownedEggs.size()];
        int i = 0;
        for (BlockPos pos : this.ownedEggs) {
            eggs[i++] = pos.asLong();
        }
        output.store("OwnedEggs", Codec.LONG_STREAM, Arrays.stream(eggs));
        this.addPersistentAngerSaveData(output);
        DyeableAnimal.saveDye(this, output);
        FollowingPet.savePet(this, output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setSaddled(input.getBooleanOr("Saddled", false));
        this.setHasEgg(input.getBooleanOr("HasEgg", false));
        this.ownedEggs.clear();
        input.read("OwnedEggs", Codec.LONG_STREAM).ifPresent(stream -> stream.forEach(packed -> this.ownedEggs.add(BlockPos.of(packed))));
        this.readPersistentAngerSaveData(this.level(), input);
        DyeableAnimal.loadDye(this, input);
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        Ostrich baby = NaturalistEntityTypes.OSTRICH.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(mob, this.random));
        }
        return baby;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.05F);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.8D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 1.5D));
        this.goalSelector.addGoal(3, new EggLayingBreedGoal<>(this, 1.0D));
        this.goalSelector.addGoal(3, new LayEggGoal<>(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new HideGoal<>(this));
        this.goalSelector.addGoal(6, new PetFollowOwnerGoal(this, 1.2D, 10.0F, 3.0F));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OstrichHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new OstrichDefendEggGoal(this));
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return PetTargeting.protectsOwnedPet(this, target) && super.canAttack(target);
    }

    @Override
    public boolean canHide() {
        long t = this.level().getGameTime();
        if (t != this.hideCacheTick) {
            this.hideCacheTick = t;
            this.hideCache = this.thinkCanHide();
        }
        return this.hideCache;
    }

    private boolean thinkCanHide() {
        if (this.isTame() || this.isBaby() || this.isAggressive() || this.isVehicle()) {
            return false;
        }
        return NaturalistAnimal.hasVisiblePlayer(this, 16.0D, this.getBoundingBox().inflate(16.0D, 8.0D, 16.0D),
                player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)
                        && !player.isDiscrete() && !player.isHolding(FOOD_ITEMS));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (this.isTame() && this.isOwnedBy(player) && this.isSaddled() && stack.is(NaturalistItemTags.SHEARS)) {
            if (this.level() instanceof ServerLevel serverLevel) {
                this.setSaddled(false);
                this.spawnAtLocation(serverLevel, Items.SADDLE);
                stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                this.gameEvent(GameEvent.SHEAR, player);
            }
            return InteractionResult.SUCCESS;
        }
        Optional<InteractionResult> dyeResult = DyeableAnimal.tryClearDye(this, player, hand);
        if (dyeResult.isEmpty()) {
            dyeResult = DyeableAnimal.tryDye(this, player, hand);
        }
        if (dyeResult.isPresent()) {
            return dyeResult.get();
        }
        if (stack.is(Items.SADDLE) && this.isSaddleable() && !this.isSaddled()) {
            if (!this.level().isClientSide()) {
                this.equipSaddle(stack.split(1), SoundSource.NEUTRAL);
                this.gameEvent(GameEvent.EQUIP, player);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.isTame() && this.isBaby() && this.isFood(stack)) {
            this.ageUp(getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.level().isClientSide()) {
            return (this.isTame() || (this.isBaby() && this.isFood(stack))) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(2.0F);
                return InteractionResult.SUCCESS;
            }
            if (this.isOwnedBy(player) && !this.isBaby() && this.isSaddled() && !this.isFood(stack)
                    && !player.isSecondaryUseActive() && !this.isVehicle() && !this.isInSittingPose()) {
                this.doPlayerRide(player);
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if ((!interactionResult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }
            return interactionResult;
        }
        if (this.isBaby() && this.isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte) 7);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    protected void doPlayerRide(@NotNull Player player) {
        if (!this.level().isClientSide()) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, Entity.@NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(@NotNull Entity entity, @NotNull EntityDimensions dimensions, float scale) {
        return new Vec3(0.0D, 1.55D, -0.17D).scale(scale).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    public void equipSaddle(@NotNull ItemStack stack, @Nullable SoundSource source) {
        this.setSaddled(true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE.value(), source, 0.5F, 1.0F);
        }
    }

    @Override
    protected void dropEquipment(@NotNull ServerLevel level) {
        super.dropEquipment(level);
        if (this.isSaddled()) {
            this.spawnAtLocation(level, Items.SADDLE);
        }
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (this.isSaddled()) {
            if (jumpPower < 0) {
                jumpPower = 0;
            }
            this.playerJumpPendingScale = jumpPower >= 90 ? 1.0F : 0.4F + 0.4F * jumpPower / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int jumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!this.isAlive()) {
            return;
        }
        LivingEntity livingEntity = this.getControllingPassenger();
        if (!this.isVehicle() || livingEntity == null) {
            super.travel(travelVector);
            return;
        }
        this.setYRot(livingEntity.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(livingEntity.getXRot() * 0.5f);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = this.getYRot();
        this.yBodyRot = Mth.rotLerp(0.35F, this.yBodyRot, this.getYRot());
        float g = livingEntity.zza;
        if (this.playerJumpPendingScale > 0.0F && !this.isJumping && this.onGround()) {
            double jumpVelocity = this.getAttributeValue(Attributes.JUMP_STRENGTH) * this.playerJumpPendingScale * this.getBlockJumpFactor() + this.getJumpBoostPower();
            Vec3 deltaMovement = this.getDeltaMovement();
            this.setDeltaMovement(deltaMovement.x, jumpVelocity, deltaMovement.z);
            this.isJumping = true;
            this.needsSync = true;
            if (g > 0.0F) {
                float sin = Mth.sin(this.getYRot() * Mth.DEG_TO_RAD);
                float cos = Mth.cos(this.getYRot() * Mth.DEG_TO_RAD);
                this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * sin * this.playerJumpPendingScale, 0.0D, 0.4F * cos * this.playerJumpPendingScale));
            }
            this.playerJumpPendingScale = 0.0F;
        }
        if (this.onGround() && this.playerJumpPendingScale == 0.0F) {
            this.isJumping = false;
        }
        if (this.isLocalInstanceAuthoritative()) {
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.4F);
            super.travel(new Vec3(livingEntity.xxa * 0.5f, travelVector.y, g));
        } else if (livingEntity instanceof Player) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.calculateEntityAnimation(false);
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected float getFlyingSpeed() {
        return this.getControllingPassenger() instanceof Player ? this.getSpeed() * 0.2F : 0.06F;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 deltaMovement = this.getDeltaMovement();
        if (!this.onGround() && deltaMovement.y < 0.0D) {
            this.setDeltaMovement(deltaMovement.multiply(1.05D, 0.6D, 1.05D));
        }
        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0
                && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
        if (!this.level().isClientSide()) {
            this.updateEggAnger();
            if (this.tickCount % 60 == 0 && !this.ownedEggs.isEmpty()) {
                this.pruneOwnedEggs();
            }
        }
    }

    private void updateEggAnger() {
        if (this.persistentAngerEndTime <= 0L) {
            return;
        }
        LivingEntity target = this.getTarget();
        boolean engaged = target != null && target.isAlive()
                && this.persistentAngerTarget != null && this.persistentAngerTarget.matches(target)
                && this.closerThan(target, this.getAttributeValue(Attributes.FOLLOW_RANGE));
        if (engaged) {
            this.persistentAngerEndTime++;
        } else if (!this.isAngry()) {
            this.stopBeingAngry();
        }
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        this.setAggressive(this.getTarget() != null);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.4D && this.onGround());
        } else {
            this.setSprinting(false);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.OSTRICH_AMBIENT_BABY.get() : NaturalistSoundEvents.OSTRICH_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.OSTRICH_HURT_BABY.get() : NaturalistSoundEvents.OSTRICH_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.OSTRICH_DEATH_BABY.get() : NaturalistSoundEvents.OSTRICH_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    static class OstrichHurtByTargetGoal extends BabyHurtByTargetGoal {
        private final Ostrich ostrich;

        public OstrichHurtByTargetGoal(Ostrich ostrich) {
            super(ostrich);
            this.ostrich = ostrich;
        }

        @Override
        public boolean canUse() {
            return !this.ostrich.isTame() && super.canUse();
        }
    }

    static class OstrichDefendEggGoal extends NearestAttackableTargetGoal<Player> {
        private final Ostrich ostrich;

        public OstrichDefendEggGoal(Ostrich ostrich) {
            super(ostrich, Player.class, 10, true, false,
                    (entity, level) -> !ostrich.isBaby() && !ostrich.isTame() && ostrich.isNearOwnedEgg(entity));
            this.ostrich = ostrich;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.ostrich.getTarget();
            return super.canContinueToUse() && target != null && this.ostrich.isNearOwnedEgg(target);
        }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean flapping = !this.onGround() && !this.isInWater() && !this.isBaby();
        boolean sitting = !flapping && this.isInSittingPose();
        boolean moving = !flapping && !sitting && NaturalistAnimal.isVisiblyMoving(this);
        boolean running = moving && (this.isSprinting() || this.isVehicle());
        boolean burying = !flapping && !sitting && !moving && this.canHide();

        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);

        this.flapAnimationState.animateWhen(flapping, this.tickCount);
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.walkAnimationState.animateWhen(moving && !running, this.tickCount);
        this.runAnimationState.animateWhen(running, this.tickCount);
        this.buryAnimationState.animateWhen(burying, this.tickCount);
        this.idleAnimationState.animateWhen(!flapping && !sitting && !moving && !burying, this.tickCount);
    }

    @Override
    public float getRenderPitch() {
        return 0.0F;
    }

    @Override
    public float getRenderRoll() {
        return 0.0F;
    }
    //endregion
}
