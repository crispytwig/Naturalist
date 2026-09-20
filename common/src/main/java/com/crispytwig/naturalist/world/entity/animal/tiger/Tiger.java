package com.crispytwig.naturalist.world.entity.animal.tiger;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.HuntingAnimal;
import com.crispytwig.naturalist.world.entity.NocturnalHostile;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.world.entity.SleepingAnimal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.function.Predicate;
import java.util.EnumSet;
import java.util.Objects;

public class Tiger extends TamableAnimal implements SleepingAnimal, FollowingPet, HuntingAnimal, DataDrivenVariantAnimal, NocturnalHostile {
    //region Data
    public static final String[] VARIANT_NAMES = {"black_panther", "leopard", "tiger", "white_tiger"};

    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.TIGER_FOOD_ITEMS);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Tiger.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Tiger.class, EntityDataSerializers.BOOLEAN);

    private boolean followingOwner = true;
    private int huntingCooldown;
    private boolean stalking;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState preyAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sleep2AnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack WALK_SOUNDS = AnimationSoundTrack.builder(1.0F, true)
            .at(0.0F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .at(0.25F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .at(0.5F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .at(0.75F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .build();
    private static final AnimationSoundTrack RUN_SOUNDS = AnimationSoundTrack.builder(0.9167F, true)
            .at(0.2083F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .at(0.25F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .at(0.7083F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .at(0.875F, NaturalistSoundEvents.TIGER_STEP, 0.4F, 1.0F)
            .build();
    private static final AnimationSoundTrack PREY_SOUNDS = AnimationSoundTrack.builder(1.0F, true)
            .at(0.0F, NaturalistSoundEvents.TIGER_PREY, 1.0F, 1.0F)
            .at(0.2917F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .at(0.5F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .at(0.7917F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .at(0.9167F, NaturalistSoundEvents.TIGER_STEP, 0.25F, 1.0F)
            .build();
    private static final AnimationSoundTrack ATTACK_SOUNDS = AnimationSoundTrack.builder(0.25F, false)
            .at(0.0F, NaturalistSoundEvents.TIGER_ATTACK, 1.0F, 1.0F)
            .build();
    private static final AnimationSoundTrack SLEEP_SOUNDS = AnimationSoundTrack.builder(5.0F, true)
            .at(0.0F, NaturalistSoundEvents.TIGER_SLEEP, 0.7F, 1.0F)
            .build();
    private static final AnimationSoundTrack SLEEP2_SOUNDS = AnimationSoundTrack.builder(2.0F, true)
            .at(0.0F, NaturalistSoundEvents.TIGER_SLEEP, 0.7F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.walkAnimationState, WALK_SOUNDS)
            .add(this.runAnimationState, RUN_SOUNDS)
            .add(this.preyAnimationState, PREY_SOUNDS)
            .add(this.attackAnimationState, ATTACK_SOUNDS)
            .add(this.sleepAnimationState, SLEEP_SOUNDS)
            .add(this.sleep2AnimationState, SLEEP2_SOUNDS);
    private final AnimationTimer attackAnimTimer = new AnimationTimer(5);

    public Tiger(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, NaturalistMobVariants.TIGER_BLACK_PANTHER.identifier().toString());
        builder.define(SLEEPING, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.TIGER_BLACK_PANTHER;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/tiger/tiger.png");
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
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean usesAltSleepPose() {
        return (this.getUUID().hashCode() & 1) == 0;
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
    public void setHuntingCooldown(int huntingCooldown) {
        this.huntingCooldown = huntingCooldown;
    }

    public boolean isStalking() {
        return this.stalking;
    }

    public void setStalking(boolean stalking) {
        this.stalking = stalking;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        FollowingPet.savePet(this, output);
        this.saveHuntingCooldown(output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        FollowingPet.loadPet(this, input);
        this.loadHuntingCooldown(input);
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
        Tiger baby = NaturalistEntityTypes.TIGER.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(mob, this.random));
        }
        return baby;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.05F);
        }
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new TigerStalkGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TigerFollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new PetFollowOwnerGoal(this, 1.3D, 7.0F, 2.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this).setAlertOthers(Tiger.class));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                (entity, level) -> entity.is(NaturalistEntityTypeTags.TIGER_HOSTILES) && !entity.isBaby()
                        && !this.isSleeping() && !this.isBaby() && !this.isStalking() && this.isNightTime() && this.canHunt()));
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
        this.setPose(this.isStalking() ? Pose.CROUCHING : Pose.STANDING);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.05D && this.onGround());
        } else {
            this.setSprinting(false);
        }
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDefaultClockTime();
        if (this.isTame() || this.getTarget() != null || this.level().isWaterAt(this.blockPosition())) {
            return false;
        }
        return dayTime > 6000 && dayTime < 13000;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.TIGER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.TIGER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.TIGER_DEATH.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }

    static class TigerFollowParentGoal extends FollowParentGoal {
        private final Tiger tiger;

        public TigerFollowParentGoal(Tiger tiger, double speedModifier) {
            super(tiger, speedModifier);
            this.tiger = tiger;
        }

        @Override
        public boolean canUse() {
            return !this.tiger.isSleeping() && !this.tiger.isTame() && super.canUse();
        }
    }

    static class TigerStalkGoal extends Goal {
        private static final double START_DISTANCE_SQR = 144.0D;
        private static final double POUNCE_DISTANCE_SQR = 36.0D;
        private final Tiger tiger;
        private int timeToRecalcPath;

        public TigerStalkGoal(Tiger tiger) {
            this.tiger = tiger;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.tiger.isBaby() || this.tiger.isTame() || this.tiger.isSleeping()) {
                return false;
            }
            LivingEntity target = this.tiger.getTarget();
            return target != null && target.isAlive() && this.tiger.distanceToSqr(target) > START_DISTANCE_SQR;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.tiger.getTarget();
            return target != null && target.isAlive() && !this.tiger.isSleeping()
                    && this.tiger.distanceToSqr(target) > POUNCE_DISTANCE_SQR;
        }

        @Override
        public void start() {
            this.tiger.setStalking(true);
            this.timeToRecalcPath = 0;
        }

        @Override
        public void stop() {
            this.tiger.setStalking(false);
            this.tiger.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.tiger.getTarget();
            if (target == null) {
                return;
            }
            this.tiger.getLookControl().setLookAt(target, 10.0F, this.tiger.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.tiger.getNavigation().moveTo(target, 0.7D);
            }
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
        boolean sleeping = this.isInSittingPose() || this.isSleeping();
        boolean altSleep = this.usesAltSleepPose();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);

        this.sleepAnimationState.animateWhen(sleeping && !altSleep, this.tickCount);
        this.sleep2AnimationState.animateWhen(sleeping && altSleep, this.tickCount);

        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.isSwinging()), this.tickCount);

        this.runAnimationState.animateWhen(!sleeping && moving && this.isSprinting(), this.tickCount);
        this.preyAnimationState.animateWhen(!sleeping && moving && !this.isSprinting() && this.isCrouching(), this.tickCount);
        this.walkAnimationState.animateWhen(!sleeping && moving && !this.isSprinting() && !this.isCrouching(), this.tickCount);
        this.idleAnimationState.animateWhen(!sleeping && !moving, this.tickCount);
    }
    //endregion
}
