package com.crispytwig.naturalist.world.entity.animal.crab;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.world.entity.HidingAnimal;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Crab extends TamableAnimal implements HidingAnimal, FollowingPet, Catchable, DataDrivenVariantAnimal {

    //region Data
    public static final String[] VARIANT_NAMES = {"blue", "brown", "orange", "red", "yellow"};

    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("crab"), "blue");

    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.CRAB_FOOD);
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);

    private static final int SWING_ANIM_TICKS = 13;

    private boolean followingOwner = true;
    private boolean wasHiding;
    private int unhideAnimTicks;
    private int hideElapsed;
    private int peekTicksLeft;
    private int peekCooldown = 50;
    private int swingAnimTicks;
    private boolean hideCache;
    private long hideCacheTick = -1L;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState hideAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState hideLoopAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState peekAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState unhideAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swingAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack WALK_SOUNDS = AnimationSoundTrack.builder(0.5F, true)
            .at(0.0F, NaturalistSoundEvents.CRAB_STEP, 0.3F, 1.0F)
            .at(0.25F, NaturalistSoundEvents.CRAB_STEP, 0.3F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.walkAnimationState, WALK_SOUNDS);

    public Crab(@NotNull EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, DEFAULT_VARIANT.identifier().toString());
        builder.define(DATA_DANCING, false);
        builder.define(FROM_HAND, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return DEFAULT_VARIANT;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/crab/blue_crab.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_DANCING);
    }

    public void setDancing(boolean dancing) {
        this.entityData.set(DATA_DANCING, dancing);
    }

    @Override
    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    @Override
    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
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
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromHand", this.fromHand());
        FollowingPet.savePet(this, output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromHand(input.getBooleanOr("FromHand", false));
        FollowingPet.loadPet(this, input);
    }

    @Override
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(tag);
        tag.putInt("Age", this.getAge());
        ItemStack held = this.getMainHandItem();
        if (!held.isEmpty()) {
            DynamicOps<Tag> ops = this.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            tag.store("HeldItem", ItemStack.CODEC, ops, held);
        }
        Catchable.saveTamableDataToHandTag(this, tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);
        if (tag.contains("Age")) {
            this.setAge(tag.getIntOr("Age", 0));
        }
        if (tag.contains("HeldItem")) {
            DynamicOps<Tag> ops = this.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            ItemStack held = tag.read("HeldItem", ItemStack.CODEC, ops).orElse(ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.MAINHAND, held);
            this.setDropChance(EquipmentSlot.MAINHAND, 2.0F);
        }
        Catchable.loadTamableDataFromHandTag(this, tag);
    }

    @Override
    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.CRAB.get());
    }

    @Nullable
    @Override
    public SoundEvent getPickupSound() {
        return null;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }
    //endregion

    //region Spawning / Misc.
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    public static boolean checkCrabSpawnRules(EntityType<Crab> type, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        boolean validGround = below.is(BlockTags.SAND) || below.is(BlockTags.SUBSTRATE_OVERWORLD) || below.is(Blocks.GRAVEL) || below.is(Blocks.STONE);
        return validGround && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setCanPickUpLoot(true);
        if (spawnType != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this || !(otherAnimal instanceof Crab)) {
            return false;
        }
        return this.isInLove() && otherAnimal.isInLove();
    }

    @Nullable
    @Override
    public Crab getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Crab baby = NaturalistEntityTypes.CRAB.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
            if (this.isTame()) {
                baby.setOwnerReference(this.getOwnerReference());
                baby.setTame(true, true);
            }
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.65D, true));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(4, new GrabWeaponGoal(this));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(7, new PetFollowOwnerGoal(this, 1.5D, 5.0F, 1.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return !Crab.this.getMainHandItem().isEmpty() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this) {
            @Override
            public boolean canUse() {
                return !Crab.this.getMainHandItem().isEmpty() && super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                if (Crab.this.getMainHandItem().isEmpty()) {
                    return false;
                }
                LivingEntity attacker = Crab.this.getLastHurtByMob();
                if (Crab.this.isTame() && attacker != null && attacker == Crab.this.getOwner()) {
                    return false;
                }
                return super.canUse();
            }
        });
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }

        Optional<InteractionResult> caught = Catchable.catchAnimal(player, hand, this, true);
        if (caught.isPresent()) {
            return caught.get();
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (this.isFood(stack)) {
                if (this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.heal(2.0F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (!this.level().isClientSide()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return InteractionResult.SUCCESS;
            }
        } else if (!this.isTame() && this.isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.setFollowingOwner(true);
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean wantsToPickUp(@NotNull ServerLevel level, @NotNull ItemStack stack) {
        return this.getMainHandItem().isEmpty() && isWeapon(stack);
    }

    private static boolean isWeapon(ItemStack stack) {
        return stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES) || stack.is(ItemTags.PICKAXES)
                || stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.HOES);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity target) {
        boolean hurt = super.doHurtTarget(level, target);
        if (hurt) {
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(NaturalistSoundEvents.CRAB_PINCER.get(), 1.0F, 1.0F);
            ItemStack weapon = this.getMainHandItem();
            if (!weapon.isEmpty() && weapon.isDamageableItem()) {
                weapon.hurtAndBreak(1, this, EquipmentSlot.MAINHAND);
            }
        }
        return hurt;
    }

    @Override
    public void tick() {
        super.tick();
        boolean hide = this.canHide();
        if (!hide && this.wasHiding) {
            this.unhideAnimTicks = 10;
        }
        if (hide) {
            this.unhideAnimTicks = 0;
        }
        this.wasHiding = hide;
        if (this.unhideAnimTicks > 0) {
            this.unhideAnimTicks--;
        }

        if (hide) {
            this.hideElapsed++;
            if (this.hideElapsed > 38) {
                if (this.peekTicksLeft > 0) {
                    this.peekTicksLeft--;
                } else if (--this.peekCooldown <= 0) {
                    this.peekTicksLeft = 23;
                    this.peekCooldown = 60 + this.random.nextInt(100);
                }
            }
        } else {
            this.hideElapsed = 0;
            this.peekTicksLeft = 0;
            this.peekCooldown = 50;
        }

        if (!this.level().isClientSide() && this.tickCount % 20 == 0) {
            this.setDancing(this.isTame() && !this.isOrderedToSit() && this.isJukeboxNearby());
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean hiding = this.canHide();
        boolean unhiding = !hiding && this.unhideAnimTicks > 0;
        boolean peeking = hiding && this.peekTicksLeft > 0;
        boolean hideLoop = hiding && !peeking && this.hideElapsed > 38;

        this.hideAnimationState.animateWhen(hiding && !peeking && !hideLoop, this.tickCount);
        this.hideLoopAnimationState.animateWhen(hideLoop, this.tickCount);
        this.peekAnimationState.animateWhen(peeking, this.tickCount);
        this.unhideAnimationState.animateWhen(unhiding, this.tickCount);

        boolean hidden = hiding || unhiding;
        boolean dancing = !hidden && this.isDancing();
        boolean sitting = !hidden && !dancing && this.isInSittingPose();
        boolean posing = hidden || dancing || sitting;
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);

        this.danceAnimationState.animateWhen(dancing, this.tickCount);
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.walkAnimationState.animateWhen(!posing && moving, this.tickCount);
        this.idleAnimationState.animateWhen(!posing && !moving, this.tickCount);

        if (this.swinging && this.swingAnimTicks <= 0) {
            this.swingAnimTicks = SWING_ANIM_TICKS;
        } else if (this.swingAnimTicks > 0) {
            this.swingAnimTicks--;
        }
        this.swingAnimationState.animateWhen(this.swingAnimTicks > 0 && !this.isDancing(), this.tickCount);
    }

    private boolean isJukeboxNearby() {
        BlockPos pos = this.blockPosition();
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-3, -1, -3), pos.offset(3, 1, 3))) {
            BlockState st = this.level().getBlockState(p);
            if (st.is(Blocks.JUKEBOX) && st.hasProperty(JukeboxBlock.HAS_RECORD) && st.getValue(JukeboxBlock.HAS_RECORD)) {
                return true;
            }
        }
        return false;
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
        if (this.isBaby() || this.isTame() || !this.getMainHandItem().isEmpty()) {
            return false;
        }
        boolean playerNear = NaturalistAnimal.hasVisiblePlayer(this, 4.0D, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D),
                player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && !player.isCrouching() && !FOOD_ITEMS.test(player.getMainHandItem()) && !FOOD_ITEMS.test(player.getOffhandItem()));
        return playerNear && this.findNearbyWeapon() == null;
    }

    @Nullable
    private ItemEntity findNearbyWeapon() {
        List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(8.0D),
                e -> e.isAlive() && !e.hasPickUpDelay() && isWeapon(e.getItem()));
        if (items.isEmpty()) {
            return null;
        }
        items.sort(Comparator.comparingDouble(this::distanceToSqr));
        return items.getFirst();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.canHide() ? strength / 4 : strength, x, z);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        return super.hurtServer(level, source, this.canHide() ? amount * 0.8F : amount);
    }

    private boolean isImmobilized() {
        return this.canHide() || this.isDancing();
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.isImmobilized()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
            super.travel(Vec3.ZERO);
            return;
        }
        super.travel(vec3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isImmobilized()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
            this.getNavigation().stop();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.CRAB_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.CRAB_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.CRAB_DEATH.get();
    }

    static class GrabWeaponGoal extends Goal {
        private final Crab crab;
        @Nullable
        private ItemEntity target;

        GrabWeaponGoal(Crab crab) {
            this.crab = crab;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (crab.isInSittingPose() || !crab.getMainHandItem().isEmpty()) {
                return false;
            }
            this.target = crab.findNearbyWeapon();
            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && !this.target.hasPickUpDelay()
                    && crab.getMainHandItem().isEmpty();
        }

        @Override
        public void start() {
            if (this.target != null) {
                crab.getNavigation().moveTo(this.target, 1.3D);
            }
        }

        @Override
        public void stop() {
            this.target = null;
            crab.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.target != null) {
                crab.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
                if (crab.getNavigation().isDone()) {
                    crab.getNavigation().moveTo(this.target, 1.3D);
                }
            }
        }
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
