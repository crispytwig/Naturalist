package com.crispytwig.naturalist.world.entity.animal.butterfly;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.block.ChrysalisBlock;
import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.animal.ClimbingAnimal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

@SuppressWarnings("unused")
public class Caterpillar extends ClimbingAnimal implements Catchable, DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Caterpillar.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Caterpillar.class, EntityDataSerializers.BOOLEAN);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState crawlAnimationState = new SmoothAnimationState();

    public Caterpillar(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(FROM_HAND, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/caterpillar.png");
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

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.hasCustomName();
    }

    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromHand", this.fromHand());
    }

    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromHand(input.getBooleanOr("FromHand", false));
    }

    public void saveToHandTag(ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(compoundTag);
        compoundTag.putInt("Age", this.getAge());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    public void loadFromHandTag(CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);

        tag.getInt("Age").ifPresent(this::setAge);
    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.CATERPILLAR.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        this.setAge(0);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return this.isBaby() && stack.is(BlockItemTags.FLOWERS.item());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CocoonGoal(this, 1.0F, 5, 2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public float getAgeScale() {
        return 1.0f;
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

    private static class CocoonGoal extends MoveToBlockGoal {
        private final Caterpillar caterpillar;
        private @NotNull Direction facing = Direction.NORTH;
        private BlockPos logPos = BlockPos.ZERO;

        public CocoonGoal(Caterpillar mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.caterpillar = mob;
        }

        @Override
        public boolean canUse() {
            return !caterpillar.isBaby() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            if (level.getBlockState(pos).isAir()) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (level.getBlockState(pos.relative(direction)).is(BlockTags.LOGS) && level.getBlockState(pos.relative(direction).below()).is(BlockTags.LOGS)) {
                        this.facing = direction;
                        this.logPos = pos.relative(direction);
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public void tick() {
            BlockPos targetPos = this.getMoveToTarget();
            if (!targetPos.closerToCenterThan(caterpillar.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    caterpillar.getNavigation().moveTo(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, this.speedModifier);
                }
            } else {
                --this.tryTicks;
            }
            caterpillar.getLookControl().setLookAt(logPos.getX() + 0.5D, logPos.getY() + 1, logPos.getZ() + 0.5D, 10.0F, this.caterpillar.getMaxHeadXRot());
            Level level = caterpillar.level();
            if (this.isValidTarget(level, caterpillar.blockPosition())) {
                if (!level.isClientSide()) {
                    ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, NaturalistRegistry.CHRYSALIS_BLOCK.get().defaultBlockState()), caterpillar.getX(), caterpillar.getY(), caterpillar.getZ(), 50, caterpillar.getBbWidth() / 4.0F, caterpillar.getBbHeight() / 4.0F, caterpillar.getBbWidth() / 4.0F, 0.05D);
                }
                caterpillar.discard();
                level.setBlockAndUpdate(caterpillar.blockPosition(), NaturalistRegistry.CHRYSALIS_BLOCK.get().defaultBlockState().setValue(ChrysalisBlock.FACING, facing));
                level.playSound(null, caterpillar.blockPosition(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            }
        }

        @Override
        protected void moveMobToBlock() {
            caterpillar.getNavigation().moveTo(logPos.getX() + 0.5D, logPos.getY() + 1.0D, logPos.getZ() + 0.5D, this.speedModifier);
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            return logPos.above();
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
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        this.crawlAnimationState.animateWhen(moving, this.tickCount);
        this.idleAnimationState.animateWhen(!moving, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
