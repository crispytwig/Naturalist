package com.crispytwig.naturalist.world.entity.animal.alligator;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.EggLayingAnimal;
import com.crispytwig.naturalist.world.entity.HuntingAnimal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabySniffFlowersGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.CloseMeleeAttackGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.EggLayingBreedGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.LayEggGoal;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import com.crispytwig.naturalist.world.level.block.AlligatorEggBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import com.crispytwig.naturalist.world.entity.BodyChain;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Alligator extends NaturalistAnimal implements EggLayingAnimal, HuntingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.ALLIGATOR_FOOD_ITEMS);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);

    private static final float MAX_SWIM_TILT = 35.0F;
    private static final float ROLL_PER_YAW = 4.0F;
    private static final float MAX_ROLL = 25.0F;

    int layEggCounter;
    private int huntingCooldown;

    private float xBodyRot;
    private float xBodyRotO;
    private float zBodyRot;
    private float zBodyRotO;
    private final BodyChain chain = new BodyChain(1.0F,
            new float[]{0.35F, 0.16F, 0.12F},
            new float[]{0.24F, 0.12F, 0.1F},
            0.0F, 0.0F, 1.0F);
    private final BodyChain babyChain = new BodyChain(1.0F,
            new float[]{0.55F, 0.28F, 0.22F},
            new float[]{0.4F, 0.22F, 0.18F},
            0.0F, 0.0F, 1.0F);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState biteAnimationState = SmoothAnimationState.instant();

    public Alligator(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.4F, 1.0F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0f);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.60)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/alligator/alligator.png");
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
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public Block getEggBlock() {
        return NaturalistRegistry.ALLIGATOR_EGG.get();
    }

    @Override
    public BlockState createEggBlockState(int eggCount) {
        return this.getEggBlock().defaultBlockState().setValue(AlligatorEggBlock.EGGS, eggCount);
    }

    @Override
    public @NotNull TagKey<Block> getEggLayableBlockTag() {
        return NaturalistBlockTags.ALLIGATOR_EGG_LAYABLE_ON;
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.layEggCounter = isLayingEgg ? 1 : 0;
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

    public boolean isDefensive() {
        return this.hasEgg() || this.isLayingEgg();
    }

    @Override
    public int getHuntingCooldown() {
        return this.huntingCooldown;
    }

    @Override
    public void setHuntingCooldown(int ticks) {
        this.huntingCooldown = ticks;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("HasEgg", this.hasEgg());
        this.saveHuntingCooldown(output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setHasEgg(input.getBooleanOr("HasEgg", false));
        this.loadHuntingCooldown(input);
    }
    //endregion

    //region Spawning
    public static boolean checkAlligatorSpawnRules(EntityType<? extends Alligator> type, ServerLevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.SUBSTRATE_OVERWORLD) && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Alligator baby = NaturalistEntityTypes.ALLIGATOR.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
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
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new CloseMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2D));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new BabySniffFlowersGoal(this, 1.0D, 16, 4, SoundEvents.FOX_SNIFF));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity, serverLevel) -> !this.isBaby() && (entity.isInWater() || this.isDefensive() || !this.level().isBrightOutside())));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity, serverLevel) -> {
            if(entity instanceof Alligator) return false;
            boolean isEntityNearAlligatorEggs = false;
            for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().offset(-2, -2, -2), entity.blockPosition().offset(2, 2, 2))) {
                if (level().getBlockState(pos).is(NaturalistRegistry.ALLIGATOR_EGG.get())) {
                    isEntityNearAlligatorEggs = true;
                    break;
                }
            }
            return !this.isBaby() && isEntityNearAlligatorEggs;
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity, serverLevel) -> !this.isBaby() && this.canHunt() && entity.is(NaturalistEntityTypeTags.ALLIGATOR_HOSTILES)));
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.0025, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return this.isInWater() && level.getFluidState(pos).is(FluidTags.WATER) ? 10.0F : super.getWalkTargetValue(pos, level);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(NaturalistAnimal.babyKnockbackStrength(this, strength), x, z);
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed, @NotNull DamageSource source) {
        boolean result = super.killedEntity(level, killed, source);
        if (result) {
            this.startHuntingCooldown();
        }
        return result;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.tickHuntingCooldown();
            NaturalistAnimal.leaveWater(this);
        }

        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0 && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_HURT_BABY.get() : NaturalistSoundEvents.GATOR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_DEATH_BABY.get() : NaturalistSoundEvents.GATOR_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.tickClientVisuals();
            this.setupAnimationStates();
        }
    }

    private void tickClientVisuals() {
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;

        Vec3 movement = new Vec3(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        boolean grounded = this.onGround() || !this.isInWater();
        float targetPitch = 0.0F;
        if (!grounded && movement.horizontalDistanceSqr() > 1.0E-7D) {
            targetPitch = Mth.clamp(-((float) (Mth.atan2(movement.y, movement.horizontalDistance()) * Mth.RAD_TO_DEG)), -MAX_SWIM_TILT, MAX_SWIM_TILT);
        }
        this.xBodyRot += (targetPitch - this.xBodyRot) * (grounded ? 0.25F : 0.07F);

        float yawStep = Mth.degreesDifference(this.yBodyRotO, this.yBodyRot);
        float targetRoll = this.isInWater() ? Mth.clamp(-yawStep * ROLL_PER_YAW, -MAX_ROLL, MAX_ROLL) : 0.0F;
        this.zBodyRot += (targetRoll - this.zBodyRot) * 0.1F;

        this.activeChain().tick(this.yBodyRot, this.xBodyRot, targetPitch);
    }

    private BodyChain activeChain() {
        return this.isBaby() ? this.babyChain : this.chain;
    }

    public float getXBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.xBodyRotO, this.xBodyRot);
    }

    public float getZBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.zBodyRotO, this.zBodyRot);
    }

    public float getSegmentPitchOffset(int index, float partialTick) {
        return this.activeChain().getSegmentPitchOffset(index, partialTick, this.getXBodyRot(partialTick));
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        return this.activeChain().getSegmentYawOffset(index, partialTick);
    }

    private void setupAnimationStates() {
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean inWater = this.isInWater();
        this.biteAnimationState.animateWhen(this.swinging, this.tickCount);
        this.swimAnimationState.animateWhen(inWater, this.tickCount);
        this.walkAnimationState.animateWhen(moving && !inWater, this.tickCount);
        this.idleAnimationState.animateWhen(!moving && !inWater, this.tickCount);
    }
    //endregion
}
