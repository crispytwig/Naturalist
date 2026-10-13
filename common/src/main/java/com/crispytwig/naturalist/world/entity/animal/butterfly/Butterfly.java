package com.crispytwig.naturalist.world.entity.animal.butterfly;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.FlyingWanderGoal;
import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import com.crispytwig.naturalist.world.entity.variant.MobVariantUtil;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class Butterfly extends NaturalistAnimal implements Catchable, DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"monarch", "clouded_yellow", "swallowtail", "blue_morpho", "jade_green_swallowtail", "purple_emperor", "red_admiral"};

    private static final EntityDataAccessor<Boolean> HAS_NECTAR = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);

    private int numCropsGrownSincePollination;

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();

    public Butterfly(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, NaturalistMobVariants.BUTTERFLY_MONARCH.identifier().toString());
        builder.define(FROM_HAND, false);
        builder.define(HAS_NECTAR, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.BUTTERFLY_MONARCH;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/butterfly/monarch.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public boolean hasNectar() {
        return this.entityData.get(HAS_NECTAR);
    }

    void setHasNectar(boolean hasNectar) {
        this.entityData.set(HAS_NECTAR, hasNectar);
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.hasCustomName();
    }

    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromHand", this.fromHand());
    }

    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromHand(input.getBooleanOr("FromHand", false));
    }

    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(compoundTag);
        compoundTag.putInt("Age", this.getAge());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);

        if (tag.contains("Age")) {
            this.setAge(tag.getIntOr("Age", 0));
        }

        if (tag.contains("HuntingCooldown")) {
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, tag.getLongOr("HuntingCooldown", 0L));
        }

    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.BUTTERFLY.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }
    //endregion

    //region Spawning
    public static boolean checkButterflySpawnRules(EntityType<? extends Butterfly> type, ServerLevelAccessor level, EntitySpawnReason reason, @NotNull BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistBlockTags.BUTTERFLIES_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (reason == EntitySpawnReason.BUCKET) {
            return super.finalizeSpawn(level, difficulty, reason, spawnData);
        } else {
            if (!(spawnData instanceof Butterfly.ButterflyGroupData)) {
                spawnData = new Butterfly.ButterflyGroupData(this.selectSpawnVariantId(level), this.selectSpawnVariantId(level));
            }

            this.setVariantString(((Butterfly.ButterflyGroupData)spawnData).getVariant(level.getRandom()));

            return super.finalizeSpawn(level, difficulty, reason, spawnData);
        }
    }

    private String selectSpawnVariantId(ServerLevelAccessor level) {
        return MobVariantUtil.selectVariantForSpawn(level, this.blockPosition(), NaturalistMobVariants.BUTTERFLY_VARIANT)
                .flatMap(holder -> holder.unwrapKey().map(key -> key.identifier().toString()))
                .orElseGet(() -> this.getDefaultVariant().identifier().toString());
    }

    public static class ButterflyGroupData extends AgeableMob.AgeableMobGroupData {
        public final String[] types;

        public ButterflyGroupData(String... variantIds) {
            super(false);
            this.types = variantIds;
        }

        public String getVariant(RandomSource random) {
            return this.types[random.nextInt(this.types.length)];
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(BlockItemTags.FLOWERS.item());
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return NaturalistEntityTypes.CATERPILLAR.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    @Override
    protected boolean canBeABaby() {
        return false;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, stack -> stack.is(BlockItemTags.FLOWERS.item()), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new ButterflyGrowCropGoal(this, 1.0D, 16, 4));
        this.goalSelector.addGoal(5, new ButterflyPollinateGoal(this, 1.0D, 16, 4));
        this.goalSelector.addGoal(6, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return !level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.getNodeEvaluator().setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return Catchable.catchAnimal(player, hand, this, true).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getCropsGrownSincePollination() >= 10) {
            this.resetNumCropsGrownSincePollination();
            this.setHasNectar(false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getY(0.5D));
            }
        }
    }

    private void spawnFluidParticle(@NotNull Level level, double x1, double x2, double z1, double z2, double y) {
        level.addParticle(ParticleTypes.FALLING_NECTAR, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.BUTTERFLY_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.BUTTERFLY_HURT.get();
    }

    static class ButterflyPollinateGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        private final Butterfly butterfly;

        public ButterflyPollinateGoal(@NotNull Butterfly mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.butterfly = mob;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(BlockTags.FLOWERS);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
        }

        protected void onReachedTarget() {
            BlockState state = butterfly.level().getBlockState(blockPos);
            if (state.is(BlockTags.FLOWERS)) {
                butterfly.setHasNectar(true);
                this.stop();
            }
        }

        @Override
        public boolean canUse() {
            return !butterfly.hasNectar() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !butterfly.hasNectar() && super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.ticksWaited = 0;
        }
    }

    static class ButterflyGrowCropGoal extends MoveToBlockGoal {
        private final Butterfly butterfly;

        public ButterflyGrowCropGoal(Butterfly mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.butterfly = mob;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.getBlock() instanceof CropBlock cropBlock && cropBlock.getAge(state) < cropBlock.getMaxAge();
        }

        public void tick() {
            BlockPos blockpos = this.getMoveToTarget();
            if (!blockpos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    this.mob.getNavigation().moveTo((double)((float)blockpos.getX()) + 0.5D, blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speedModifier);
                }
            } else {
                this.onReachedTarget();
            }

        }

        protected void onReachedTarget() {
            BlockState state = butterfly.level().getBlockState(blockPos);
            if (state.getBlock() instanceof CropBlock cropBlock) {
                cropBlock.growCrops(butterfly.level(), blockPos, state);
                butterfly.incrementNumCropsGrownSincePollination();
                this.stop();
            }
        }

        @Override
        public boolean canUse() {
            return butterfly.hasNectar() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return butterfly.hasNectar() && super.canContinueToUse();
        }
    }
    //endregion

    //region Animation
    private void setupAnimationStates() {
        this.flyAnimationState.animateWhen(true, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
