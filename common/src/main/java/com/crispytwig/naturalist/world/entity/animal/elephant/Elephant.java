package com.crispytwig.naturalist.world.entity.animal.elephant;

import com.crispytwig.naturalist.world.entity.LegacyEntityHelper;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.DistancedFollowParentGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import com.crispytwig.naturalist.world.inventory.ElephantInventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.world.entity.IKMount;
import com.crispytwig.naturalist.world.entity.TerrainLegSolver;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class Elephant extends TamableAnimal implements NeutralMob, IKMount, DataDrivenVariantAnimal {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.MELON_SLICE);
    private static final int INVENTORY_SIZE = 27;
    private static final int CAKES_TO_TAME = 5;
    private static final int SWING_ANIM_TICKS = 25;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> DATA_BANNER = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.ITEM_STACK);

    private final SimpleContainer inventory = new SimpleContainer(INVENTORY_SIZE);
    private int tamingFood;
    @Nullable
    private UUID persistentAngerTarget;
    public final TerrainLegSolver legSolver = new TerrainLegSolver(1.0F, 0.5F, 0.9F);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swingAnimationState = SmoothAnimationState.instant();
    private int swingAnimTicks;

    public float bannerLift;
    public float bannerLiftO;
    public float bannerSwing;
    public float bannerSwingO;

    public Elephant(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(1.0F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 15.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_ID_CHEST, false);
        this.entityData.define(DATA_BANNER, ItemStack.EMPTY);
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/elephant/elephant.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLE_ID);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLE_ID, saddled);
    }

    public boolean hasChest() {
        return this.entityData.get(DATA_ID_CHEST);
    }

    public void setChest(boolean chested) {
        this.entityData.set(DATA_ID_CHEST, chested);
    }

    public ItemStack getBanner() {
        return this.entityData.get(DATA_BANNER);
    }

    public void setBanner(ItemStack stack) {
        this.entityData.set(DATA_BANNER, stack.copyWithCount(1));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        this.addPersistentAngerSaveData(compound);
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putBoolean("Chested", this.hasChest());
        compound.putInt("TamingFood", this.tamingFood);
        NonNullList<ItemStack> items = NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++) {
            items.set(i, this.inventory.getItem(i));
        }
        ContainerHelper.saveAllItems(compound, items);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setChest(compound.getBoolean("Chested"));
        this.tamingFood = compound.getInt("TamingFood");
        NonNullList<ItemStack> items = NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, items);
        for (int i = 0; i < items.size(); i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.selectVariantForSpawn(level);
        AgeableMobGroupData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(true);
        }
        if ((ageableMobGroupData = (AgeableMobGroupData)spawnData).getGroupSize() > 1) {
            this.setAge(-24000);
        }
        ageableMobGroupData.increaseGroupSizeByOne();
        RandomSource random = level.getRandom();
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier("Random spawn bonus", random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.MULTIPLY_BASE));
        return spawnData;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Elephant baby = NaturalistEntityTypes.ELEPHANT.get().create(serverLevel);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Bee.class, 8.0f, 1.3, 1.3));
        this.goalSelector.addGoal(2, new ElephantMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new DistancedFollowParentGoal(this, 1.2D, 24.0D, 6.0D, 12.0D));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!this.isAlive()) {
            return;
        }
        LivingEntity livingEntity = this.getControllingPassenger();
        if (!this.isVehicle() || livingEntity == null) {
            super.travel(travelVector);
            return;
        }
        this.setYRot(livingEntity.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(livingEntity.getXRot() * 0.5f);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = this.getYRot();
        this.yBodyRot = Mth.rotLerp(0.35F, this.yBodyRot, this.getYRot());
        float f = livingEntity.xxa * 0.5f;
        float g = livingEntity.zza;
        if (this.isControlledByLocalInstance()) {
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.3F);
            super.travel(new Vec3(f, travelVector.y, g));
        } else if (livingEntity instanceof Player) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.calculateEntityAnimation(false);
        this.tryCheckInsideBlocks();
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (this.isBaby()) {
            double knockbackResistance = this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            super.knockback(strength / Math.max(1.0 - knockbackResistance, 0.01), x, z);
        } else {
            super.knockback(strength, x, z);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return 35;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean shouldHurt = target.hurt(target.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (shouldHurt && target instanceof LivingEntity livingEntity) {
            Vec3 knockbackDirection = new Vec3(this.blockPosition().getX() - target.getX(), 0.0, this.blockPosition().getZ() - target.getZ()).normalize();
            float shieldBlockModifier = livingEntity.isDamageSourceBlocked(target.damageSources().mobAttack(this)) ? 0.5f : 1.0f;
            livingEntity.knockback(shieldBlockModifier * 3.0D, knockbackDirection.x(), knockbackDirection.z());
            double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0.0, 0.5f * knockbackResistance, 0.0));
        }
        if (shouldHurt) {
            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }
        this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0f, 1.0f);
        return shouldHurt;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return PetTargeting.protectsOwnedPet(this, target) && super.canAttack(target);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isBaby() && this.isVehicle()) {
            return super.mobInteract(player, hand);
        }
        if (!this.isTame()) {
            if (this.isBaby() && stack.is(Items.CAKE)) {
                if (!this.level().isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    if (++this.tamingFood >= CAKES_TO_TAME) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return super.mobInteract(player, hand);
        }
        if (this.isOwnedBy(player)) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(4.0F);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.isBaby() && stack.is(Items.SHEARS)) {
                if (this.hasChest()) {
                    if (!this.level().isClientSide) {
                        this.popItem(new ItemStack(Items.CHEST));
                        for (int i = 0; i < ElephantInventoryMenu.CHEST_SIZE; i++) {
                            this.popItem(this.inventory.getItem(i));
                            this.inventory.setItem(i, ItemStack.EMPTY);
                        }
                        this.setChest(false);
                        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                        this.gameEvent(GameEvent.SHEAR, player);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
                if (this.isSaddled()) {
                    if (!this.level().isClientSide) {
                        ItemStack saddleStack = this.inventory.getItem(ElephantInventoryMenu.SADDLE_SLOT);
                        this.popItem(saddleStack.isEmpty() ? new ItemStack(Items.SADDLE) : saddleStack.copy());
                        this.inventory.setItem(ElephantInventoryMenu.SADDLE_SLOT, ItemStack.EMPTY);
                        this.setSaddled(false);
                        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                        this.gameEvent(GameEvent.SHEAR, player);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
            if (!this.isBaby() && !this.hasChest() && stack.is(Items.CHEST)) {
                if (!this.level().isClientSide) {
                    this.setChest(true);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.playSound(SoundEvents.MULE_CHEST, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.isBaby() && stack.is(Items.SADDLE) && !this.isSaddled()) {
                if (!this.level().isClientSide) {
                    this.inventory.setItem(ElephantInventoryMenu.SADDLE_SLOT, stack.copyWithCount(1));
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    this.setSaddled(true);
                    this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.isBaby() && player.isSecondaryUseActive()) {
                this.openCustomInventoryScreen(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.isBaby() && this.isSaddled() && stack.isEmpty()) {
                this.doPlayerRide(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    protected void doPlayerRide(@NotNull Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, Entity.@NotNull MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            LegacyEntityHelper.positionRider(this, passenger, callback, this.getRiderAttachment());
        }
        if (passenger instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getRiderAttachment().y;
    }

    protected Vec3 getRiderAttachment() {
        return new Vec3(0.0, this.getBbHeight() * 0.92, -0.1);
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
        double d = this.getX() + direction.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + direction.z;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        block0:
        for (Pose pose : passenger.getDismountPoses()) {
            mutableBlockPos.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;
            do {
                Vec3 vec3;
                double h = this.level().getBlockFloorHeight(mutableBlockPos);
                if ((double) mutableBlockPos.getY() + h > g) continue block0;
                if (DismountHelper.isBlockFloorValid(h) && DismountHelper.canDismountTo(this.level(), passenger, passenger.getLocalBoundsForPose(pose).move(vec3 = new Vec3(d, (double) mutableBlockPos.getY() + h, f)))) {
                    passenger.setPose(pose);
                    return vec3;
                }
                mutableBlockPos.move(Direction.UP);
            } while ((double) mutableBlockPos.getY() < g);
        }
        return null;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        Vec3 vec3 = AbstractHorse.getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0f : -90.0f));
        Vec3 vec32 = this.getDismountLocationInDirection(vec3, passenger);
        if (vec32 != null) {
            return vec32;
        }
        Vec3 vec33 = AbstractHorse.getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0f : -90.0f));
        Vec3 vec34 = this.getDismountLocationInDirection(vec33, passenger);
        return vec34 != null ? vec34 : this.position();
    }

    private void openCustomInventoryScreen(Player player) {
        if (this.level().isClientSide) {
            ElephantInventoryMenu.clientOpenEntityId = this.getId();
            return;
        }
        if (this.isTame()) {
            player.openMenu(new SimpleMenuProvider((id, inv, p) -> new ElephantInventoryMenu(id, inv, this.inventory, this), this.getDisplayName()));
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (!this.level().isClientSide) {
            if (this.hasChest()) {
                this.popItem(new ItemStack(Items.CHEST));
            }
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                this.popItem(this.inventory.getItem(i));
            }
            this.inventory.clearContent();
        }
        this.setSaddled(false);
        this.setChest(false);
        this.setBanner(ItemStack.EMPTY);
    }

    private void popItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemEntity item = this.spawnAtLocation(stack, this.getBbHeight());
        if (item != null) {
            item.setDeltaMovement(item.getDeltaMovement().add(0.0, 0.15, 0.0));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.legSolver.update(this, this.getScale() * (this.isBaby() ? 0.5F : 1.0F));
            this.setupAnimationStates();
            this.updateBannerPhysics();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.setSaddled(!this.inventory.getItem(ElephantInventoryMenu.SADDLE_SLOT).isEmpty());
        ItemStack banner = this.inventory.getItem(ElephantInventoryMenu.BANNER_SLOT);
        if (!ItemStack.isSameItemSameTags(banner, this.getBanner())) {
            this.setBanner(banner);
        }
        if (this.isVehicle()) {
            this.setSprinting(false);
        } else if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() > 1.0D);
        } else {
            this.setSprinting(false);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.ELEPHANT_AMBIENT_BABY.get() : NaturalistSoundEvents.ELEPHANT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.ELEPHANT_HURT_BABY.get() : NaturalistSoundEvents.ELEPHANT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.ELEPHANT_DEATH_BABY.get() : NaturalistSoundEvents.ELEPHANT_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    static class ElephantMeleeAttackGoal extends MeleeAttackGoal {
        public ElephantMeleeAttackGoal(PathfinderMob pathfinderMob, double speedMultiplier, boolean followingTargetEvenIfNotSeen) {
            super(pathfinderMob, speedMultiplier, followingTargetEvenIfNotSeen);
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity target, double distToEnemySqr) {
            if (distToEnemySqr <= Mth.square(this.mob.getBbWidth()) && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
            }
        }
    }
    //endregion

    //region Animation
    private void setupAnimationStates() {
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);

        if (this.swinging && this.swingAnimTicks <= 0) {
            this.swingAnimTicks = SWING_ANIM_TICKS;
        } else if (this.swingAnimTicks > 0) {
            this.swingAnimTicks--;
        }
        this.swingAnimationState.animateWhen(this.swingAnimTicks > 0, this.tickCount);

        this.walkAnimationState.animateWhen(moving && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(moving && this.isSprinting(), this.tickCount);
        this.idleAnimationState.animateWhen(!moving, this.tickCount);
    }

    private void updateBannerPhysics() {
        Vec3 velocity = this.getDeltaMovement().yRot(this.yBodyRot * Mth.DEG_TO_RAD);
        float liftTarget = (float) velocity.z * 120.0F + Math.max(this.getRenderPitch() * Mth.RAD_TO_DEG, 0.0F);
        float swingTarget = Mth.degreesDifference(this.yBodyRotO, this.yBodyRot) * 1.5F - (float) velocity.x * 160.0F;

        this.bannerLiftO = this.bannerLift;
        this.bannerSwingO = this.bannerSwing;
        this.bannerLift += (Mth.clamp(liftTarget, -35.0F, 35.0F) - this.bannerLift) * 0.15F;
        this.bannerSwing += (Mth.clamp(swingTarget, -20.0F, 20.0F) - this.bannerSwing) * 0.15F;
    }

    @Override
    public float getRenderPitch() {
        return this.legSolver.renderPitch;
    }

    @Override
    public float getRenderRoll() {
        return this.legSolver.renderRoll;
    }
    //endregion
}
