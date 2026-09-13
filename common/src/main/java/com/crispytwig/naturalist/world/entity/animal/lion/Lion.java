package com.crispytwig.naturalist.world.entity.animal.lion;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.HuntingAnimal;
import com.crispytwig.naturalist.world.entity.NocturnalHostile;
import com.crispytwig.naturalist.world.entity.SleepingAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Lion extends TamableAnimal implements SleepingAnimal, FollowingPet, HuntingAnimal, NocturnalHostile, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.LION_FOOD_ITEMS);
    private static final Identifier BABY_SPEED_BOOST_ID = Naturalist.location("baby_speed_boost");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, 0.05D, AttributeModifier.Operation.ADD_VALUE);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Lion.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Lion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_MANE = SynchedEntityData.defineId(Lion.class, EntityDataSerializers.BOOLEAN);

    private boolean followingOwner = true;
    private int huntingCooldown;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState preyAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sleep2AnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();
    private final AnimationTimer attackAnimTimer = new AnimationTimer(5);

    public Lion(@NotNull EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(SLEEPING, false);
        builder.define(HAS_MANE, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/lion/lion.png");
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
    public int getHuntingCooldown() {
        return this.huntingCooldown;
    }

    @Override
    public void setHuntingCooldown(int ticks) {
        this.huntingCooldown = ticks;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setHasMane(boolean hasMane) {
        this.entityData.set(HAS_MANE, hasMane);
    }

    public boolean hasMane() {
        return this.entityData.get(HAS_MANE);
    }

    public boolean usesAltSleepPose() {
        return (this.getUUID().hashCode() & 1) == 0;
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("Mane", this.hasMane());
        FollowingPet.savePet(this, output);
        this.saveHuntingCooldown(output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setHasMane(input.getBooleanOr("Mane", false));
        FollowingPet.loadPet(this, input);
        this.loadHuntingCooldown(input);
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        super.finalizeSpawn(level, difficulty, reason, spawnData);
        AgeableMobGroupData ageableMobGroupData;
        boolean prideLeader = spawnData == null;
        if (prideLeader) {
            spawnData = new AgeableMobGroupData(true);
        }
        this.setHasMane(!prideLeader && this.getRandom().nextBoolean());
        if ((ageableMobGroupData = (AgeableMobGroupData)spawnData).getGroupSize() > 2 && this.getRandom().nextFloat() < 0.7F) {
            this.setAge(-24000);
        }
        ageableMobGroupData.increaseGroupSizeByOne();
        RandomSource random = level.getRandom();
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier(Naturalist.location("random_spawn_bonus"), random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return spawnData;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Lion baby = NaturalistEntityTypes.LION.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.setHasMane(this.getRandom().nextBoolean());
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LionFollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new PetFollowOwnerGoal(this, 1.3D, 7.0F, 2.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LionFollowLeaderGoal(this, 1.1D, 8.0F, 24.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                (entity, level) -> entity.is(NaturalistEntityTypeTags.LION_HOSTILES) && !entity.isBaby()
                        && !this.isSleeping() && !this.isBaby() && this.isNightTime() && this.canHunt()));
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return PetTargeting.protectsOwnedPet(this, target) && super.canAttack(target);
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
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (this.isTame() && this.isBaby() && this.isFood(stack)) {
            this.ageUp(getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.level().isClientSide()) {
            boolean canInteract = this.isOwnedBy(player) || this.isTame()
                    || (this.isBaby() && this.isFood(stack) && !this.isTame());
            return canInteract ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(stack.has(DataComponents.FOOD)
                        ? Objects.requireNonNull(stack.get(DataComponents.FOOD)).nutrition()
                        : 4.0F);
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
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        this.tickHuntingCooldown();
        this.updateBabySpeed();
        if (this.getMoveControl().hasWanted()) {
            double speedModifier = this.getMoveControl().getSpeedModifier();

            if (speedModifier >= 1.05D && this.onGround()) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }

    private void updateBabySpeed() {
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        boolean hasBoost = speed.hasModifier(BABY_SPEED_BOOST_ID);
        if (this.isBaby()) {
            if (!hasBoost) {
                speed.addTransientModifier(BABY_SPEED_BOOST);
            }
        } else if (hasBoost) {
            speed.removeModifier(BABY_SPEED_BOOST_ID);
        }
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDefaultClockTime();
        if (this.isTame() || this.getTarget() != null || this.level().isWaterAt(this.blockPosition())) {
            return false;
        } else {
            return dayTime > 6000 && dayTime < 13000;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.LION_AMBIENT_BABY.get() : NaturalistSoundEvents.LION_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.LION_HURT_BABY.get() : NaturalistSoundEvents.LION_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.LION_DEATH_BABY.get() : NaturalistSoundEvents.LION_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }

    static class LionFollowLeaderGoal extends Goal {
        private final Lion mob;
        @Nullable
        private Lion followingMob;
        private final double speedModifier;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private float oldWaterCost;
        private final float areaSize;

        public LionFollowLeaderGoal(Lion mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isBaby()) {
                return false;
            }
            List<Lion> nearbyLions = this.mob.level().getEntitiesOfClass(Lion.class, this.mob.getBoundingBox().inflate(this.areaSize), lion -> lion != this.mob && !lion.isBaby() && !lion.isInvisible());
            Lion leader = null;
            for (Lion lion : nearbyLions) {
                if (lion.hasMane()) continue;
                if (leader == null || lion.getUUID().compareTo(leader.getUUID()) < 0) {
                    leader = lion;
                }
            }
            if (leader == null) {
                return false;
            }
            if (!this.mob.hasMane() && this.mob.getUUID().compareTo(leader.getUUID()) < 0) {
                return false;
            }
            this.followingMob = leader;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
            this.mob.setPathfindingMalus(PathType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            double f;
            double e;
            if (this.followingMob == null || this.mob.isLeashed()) {
                return;
            }
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0f, this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath > 0) {
                return;
            }
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            double d = this.mob.getX() - this.followingMob.getX();
            double g = d * d + (e = this.mob.getY() - this.followingMob.getY()) * e + (f = this.mob.getZ() - this.followingMob.getZ()) * f;
            if (g <= (double)(this.stopDistance * this.stopDistance)) {
                this.navigation.stop();
                LookControl lookControl = this.followingMob.getLookControl();
                if (g <= (double)this.stopDistance || lookControl.getWantedX() == this.mob.getX() && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ()) {
                    double h = this.followingMob.getX() - this.mob.getX();
                    double i = this.followingMob.getZ() - this.mob.getZ();
                    this.navigation.moveTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speedModifier);
                }
                return;
            }
            this.navigation.moveTo(this.followingMob, this.speedModifier);
        }
    }

    static class LionFollowParentGoal extends FollowParentGoal {
        private final Lion lion;

        public LionFollowParentGoal(Lion animal, double speedModifier) {
            super(animal, speedModifier);
            this.lion = animal;
        }

        @Override
        public boolean canUse() {
            return !this.lion.isSleeping() && !this.lion.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.lion.isSleeping() && !this.lion.isTame() && super.canContinueToUse();
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
        boolean baby = this.isBaby();
        boolean sitting = baby && this.isInSittingPose();
        boolean sleeping = this.isSleeping() || (!baby && this.isInSittingPose());
        boolean altSleep = this.usesAltSleepPose();
        boolean posing = sitting || sleeping;
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);

        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.sleepAnimationState.animateWhen(sleeping && !altSleep, this.tickCount);
        this.sleep2AnimationState.animateWhen(sleeping && altSleep, this.tickCount);

        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);

        this.runAnimationState.animateWhen(!posing && moving && this.isSprinting(), this.tickCount);
        this.preyAnimationState.animateWhen(!posing && moving && !this.isSprinting() && this.isCrouching(), this.tickCount);
        this.walkAnimationState.animateWhen(!posing && moving && !this.isSprinting() && !this.isCrouching(), this.tickCount);
        this.idleAnimationState.animateWhen(!posing && !moving, this.tickCount);
    }
    //endregion
}
