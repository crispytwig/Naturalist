package com.crispytwig.naturalist.world.entity.animal.hippo;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.DistancedFollowParentGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SmoothFloatGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import net.minecraft.nbt.CompoundTag;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unused")
public class Hippo extends TamableAnimal implements FollowingPet, DataDrivenVariantAnimal {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Blocks.MELON.asItem());

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Hippo.class, EntityDataSerializers.STRING);

    private boolean followingOwner = true;
    private int eatingTicks;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimIdleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState biteAnimationState = SmoothAnimationState.instant();
    private static final int BITE_ANIM_TICKS = 20;
    private int biteAnimTicks;

    public Hippo(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/hippo/hippo.png");
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
    public boolean isFollowingOwner() {
        return this.followingOwner;
    }

    @Override
    public void setFollowingOwner(boolean following) {
        this.followingOwner = following;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        FollowingPet.savePet(this, output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    public static boolean checkHippoSpawnRules(EntityType<? extends Animal> entityType, ServerLevelAccessor levelAccessor, EntitySpawnReason mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        if (levelAccessor.getBlockState(blockPos.below()).is(BlockTags.DIRT) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos)) {
            for (int x = -16; x <= 16; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -16; z <= 16; z++) {
                        mutableBlockPos.setWithOffset(blockPos, x, y, z);
                        if (!levelAccessor.hasChunk(SectionPos.blockToSectionCoord(mutableBlockPos.getX()), SectionPos.blockToSectionCoord(mutableBlockPos.getZ()))) {
                            continue;
                        }
                        if (levelAccessor.getFluidState(mutableBlockPos).is(FluidTags.WATER)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return this.isInWater() && otherAnimal.isInWater() && super.canMate(otherAnimal);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Hippo baby = NaturalistEntityTypes.HIPPO.get().create(serverLevel, EntitySpawnReason.BREEDING);
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
        this.goalSelector.addGoal(0, new SmoothFloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new HippoAttackBoatsGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(5, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new PetFollowOwnerGoal(this, 1.25D, 10.0F, 5.0F));
        this.goalSelector.addGoal(6, new DistancedFollowParentGoal(this, 1.25D, 8.0D, 2.0D, 5.0D));
        this.goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity, level) -> !this.isBaby() && entity.isInWater() && !this.isOwnedBy(entity)));
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(NaturalistAnimal.babyKnockbackStrength(this, strength), x, z);
    }

    @Override
    public double getFluidJumpThreshold() {
        if (this.isBaby()) {
            return 0.2F;
        } else {
            return 1.0F;
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return PetTargeting.protectsOwnedPet(this, target) && super.canAttack(target);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (this.isFood(itemStack)) {
            if (!this.isTame() && this.isBaby()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                return InteractionResult.SUCCESS;
            }
            if (this.isTame() && this.getHealth() < this.getMaxHealth()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.heal(4.0F);
                }
                return InteractionResult.SUCCESS;
            }
            int age = this.getAge();
            if (age == 0 && this.canFallInLove()) {
                if (!this.level().isClientSide()) {
                    this.eatingTicks = 10;
                    this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.copy());
                    this.swing(InteractionHand.MAIN_HAND);
                    float yRot = (this.getYRot() + 90) * Mth.DEG_TO_RAD;
                    ((ServerLevel)level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MELON.defaultBlockState()), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
                    ((ServerLevel)level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, Items.MELON_SLICE), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
                    this.playSound(SoundEvents.HORSE_EAT);
                    this.playSound(SoundEvents.WOOD_BREAK);
                    this.usePlayerItem(player, hand, itemStack);
                    this.setInLove(player);
                }
                return InteractionResult.SUCCESS;
            }
            if (this.isBaby()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.ageUp(Animal.getSpeedUpSecondsWhenFeeding(-age), true);
                }
                return InteractionResult.SUCCESS;
            }
        }
        if (this.isTame() && this.isOwnedBy(player) && itemStack.isEmpty() && player.isSecondaryUseActive()) {
            if (!this.level().isClientSide()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.0D);
        } else {
            this.setSprinting(false);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (this.eatingTicks > 0) {
                this.eatingTicks--;
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.HIPPO_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.HIPPO_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.HIPPO_DEATH.get();
    }

    static class HippoAttackBoatsGoal extends Goal {
        protected final PathfinderMob mob;
        private final double speedModifier;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;
        private Boat target;

        public HippoAttackBoatsGoal(PathfinderMob pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isBaby()) {
                return false;
            }
            long gameTime = this.mob.level().getGameTime();
            if (gameTime - this.lastCanUseCheck < 20L) {
                return false;
            }
            this.lastCanUseCheck = gameTime;
            List<Boat> entities = this.mob.level().getEntitiesOfClass(Boat.class, this.mob.getBoundingBox().inflate(8, 4, 8));
            if (!entities.isEmpty()) {
                this.target = entities.getFirst();
            }
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            this.path = this.mob.getNavigation().createPath(this.target, 0);
            if (this.path != null) {
                return true;
            }
            return this.getAttackReachSqr(this.target) >= this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        }

        @Override
        public boolean canContinueToUse() {
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            return !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.mob.getTarget())) {
                this.mob.setTarget(null);
            }
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                return;
            }
            this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
            double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.mob.getSensing().hasLineOfSight(this.target)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || this.target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.pathedTargetX = this.target.getX();
                this.pathedTargetY = this.target.getY();
                this.pathedTargetZ = this.target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.getNavigation().moveTo(this.target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(this.target, d);
        }

        protected void checkAndPerformAttack(Entity enemy, double distToEnemySqr) {
            if (distToEnemySqr <= this.getAttackReachSqr(enemy) && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(getServerLevel(this.mob), enemy);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(Entity attackTarget) {
            return Mth.square(this.mob.getBbWidth() * 1.2f) + attackTarget.getBbWidth();
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
        boolean posing = this.isInSittingPose();
        boolean baby = this.isBaby();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean inWater = this.isInWater();

        this.sitAnimationState.animateWhen(posing && baby, this.tickCount);
        this.sleepAnimationState.animateWhen(posing && !baby, this.tickCount);

        if (this.swinging && this.biteAnimTicks <= 0) {
            this.biteAnimTicks = BITE_ANIM_TICKS;
        } else if (this.biteAnimTicks > 0) {
            this.biteAnimTicks--;
        }
        this.biteAnimationState.animateWhen(this.biteAnimTicks > 0, this.tickCount);

        this.swimAnimationState.animateWhen(!posing && moving && inWater, this.tickCount);
        this.runAnimationState.animateWhen(!posing && moving && !inWater && this.isSprinting(), this.tickCount);
        this.walkAnimationState.animateWhen(!posing && moving && !inWater && !this.isSprinting(), this.tickCount);
        this.swimIdleAnimationState.animateWhen(!posing && !moving && inWater, this.tickCount);
        this.idleAnimationState.animateWhen(!posing && !moving && !inWater, this.tickCount);
    }
    //endregion
}
