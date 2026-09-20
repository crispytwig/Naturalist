package com.crispytwig.naturalist.server.entity.mob;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.server.block.AntHillBlock;
import com.crispytwig.naturalist.server.entity.base.NaturalistAnimal;
import com.crispytwig.naturalist.server.entity.base.Catchable;
import com.crispytwig.naturalist.server.entity.base.PetTargeting;
import com.crispytwig.naturalist.server.entity.base.TamableClimbingAnimal;
import com.crispytwig.naturalist.server.entity.misc.CarriedFoodEntity;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.server.entity.util.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Ant extends TamableClimbingAnimal implements NeutralMob, Catchable, DataDrivenVariantAnimal {
    //region Data
    private static final int PERSISTENT_ANGER_TIME = 500;
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.BOOLEAN);

    private int hillCooldown;
    @Nullable
    private UUID persistentAngerTarget;

    @Nullable
    private UUID carriedFoodId;
    @Nullable
    private CarriedFoodEntity carriedFood;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();

    public Ant(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
        builder.define(FROM_HAND, false);
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/ant.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public void startHillCooldown() {
        this.hillCooldown = 400 + this.random.nextInt(800);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        compound.putInt("HillCooldown", this.hillCooldown);
        compound.putBoolean("FromHand", this.fromHand());
        if (this.carriedFoodId != null) {
            compound.putUUID("CarriedFood", this.carriedFoodId);
        }
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.hillCooldown = compound.getInt("HillCooldown");
        this.setFromHand(compound.getBoolean("FromHand"));
        this.carriedFoodId = compound.hasUUID("CarriedFood") ? compound.getUUID("CarriedFood") : null;
        this.readPersistentAngerSaveData(this.level(), compound);
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
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(tag);
        if (this.isTame() && this.getOwnerUUID() != null) {
            tag.putBoolean("Tame", true);
            tag.putUUID("Owner", this.getOwnerUUID());
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);
        if (tag.getBoolean("Tame") && tag.hasUUID("Owner")) {
            this.setOwnerUUID(tag.getUUID("Owner"));
            this.setTame(true, true);
        }
    }

    @Override
    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.ANT.get());
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
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.15D, true));
        this.goalSelector.addGoal(2, new AntStoreFoodGoal(this));
        this.goalSelector.addGoal(3, new AntGatherFoodGoal(this));
        this.goalSelector.addGoal(4, new AntEnterHillGoal(this, 1.0D, 16, 5));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F, 0.02F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null && this.isOwnedBy(target)) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        return PetTargeting.wantsToAttack(target, owner);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        Optional<InteractionResult> caught = Catchable.catchAnimal(player, hand, this, true);
        if (caught.isPresent()) {
            return caught.get();
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.SUGAR) && this.getHealth() < this.getMaxHealth()) {
            this.heal(1.0F);
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            stack.consume(1, player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.hillCooldown > 0) {
            this.hillCooldown--;
        }
    }

    public static boolean isFoodItem(@NotNull ItemStack stack) {
        return stack.has(DataComponents.FOOD);
    }

    public boolean isCarryingFood() {
        return this.getCarriedFood() != null;
    }

    @Nullable
    public CarriedFoodEntity getCarriedFood() {
        if (this.carriedFood != null && (this.carriedFood.isRemoved() || this.carriedFood.getItem().isEmpty())) {
            this.carriedFood = null;
        }
        if (this.carriedFood == null && this.carriedFoodId != null && this.level() instanceof ServerLevel server) {
            if (server.getEntity(this.carriedFoodId) instanceof CarriedFoodEntity item && item.isAlive() && !item.getItem().isEmpty()) {
                this.carriedFood = item;
            } else {
                this.carriedFoodId = null;
            }
        }
        return this.carriedFood;
    }

    public boolean isCarriedFood(@NotNull ItemEntity item) {
        return this.carriedFoodId != null && this.carriedFoodId.equals(item.getUUID());
    }

    public void onCarriedFoodTaken(@NotNull Player player) {
        this.detachCarriedFood();
        if (player.isAlive() && !this.isOwnedBy(player) && this.getTarget() == null) {
            this.setPersistentAngerTarget(player.getUUID());
            this.startPersistentAngerTimer();
            this.setTarget(player);
        }
    }

    @Nullable
    private CarriedFoodEntity detachCarriedFood() {
        CarriedFoodEntity carried = this.getCarriedFood();
        this.carriedFood = null;
        this.carriedFoodId = null;
        return carried;
    }

    @Nullable
    private ItemEntity findNearbyFood() {
        List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
                e -> e.isAlive() && !e.hasPickUpDelay() && !(e instanceof CarriedFoodEntity) && isFoodItem(e.getItem()));
        return items.stream().min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
    }

    private void pickUpOneFood(@NotNull ItemEntity source) {
        ItemStack stack = source.getItem();
        ItemStack one = stack.copyWithCount(1);
        if (stack.getCount() <= 1) {
            source.discard();
        } else {
            source.setItem(stack.copyWithCount(stack.getCount() - 1));
        }

        CarriedFoodEntity carried = new CarriedFoodEntity(this.level(), this, one);
        this.level().addFreshEntity(carried);
        this.carriedFood = carried;
        this.carriedFoodId = carried.getUUID();
        this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }

    public void consumeCarriedFood() {
        CarriedFoodEntity carried = this.detachCarriedFood();
        if (carried != null) {
            carried.discard();
        }
    }

    private void releaseCarriedFood() {
        CarriedFoodEntity carried = this.detachCarriedFood();
        if (carried != null) {
            carried.releaseToWorld();
        }
    }

    @Override
    public void remove(Entity.@NotNull RemovalReason reason) {
        if (!this.level().isClientSide && reason.shouldDestroy()) {
            this.releaseCarriedFood();
        }
        super.remove(reason);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.updatePersistentAnger((ServerLevel) this.level(), true);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.ANT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return NaturalistSoundEvents.ANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.ANT_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(NaturalistSoundEvents.ANT_STEP.get(), 0.375F, 1.0F);
    }

    private static class AntEnterHillGoal extends MoveToBlockGoal {
        private final Ant ant;

        public AntEnterHillGoal(Ant mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.ant = mob;
        }

        @Override
        public boolean canUse() {
            return this.ant.hillCooldown <= 0 && this.ant.getTarget() == null && !this.ant.isCarryingFood() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.ant.getTarget() == null && !this.ant.isCarryingFood() && super.canContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget() && this.ant.level() instanceof ServerLevel level && !AntHillBlock.tryEnter(level, this.blockPos, this.ant)) {
                this.ant.startHillCooldown();
            }
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return AntHillBlock.canAntEnter(level, pos, this.ant.getOwnerUUID());
        }
    }

    private static class AntGatherFoodGoal extends Goal {
        private final Ant ant;
        @Nullable
        private ItemEntity target;

        AntGatherFoodGoal(Ant ant) {
            this.ant = ant;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.ant.tickCount % 10 != 0 || this.ant.isCarryingFood() || this.ant.getTarget() != null) {
                return false;
            }
            this.target = this.ant.findNearbyFood();
            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && !this.target.hasPickUpDelay()
                    && !this.ant.isCarryingFood() && this.ant.getTarget() == null;
        }

        @Override
        public void start() {
            if (this.target != null) {
                this.ant.getNavigation().moveTo(this.target, 1.15D);
            }
        }

        @Override
        public void stop() {
            this.target = null;
            this.ant.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.target == null) {
                return;
            }
            this.ant.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (this.ant.getNavigation().isDone()) {
                this.ant.getNavigation().moveTo(this.target, 1.15D);
            }
            if (this.ant.distanceToSqr(this.target) < 1.0D) {
                this.ant.pickUpOneFood(this.target);
            }
        }
    }

    private static class AntStoreFoodGoal extends MoveToBlockGoal {
        private final Ant ant;

        AntStoreFoodGoal(Ant mob) {
            super(mob, 1.0, 16, 5);
            this.ant = mob;
        }

        @Override
        public boolean canUse() {
            return this.ant.isCarryingFood() && this.ant.getTarget() == null && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.ant.isCarryingFood() && this.ant.getTarget() == null && super.canContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget() && this.ant.level() instanceof ServerLevel level) {
                AntHillBlock.storeFood(level, this.blockPos, this.ant);
            }
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return AntHillBlock.canAntStore(level, pos, this.ant.getOwnerUUID());
        }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        this.walkAnimationState.animateWhen(moving, this.tickCount);
        this.idleAnimationState.animateWhen(!moving, this.tickCount);
    }
    //endregion

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
