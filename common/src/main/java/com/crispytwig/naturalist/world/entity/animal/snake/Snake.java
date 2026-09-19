package com.crispytwig.naturalist.world.entity.animal.snake;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.HuntingAnimal;
import com.crispytwig.naturalist.world.entity.SleepingAnimal;
import com.crispytwig.naturalist.world.entity.animal.TamableClimbingAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SearchForItemsGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Snake extends TamableClimbingAnimal implements SleepingAnimal, NeutralMob, FollowingPet, HuntingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.SNAKE_TEMPT_ITEMS);
    private static final Predicate<ItemStack> TAME_ITEMS = stack -> stack.is(NaturalistItemTags.SNAKE_TAME_ITEMS);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.INT);

    private boolean followingOwner = true;
    private int huntingCooldown;
    @Nullable
    private EntityReference<LivingEntity> persistentAngerTarget;

    private int tongueTicks;

    public final SmoothAnimationState moveAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState climbAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();
    private final AnimationTimer attackAnimTimer = new AnimationTimer(10);
    public final SmoothAnimationState tongueAnimationState = SmoothAnimationState.instant();
    public final SmoothAnimationState rattleAnimationState = new SmoothAnimationState();

    public Snake(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.18D).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(SLEEPING, false);
        builder.define(EAT_COUNTER, 0);
        builder.define(DATA_ANGER_END_TIME, -1L);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/snake/green_snake.png");
    }

    public boolean isVenomous() {
        return this.isRattlesnake() || this.getVariantLocation().getPath().equals("coral_snake");
    }

    public boolean isRattlesnake() {
        return this.getVariantLocation().getPath().equals("rattlesnake");
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

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void setEating(boolean eating) {
        this.entityData.set(EAT_COUNTER, eating ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.entityData.set(EAT_COUNTER, amount);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.entityData.set(DATA_ANGER_END_TIME, endTime);
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.entityData.get(DATA_ANGER_END_TIME);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        this.addPersistentAngerSaveData(output);
        FollowingPet.savePet(this, output);
        this.saveHuntingCooldown(output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.readPersistentAngerSaveData(this.level(), input);
        FollowingPet.loadPet(this, input);
        this.loadHuntingCooldown(input);
    }
    //endregion

    //region Spawning
    public static boolean checkSnakeSpawnRules(EntityType<Snake> entityType, LevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.DIRT) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        this.populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource random, @NotNull DifficultyInstance difficulty) {
        if (random.nextFloat() < 0.2F) {
            float chance = random.nextFloat();
            ItemStack stack;
            if (chance < 0.05F) {
                stack = new ItemStack(Items.RABBIT_FOOT);
            } else if (chance < 0.1F) {
                stack = new ItemStack(Items.SLIME_BALL);
            } else if (chance < 0.15F) {
                stack = new ItemStack(Items.FEATHER);
            } else if (chance < 0.3F) {
                stack = new ItemStack(Items.RABBIT);
            } else {
                stack = new ItemStack(Items.CHICKEN);
            }

            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    public boolean isTameFood(ItemStack stack) {
        return TAME_ITEMS.test(stack);
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
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new SnakeMeleeAttackGoal(this, 1.75D, true));
        this.goalSelector.addGoal(2, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8.0D, 8.0D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new PetFollowOwnerGoal(this, 1.2D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false, (livingEntity, level) -> this.canHunt() && (livingEntity.is(NaturalistEntityTypeTags.SNAKE_HOSTILES) || (livingEntity instanceof Slime slime && slime.isTiny()))));
        this.targetSelector.addGoal(6, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public float getSpeed() {
        return this.getMainHandItem().isEmpty() ? super.getSpeed() : super.getSpeed() * 0.5F;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (!this.getMainHandItem().isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, this.getMainHandItem());
            itemEntity.setPickUpDelay(80);
            itemEntity.setThrower(this);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.level().addFreshEntity(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity entity) {
        if (this.isVenomous() && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 40));
        }
        return super.doHurtTarget(level, entity);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return PetTargeting.protectsOwnedPet(this, target) && super.canAttack(target);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
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
        if (this.level().isClientSide()) {
            return (this.isOwnedBy(player) || this.isTame() || (this.isTameFood(stack) && !this.isTame())) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isTameFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(4.0F);
                return InteractionResult.SUCCESS;
            }
            if (this.isOwnedBy(player) && !this.isTameFood(stack)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(player, hand);
        }
        if (this.isTameFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (this.random.nextInt(2) == 0) {
                this.tame(player);
                this.setOrderedToSit(true);
                this.navigation.stop();
                this.setTarget(null);
                this.stopBeingAngry();
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(@NotNull EquipmentSlot slot) {
        if (!this.getItemBySlot(slot).isEmpty()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canDispenserEquipIntoSlot(slot);
        }
    }

    @Override
    protected void pickUpItem(@NotNull ServerLevel level, @NotNull ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        if (this.getMainHandItem().isEmpty() && FOOD_ITEMS.test(stack)) {
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            this.setDropChance(EquipmentSlot.MAINHAND, 2.0F);
            this.take(itemEntity, stack.getCount());
            itemEntity.discard();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel serverLevel) {
            this.updatePersistentAnger(serverLevel, true);
            this.tickHuntingCooldown();
        }
        if (this.isSleeping() || this.isImmobile() || this.isInSittingPose()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandItem().isEmpty()) {
            if (this.isAngry()) {
                this.stopBeingAngry();
            }
        }
        if (this.canRattle() && !this.isSleeping()) {
            this.playSound(NaturalistSoundEvents.SNAKE_RATTLE.get(), 0.15F, 1.0F);
        }
    }

    private void handleEating() {
        if (!this.isEating() && !this.isSleeping() && !this.getMainHandItem().isEmpty()) {
            this.setEating(true);
        } else if (this.getMainHandItem().isEmpty()) {
            this.setEating(false);
        }
        if (this.isEating()) {
            if (!this.level().isClientSide() && this.getEatCounter() > 6000) {
                if (!this.getMainHandItem().isEmpty()) {
                    if (!this.level().isClientSide()) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);
                    }
                }
                this.setEating(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDefaultClockTime();
        if (this.isAngry() || this.level().isWaterAt(this.blockPosition())) {
            return false;
        } else if (dayTime > 18000 && dayTime < 23000) {
            return false;
        } else return dayTime > 12000 && dayTime < 28000;
    }

    private boolean canRattle() {
        boolean rattlesnake = this.isRattlesnake();
        Player player = NaturalistAnimal.findVisiblePlayer(this, 4.0D, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D), candidate -> true);
        if (player != null && rattlesnake && !player.isCreative()) {
            this.setTarget(player);
        } else {
            this.setTarget(null);
        }
        return player != null && rattlesnake;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.SNAKE_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.SNAKE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAKE_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.15F;
    }

    static class SnakeMeleeAttackGoal extends MeleeAttackGoal {
        private long lastCanUseCheck;

        public SnakeMeleeAttackGoal(@NotNull PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && testUse();
        }

        boolean testUse(){
            long l = this.mob.level().getGameTime();
            if (l - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = l;
                LivingEntity livingEntity = this.mob.getTarget();
                if (livingEntity == null) {
                    return false;
                } else if (!livingEntity.isAlive()) {
                    return false;
                } else {
                    if (this.mob.getNavigation().createPath(livingEntity, 0) != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingEntity) >= this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
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
        boolean sleeping = this.isSleeping() || this.isInSittingPose();
        boolean climbing = this.isClimbing();
        if (this.tongueTicks > 0) {
            this.tongueTicks--;
        } else if (!this.isSleeping() && this.random.nextInt(1000) < this.ambientSoundTime) {
            this.tongueTicks = 15;
        }
        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);
        this.tongueAnimationState.animateWhen(this.tongueTicks > 0, this.tickCount);
        this.rattleAnimationState.animateWhen(this.canRattle() && !this.isSleeping(), this.tickCount);
        this.sleepAnimationState.animateWhen(sleeping, this.tickCount);
        this.climbAnimationState.animateWhen(!sleeping && climbing, this.tickCount);
        this.moveAnimationState.animateWhen(!sleeping && !climbing && this.walkAnimation.speed() > 0.04F, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
