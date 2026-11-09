package com.crispytwig.naturalist.common.entity.fish;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.common.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.common.registry.NaturalistRegistry;
import com.crispytwig.naturalist.common.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.common.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.common.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.common.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.common.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.common.entity.FishSwimTilt;
import com.crispytwig.naturalist.common.entity.SmoothAnimationState;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class Ray extends AbstractFish implements DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"eagle_ray", "mobula_ray", "stingray"};

    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("ray"), "eagle_ray");

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Ray.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_BURIED = SynchedEntityData.defineId(Ray.class, EntityDataSerializers.BOOLEAN);

    private static final int ESCAPE_TICKS = 80;
    private static final int SINK_TICKS = 60;
    private static final int BURY_TICKS = 60;
    private static final int ATTACK_MODE_TICKS = 40;
    private static final int HUNT_COOLDOWN_TICKS = 2400;
    private static final int WATER_SEARCH_INTERVAL = 20;
    private static final int WATER_SEARCH_HORIZONTAL = 32;
    private static final int WATER_SEARCH_VERTICAL = 5;
    private static final int FLOOR_SEARCH_DEPTH = 5;
    private static final double STING_RANGE_SQR = 4.0D;
    private static final double STING_HEIGHT = 1.0D;
    private static final double BEACHED_SPEED = 0.12D;
    private static final double SINK_SPEED = 0.1D;

    private int escapeTicks;
    private int sinkTicks;
    private int buryTicks;
    private int attackModeTicks;
    private int huntCooldown;
    private int waterSearchDelay;
    private @Nullable BlockPos waterTarget;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState hideAnimationState = SmoothAnimationState.pose();

    private static final AnimationSoundTrack SWIM_SOUNDS = AnimationSoundTrack.builder(6.0833F, true)
            .at(0.0F, NaturalistSoundEvents.RAY_SWIM, 0.3F, 1.0F)
            .at(1.0F, NaturalistSoundEvents.RAY_SWIM, 0.3F, 1.0F)
            .at(2.25F, NaturalistSoundEvents.RAY_SWIM, 0.3F, 1.0F)
            .at(3.5F, NaturalistSoundEvents.RAY_SWIM, 0.3F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.swimAnimationState, SWIM_SOUNDS);

    private static final float FIN_LAG = 0.15F;
    private static final float TAIL_LAG = 0.09F;

    public final FishSwimTilt swimTilt = new FishSwimTilt();

    public float finLag;
    public float finLagO;
    public float tailLag;
    public float tailLagO;

    public Ray(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, DEFAULT_VARIANT.identifier().toString());
        builder.define(DATA_BURIED, false);
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
        return Naturalist.location("textures/entity/ray/eagle_ray.png");
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
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putInt("HuntCooldown", this.huntCooldown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.huntCooldown = input.getIntOr("HuntCooldown", 0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (reason != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, this::saveVariant);
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(custom);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        this.loadVariant(tag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.RAY_BUCKET.get());
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return NaturalistSoundEvents.CATFISH_FLOP.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.RAY_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.RAY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.RAY_DEATH.get();
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<LivingEntity>(this, LivingEntity.class, 5.0F, 1.15D, 1.25D, this::isThreatWhileSwimming) {
            @Override
            public void start() {
                super.start();
                Ray.this.startEscaping();
            }
        });
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 20));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new StingTargetGoal<>(LivingEntity.class, this::isBuried, (target, level) -> this.isThreat(target) && this.inStingRange(target)));
        this.targetSelector.addGoal(3, new StingTargetGoal<>(Squid.class, () -> this.huntCooldown <= 0, (target, level) -> this.inStingRange(target)));
    }

    private boolean isThreat(LivingEntity entity) {
        return entity instanceof Player || entity instanceof Enemy;
    }

    private boolean isThreatWhileSwimming(LivingEntity entity) {
        return this.isUnderWater() && this.isThreat(entity);
    }

    private boolean inStingRange(LivingEntity target) {
        return this.distanceToSqr(target) <= STING_RANGE_SQR && Math.abs(target.getY() - this.getY()) <= STING_HEIGHT;
    }

    public boolean isBuried() {
        return this.entityData.get(DATA_BURIED);
    }

    private void setBuried(boolean buried) {
        this.entityData.set(DATA_BURIED, buried);
    }

    private void startEscaping() {
        if (this.escapeTicks <= 0 && this.sinkTicks <= 0 && !this.isBuried()) {
            this.escapeTicks = ESCAPE_TICKS;
        }
    }

    private void holdStill() {
        this.getNavigation().stop();
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
    }

    private void bury() {
        this.sinkTicks = 0;
        this.buryTicks = BURY_TICKS;
        this.setBuried(true);
        this.holdStill();
    }

    private void stopHiding() {
        this.escapeTicks = 0;
        this.sinkTicks = 0;
        this.setBuried(false);
        this.updateControlFlags();
    }

    private void finishEscaping() {
        if (this.getTarget() != null) {
            return;
        }
        if (this.onGround()) {
            this.bury();
        } else if (this.hasFloorBelow()) {
            this.sinkTicks = SINK_TICKS;
            this.holdStill();
        }
    }

    private boolean hasFloorBelow() {
        BlockPos.MutableBlockPos pos = this.blockPosition().mutable();
        for (int i = 0; i < FLOOR_SEARCH_DEPTH; i++) {
            pos.move(Direction.DOWN);
            if (this.level().getBlockState(pos).isFaceSturdy(this.level(), pos, Direction.UP)) {
                return true;
            }
            if (!this.level().getFluidState(pos).is(FluidTags.WATER)) {
                return false;
            }
        }
        return false;
    }

    private void tickSinking() {
        if (this.onGround()) {
            this.bury();
        } else if (--this.sinkTicks <= 0 || this.getTarget() != null) {
            this.stopHiding();
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8D, 0.0D, 0.8D).add(0.0D, -SINK_SPEED, 0.0D));
        }
    }

    private void tickBuried() {
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
        if (this.getTarget() != null || --this.buryTicks <= 0 || !this.onGround()) {
            this.stopHiding();
        }
    }

    private void moveTowardsWater() {
        if (--this.waterSearchDelay <= 0) {
            this.waterSearchDelay = WATER_SEARCH_INTERVAL;
            this.waterTarget = this.findNearestWater();
        }
        if (this.waterTarget == null) {
            return;
        }
        double dx = this.waterTarget.getX() + 0.5D - this.getX();
        double dz = this.waterTarget.getZ() + 0.5D - this.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        if (distance < 0.01D) {
            return;
        }
        this.setDeltaMovement(dx / distance * BEACHED_SPEED, this.getDeltaMovement().y, dz / distance * BEACHED_SPEED);
        float yaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
    }

    private @Nullable BlockPos findNearestWater() {
        for (BlockPos pos : BlockPos.withinBoxByManhattanDistance(this.blockPosition(), WATER_SEARCH_HORIZONTAL, WATER_SEARCH_VERTICAL, WATER_SEARCH_HORIZONTAL)) {
            if (this.level().hasChunkAt(pos) && this.level().getFluidState(pos).is(FluidTags.WATER)) {
                return pos.immutable();
            }
        }
        return null;
    }

    @Override
    protected void updateControlFlags() {
        super.updateControlFlags();
        if (this.isBuried() || this.sinkTicks > 0) {
            this.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
        }
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.huntCooldown > 0) {
            this.huntCooldown--;
        }
        if (this.attackModeTicks > 0 && --this.attackModeTicks == 0) {
            this.setTarget(null);
        }
        if (!this.isInWater()) {
            if (this.isBuried() || this.sinkTicks > 0 || this.escapeTicks > 0) {
                this.stopHiding();
            }
            this.moveTowardsWater();
            return;
        }
        this.waterTarget = null;
        if (this.isBuried()) {
            this.tickBuried();
        } else if (this.sinkTicks > 0) {
            this.tickSinking();
        } else if (this.escapeTicks > 0 && --this.escapeTicks == 0) {
            this.finishEscaping();
        }
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        boolean hurt = super.hurtServer(level, source, amount);
        if (hurt && !this.level().isClientSide() && source.getEntity() instanceof LivingEntity attacker && attacker != this) {
            this.attackModeTicks = 0;
            this.setTarget(attacker);
        }
        return hurt;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity target) {
        boolean result = super.doHurtTarget(level, target);
        if (result && !this.level().isClientSide() && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), this);
            if (!living.isAlive() && !(living instanceof Player)) {
                this.huntCooldown = HUNT_COOLDOWN_TICKS;
            }
        }
        return result;
    }

    public float getFinLag(float partialTick) {
        return Mth.lerp(partialTick, this.finLagO, this.finLag);
    }

    public float getTailLag(float partialTick) {
        return Mth.lerp(partialTick, this.tailLagO, this.tailLag);
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            this.swimTilt.tick(this);
            this.finLagO = this.finLag;
            this.tailLagO = this.tailLag;
            this.finLag += (this.swimTilt.swimPitch - this.finLag) * FIN_LAG;
            this.tailLag += (this.swimTilt.swimPitch - this.tailLag) * TAIL_LAG;
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        boolean buried = this.isBuried();
        this.swimAnimationState.animateWhen(inWater && !buried, this.tickCount);
        this.idleAnimationState.animateWhen(!inWater || buried, this.tickCount);
        this.hideAnimationState.animateWhen(buried, this.tickCount);
    }
    //endregion

    private class StingTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private final BooleanSupplier canSearch;

        private StingTargetGoal(Class<T> targetType, BooleanSupplier canSearch, TargetingConditions.Selector selector) {
            super(Ray.this, targetType, 20, false, false, selector);
            this.canSearch = canSearch;
        }

        @Override
        public boolean canUse() {
            return this.canSearch.getAsBoolean() && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            Ray.this.attackModeTicks = ATTACK_MODE_TICKS;
        }
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
