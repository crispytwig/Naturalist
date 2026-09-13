package com.crispytwig.naturalist.world.entity.animal.firefly;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.FlyingWanderGoal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public class Firefly extends NaturalistAnimal implements DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> GLOW_TICKS_REMAINING = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GLOW_START_TICK = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SUN_TICKS = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();

    public Firefly(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(GLOW_TICKS_REMAINING, 0);
        builder.define(GLOW_START_TICK, 0);
        builder.define(SUN_TICKS, 0);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/firefly.png");
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
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
    }

    public boolean isGlowing() {
        return this.entityData.get(GLOW_TICKS_REMAINING) > 0;
    }

    public int getGlowTicksRemaining() {
        return this.entityData.get(GLOW_TICKS_REMAINING);
    }

    public int getGlowStartTick() {
        return this.entityData.get(GLOW_START_TICK);
    }

    public int getGlowLuminance() {
        if (!isGlowing()) return 0;
        int elapsed = this.tickCount - this.getGlowStartTick();
        if (elapsed >= 30) return 0;
        return Math.max(1, (int) (10.0 * Math.min(1.0, 1.5 * Math.sin(elapsed * Math.PI / 30.0))));
    }

    private void setGlowTicks(int ticks) {
        this.entityData.set(GLOW_TICKS_REMAINING, ticks);
    }

    public int getSunTicks() {
        return this.entityData.get(SUN_TICKS);
    }

    private void setSunTicks(int ticks) {
        this.entityData.set(SUN_TICKS, ticks);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.hasCustomName();
    }
    //endregion

    //region Spawning
    public static boolean checkFireflySpawnRules(EntityType<? extends Firefly> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return Monster.isDarkEnoughToSpawn(level, pos, random) && level.getBlockState(pos.below()).is(NaturalistBlockTags.FIREFLIES_SPAWNABLE_ON);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FireflyHideInGrassGoal(this, 1.2F, 10, 4));
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(3, new FloatGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.getNodeEvaluator().setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    public boolean isFlying() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int ticks = this.getGlowTicksRemaining();
        if (ticks > 0) {
            this.setGlowTicks(ticks - 1);
        }
        if (this.canGlow()) {
            if (this.random.nextFloat() <= 0.01 && !this.isGlowing()) {
                this.setGlowTicks(40 + this.random.nextInt(20));
                this.entityData.set(GLOW_START_TICK, this.tickCount);
            }
        }
        if (this.isSunBurnTick()) {
            this.setSunTicks(this.getSunTicks() + 1);
            if (this.getSunTicks() > 600) {
                BlockPos pos = this.blockPosition();
                if (!level().isClientSide()) {
                    for(int i = 0; i < 20; ++i) {
                        double x = random.nextGaussian() * 0.02D;
                        double y = random.nextGaussian() * 0.02D;
                        double z = random.nextGaussian() * 0.02D;
                        ((ServerLevel)level()).sendParticles(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1, x, y, z, 0.15F);
                    }
                }
                level().playSound(null, this.blockPosition(), NaturalistSoundEvents.FIREFLY_HIDE.get(), SoundSource.NEUTRAL, 0.7F, 0.9F + level().getRandom().nextFloat() * 0.2F);
                this.discard();
            }
        }
    }

    private boolean canGlow() {
        if (!this.level().isClientSide()) {
            return this.level().isDarkOutside() || this.level().getMaxLocalRawBrightness(this.blockPosition()) < 8;
        }
        return false;
    }

    protected boolean isSunBurnTick() {
        if (this.level().isBrightOutside() && !this.hasCustomName() && !this.level().isClientSide()) {
            float f = (float) this.level().getMaxLocalRawBrightness(BlockPos.containing(this.getX(), this.getEyeY(), this.getZ())) / 15.0F;
            return Mth.lerp(this.level().dimensionType().ambientLight(), f / (4.0F - 3.0F * f), 1.0F) > 0.5F;
        }

        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.FIREFLY_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.FIREFLY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.FIREFLY_DEATH.get();
    }

    static class FireflyHideInGrassGoal extends MoveToBlockGoal {
        private final Firefly firefly;

        public FireflyHideInGrassGoal(Firefly mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.firefly = mob;
        }

        @Override
        public boolean canUse() {
            return firefly.isSunBurnTick() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(Blocks.SHORT_GRASS) || level.getBlockState(pos).is(Blocks.FERN) || level.getBlockState(pos).is(Blocks.TALL_GRASS);
        }

        @Override
        public void tick() {
            super.tick();
            Level level = firefly.level();
            if (this.isReachedTarget()) {
                if (!level.isClientSide()) {
                    ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SHORT_GRASS.defaultBlockState()), firefly.getX(), firefly.getY(), firefly.getZ(), 50, firefly.getBbWidth() / 4.0F, firefly.getBbHeight() / 4.0F, firefly.getBbWidth() / 4.0F, 0.05D);
                }
                level.playSound(null, firefly.blockPosition(), NaturalistSoundEvents.FIREFLY_HIDE.get(), SoundSource.NEUTRAL, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
                firefly.discard();
            }
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            return this.blockPos;
        }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        this.flyAnimationState.animateWhen(!this.isBaby(), this.tickCount);
        boolean moving = this.isBaby() && NaturalistAnimal.isVisiblyMoving(this);
        this.walkAnimationState.animateWhen(moving, this.tickCount);
        this.idleAnimationState.animateWhen(this.isBaby() && !moving, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
