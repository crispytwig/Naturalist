package com.crispytwig.naturalist.world.entity.animal.hedgehog;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistDamageTypes;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.DistancedFollowParentGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.HideGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.HidingAnimal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Hedgehog extends TamableAnimal implements DyeableAnimal, FollowingPet, HidingAnimal, Catchable, DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"brown", "dark", "white"};

    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.HEDGEHOG_FOOD_ITEMS);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Hedgehog.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_DYE = SynchedEntityData.defineId(Hedgehog.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ROLLING = SynchedEntityData.defineId(Hedgehog.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Hedgehog.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GLINT = SynchedEntityData.defineId(Hedgehog.class, EntityDataSerializers.BOOLEAN);

    private boolean followingOwner = true;
    private boolean wasHiding;
    private int unhideAnimTicks;
    private int idleEventTicks;
    private int followCooldown;
    private int rolledPoseTicks;
    private long hideCacheTick = Long.MIN_VALUE;
    private boolean hideCache;
    private ItemEnchantments throwEnchantments = ItemEnchantments.EMPTY;
    @Nullable
    private UUID throwerUUID;
    private boolean returning;
    private int returnTicks;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleEventAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState hideAnimationState = SmoothAnimationState.instant();
    public final SmoothAnimationState unhideAnimationState = SmoothAnimationState.instant();
    public final SmoothAnimationState rollGroundAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState rollAirAnimationState = new SmoothAnimationState();

    public Hedgehog(@NotNull EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 12.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, NaturalistMobVariants.HEDGEHOG_BROWN.identifier().toString());
        builder.define(DATA_DYE, -1);
        builder.define(ROLLING, false);
        builder.define(FROM_HAND, false);
        builder.define(GLINT, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.HEDGEHOG_BROWN;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/hedgehog/brown.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
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

    public boolean isRolling() {
        return this.entityData.get(ROLLING);
    }

    public void setRolling(boolean rolling) {
        this.entityData.set(ROLLING, rolling);
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
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromHand", this.fromHand());
        if (!this.throwEnchantments.keySet().isEmpty()) {
            output.store("ThrowEnchantments", ItemEnchantments.CODEC, this.throwEnchantments);
        }
        DyeableAnimal.saveDye(this, output);
        FollowingPet.savePet(this, output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromHand(input.getBooleanOr("FromHand", false));
        input.read("ThrowEnchantments", ItemEnchantments.CODEC).ifPresent(this::setThrowEnchantments);
        DyeableAnimal.loadDye(this, input);
        FollowingPet.loadPet(this, input);
    }

    @Override
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(tag);
        tag.putInt("Age", this.getAge());
        DyeColor color = this.getDyeColor();
        tag.putInt("DyeColor", color == null ? -1 : color.getId());
        Catchable.saveTamableDataToHandTag(this, tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        if (!this.throwEnchantments.keySet().isEmpty()) {
            stack.set(DataComponents.ENCHANTMENTS, this.throwEnchantments);
        }
    }

    @Override
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);
        this.setAge(tag.getIntOr("Age", 0));
        DyeableAnimal.loadDye(this, tag);
        Catchable.loadTamableDataFromHandTag(this, tag);
    }

    @Override
    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.HEDGEHOG.get());
    }

    @Nullable
    @Override
    public SoundEvent getPickupSound() {
        return null;
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public Hedgehog getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Hedgehog baby = NaturalistEntityTypes.HEDGEHOG.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
            if (this.isTame()) {
                baby.setOwnerReference(this.getOwnerReference());
                baby.setTame(true, true);
                baby.setDyeColor(this.getDyeColor() == null ? DyeColor.RED : this.getDyeColor());
            }
        }
        return baby;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.05F);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new HedgehogPanicGoal(this));
        this.goalSelector.addGoal(2, new HideGoal<>(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.5, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new DistancedFollowParentGoal(this, 1.1, 16.0, 8.0, 5.0));
        this.goalSelector.addGoal(5, new PetFollowOwnerGoal(this, 1.2, 10.0F, 3.0F) {
            @Override
            public boolean canUse() {
                return !Hedgehog.this.isRolling() && Hedgehog.this.followCooldown <= 0 && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !Hedgehog.this.isRolling() && Hedgehog.this.followCooldown <= 0 && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F, 0.02F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canHide() {
        long gameTime = this.level().getGameTime();
        if (gameTime != this.hideCacheTick) {
            this.hideCacheTick = gameTime;
            this.hideCache = this.thinkCanHide();
        }
        return this.hideCache;
    }

    private boolean thinkCanHide() {
        if (this.isTame() || this.isRolling() || this.isSprinting() || this.isInSittingPose()) {
            return false;
        }
        return NaturalistAnimal.hasVisiblePlayer(this, 6.0, this.getBoundingBox().inflate(6.0, 3.0, 6.0),
                player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && !player.isCrouching() && !FOOD_ITEMS.test(player.getMainHandItem()) && !FOOD_ITEMS.test(player.getOffhandItem()));
    }

    @Override
    public void tame(@NotNull Player player) {
        super.tame(player);
        if (this.getDyeColor() == null) {
            this.setDyeColor(DyeColor.RED);
        }
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return this.isTame() && otherAnimal instanceof Hedgehog hedgehog && hedgehog.isTame() && super.canMate(otherAnimal);
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull ServerLevel level, @NotNull DamageSource source) {
        return super.isInvulnerableTo(level, source) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.SWEET_BERRY_BUSH)
                || this.isRolling() && source.is(DamageTypeTags.IS_FIRE);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (this.isTame()) {
            Optional<InteractionResult> caught = Catchable.catchAnimal(player, hand, this, true);
            if (caught.isPresent()) {
                return caught.get();
            }
        }
        Optional<InteractionResult> dyeResult = DyeableAnimal.tryClearDye(this, player, hand);
        if (dyeResult.isEmpty()) {
            dyeResult = DyeableAnimal.tryDye(this, player, hand);
        }
        if (dyeResult.isPresent()) {
            return dyeResult.get();
        }
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack)) {
            if (!this.isTame()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!this.level().isClientSide()) {
                    if (this.random.nextBoolean()) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            if (this.isOwnedBy(player) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(2.0F);
                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(player, hand);
        }
        if (this.isTame() && this.isOwnedBy(player) && stack.isEmpty() && !player.isSecondaryUseActive()) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected int getBaseExperienceReward(@NotNull ServerLevel level) {
        return 1 + this.random.nextInt(3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.followCooldown > 0) {
                this.followCooldown--;
            }
            if (!this.isRolling() && this.isNoGravity()) {
                this.setNoGravity(false);
            }
            if (this.isRolling()) {
                boolean flaming = this.getThrowEnchantmentLevel(Enchantments.FLAME) > 0;
                if (flaming) {
                    this.setRemainingFireTicks(2);
                }
                if (this.returning) {
                    this.tickReturn();
                } else {
                    for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2), this::isSpikeTarget)) {
                        Vec3 motion = this.getDeltaMovement();
                        DamageSource spikeSource = this.damageSources().thrown(this, this.getOwner());
                        float spikeDamage = 2.0F + this.getThrowEnchantmentLevel(Enchantments.THORNS);
                        if (target.hurtServer(serverLevel, spikeSource, spikeDamage)) {
                            int punch = this.getThrowEnchantmentLevel(Enchantments.PUNCH);
                            if (punch > 0) {
                                target.knockback(punch * 0.6, -motion.x, -motion.z);
                            }
                            if (flaming) {
                                target.igniteForSeconds(5.0F);
                            }
                            ItemStack particleStack = this.getCaughtItemStack();
                            this.saveToHandTag(particleStack);
                            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(particleStack)), this.getX(), this.getY(0.5), this.getZ(), 8, 0.1, 0.1, 0.1, 0.05);
                            if (this.random.nextInt(this.getThrowEnchantmentLevel(Enchantments.UNBREAKING) + 1) == 0) {
                                Holder<DamageType> throwDamage = this.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(NaturalistDamageTypes.HEDGEHOG_THROW);
                                this.hurtServer(serverLevel, new DamageSource(throwDamage, target), 2.0F);
                            }
                            if (!this.isDeadOrDying() && this.getThrowEnchantmentLevel(Enchantments.LOYALTY) > 0) {
                                this.startReturn(motion);
                            } else {
                                this.setDeltaMovement(Vec3.ZERO);
                                this.stopRolling();
                            }
                            break;
                        }
                    }
                    if (this.isRolling() && !this.returning && (this.onGround() || this.isInWater())) {
                        this.stopRolling();
                    }
                }
            } else if (this.canHide() && this.tickCount % 10 == 0) {
                for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2), this::isSpikeTarget)) {
                    target.hurtServer(serverLevel, this.damageSources().mobAttack(this), 2.0F);
                }
            }
        }
    }

    private boolean isSpikeTarget(LivingEntity entity) {
        return !(entity instanceof Hedgehog) && entity.isAlive()
                && !(entity instanceof Player player && this.isOwnedBy(player));
    }

    public ItemEnchantments getThrowEnchantments() {
        return this.throwEnchantments;
    }

    public void setThrowEnchantments(ItemEnchantments enchantments) {
        this.throwEnchantments = enchantments;
        this.entityData.set(GLINT, !enchantments.keySet().isEmpty());
    }

    public boolean hasThrowEnchantments() {
        return this.entityData.get(GLINT);
    }

    public void setThrower(@Nullable UUID thrower) {
        this.throwerUUID = thrower;
    }

    @Nullable
    private Player getThrower() {
        Player thrower = this.throwerUUID == null ? null : this.level().getPlayerByUUID(this.throwerUUID);
        return thrower != null ? thrower : this.getOwner() instanceof Player owner ? owner : null;
    }

    public int getThrowEnchantmentLevel(ResourceKey<Enchantment> key) {
        return this.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key).map(this.throwEnchantments::getLevel).orElse(0);
    }

    private void startReturn(Vec3 motion) {
        this.returning = true;
        this.returnTicks = 0;
        this.setNoGravity(true);
        this.setDeltaMovement(motion.scale(-0.5).add(0.0, 0.3, 0.0));
    }

    private void tickReturn() {
        Player thrower = this.getThrower();
        if (this.isDeadOrDying() || thrower == null || !thrower.isAlive() || ++this.returnTicks > 200) {
            this.stopRolling();
            return;
        }
        Vec3 toThrower = thrower.position().add(0.0, (thrower.getBbHeight() - this.getBbHeight()) / 2.0, 0.0).subtract(this.position());
        double distance = toThrower.length();
        if (distance < 0.25) {
            this.setNoGravity(false);
            ItemStack stack = this.getCaughtItemStack();
            this.saveToHandTag(stack);
            if (!thrower.getInventory().add(stack)) {
                thrower.drop(stack, false);
            }
            this.playSound(SoundEvents.ITEM_PICKUP, 0.3F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            this.discard();
            return;
        }
        if (distance < 1.5) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPos(this.position().add(distance > 0.5 ? toThrower.normalize().scale(0.5) : toThrower));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85).add(toThrower.normalize().scale(0.25)));
        }
    }

    private void stopRolling() {
        this.setRolling(false);
        this.returning = false;
        this.setNoGravity(false);
        this.clearFire();
        this.followCooldown = 40;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.canHide() || this.isRolling() ? null : NaturalistSoundEvents.HEDGEHOG_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.HEDGEHOG_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.HEDGEHOG_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    private static class HedgehogPanicGoal extends PanicGoal {
        public HedgehogPanicGoal(Hedgehog hedgehog) {
            super(hedgehog, 2.0);
        }

        @Override
        public void start() {
            super.start();
            this.mob.setSprinting(true);
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setSprinting(false);
        }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        boolean hiding = this.canHide();
        if (!hiding && this.wasHiding) {
            this.unhideAnimTicks = 10;
            if (!this.level().isClientSide()) {
                this.playSound(NaturalistSoundEvents.HEDGEHOG_UNHIDE.get(), 0.6F, 1.0F);
            }
        }
        if (hiding) {
            this.unhideAnimTicks = 0;
            if (!this.wasHiding && !this.level().isClientSide()) {
                this.playSound(NaturalistSoundEvents.HEDGEHOG_HIDE.get(), 0.6F, 1.0F);
            }
        }
        this.wasHiding = hiding;
        if (this.unhideAnimTicks > 0) {
            this.unhideAnimTicks--;
        }
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    public boolean isFullyRolled() {
        return this.rolledPoseTicks >= 3;
    }

    private void setupAnimationStates() {
        boolean rollingAir = this.isRolling();
        boolean hiding = !rollingAir && this.canHide();
        boolean unhiding = !rollingAir && !hiding && this.unhideAnimTicks > 0;
        boolean sitting = !rollingAir && !hiding && !unhiding && this.isInSittingPose();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean rollingGround = !rollingAir && !hiding && !unhiding && !sitting && moving && this.isSprinting();
        boolean posing = rollingAir || hiding || unhiding || sitting || rollingGround;
        this.rolledPoseTicks = rollingAir || hiding || rollingGround || (unhiding && this.unhideAnimTicks > 1) ? this.rolledPoseTicks + 1 : 0;
        boolean idle = !posing && !moving;

        if (!idle) {
            this.idleEventTicks = 0;
        } else if (this.idleEventTicks > 0) {
            this.idleEventTicks--;
        } else if (this.random.nextInt(1000) == 0) {
            this.idleEventTicks = 18;
        }
        this.rollAirAnimationState.animateWhen(rollingAir, this.tickCount);
        this.hideAnimationState.animateWhen(hiding, this.tickCount);
        this.unhideAnimationState.animateWhen(unhiding, this.tickCount);
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.rollGroundAnimationState.animateWhen(rollingGround, this.tickCount);
        this.walkAnimationState.animateWhen(!posing && moving, this.tickCount);
        this.idleEventAnimationState.animateWhen(idle && this.idleEventTicks > 0, this.tickCount);
        this.idleAnimationState.animateWhen(idle && this.idleEventTicks <= 0, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
