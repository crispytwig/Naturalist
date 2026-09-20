package com.crispytwig.naturalist.world.entity.animal.giantisopod;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.HidingAnimal;
import com.crispytwig.naturalist.world.entity.VariantBucketable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class GiantIsopod extends Animal implements HidingAnimal, VariantBucketable {
    //region Data
    public static final String[] VARIANT_NAMES = {"brown", "blue"};

    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("giant_isopod"), "brown");

    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.GIANT_ISOPOD_FOOD);
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(GiantIsopod.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(GiantIsopod.class, EntityDataSerializers.BOOLEAN);

    private static final int HIDE_END_ANIM_TICKS = 14;

    private boolean wasHiding;
    private boolean wasHidingClient;
    private int hideEndTicks;
    private long hideCacheTick = -1L;
    private boolean hideCache;
    private int hideHoldTicks;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState hideAnimationState = SmoothAnimationState.instant();
    public final SmoothAnimationState hideEndAnimationState = SmoothAnimationState.instant();

    public GiantIsopod(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, DEFAULT_VARIANT.identifier().toString());
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return DEFAULT_VARIANT;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/giant_isopod/brown.png");
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
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return distance > 16384.0D && !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromBucket(input.getBooleanOr("FromBucket", false));
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.GIANT_ISOPOD_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
    //endregion

    //region Spawning
    public static boolean checkGiantIsopodSpawnRules(EntityType<? extends GiantIsopod> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return pos.getY() < level.getLevel().getSeaLevel()
                && level.getFluidState(pos).is(FluidTags.WATER)
                && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (reason != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public GiantIsopod getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        GiantIsopod baby = NaturalistEntityTypes.GIANT_ISOPOD.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(mob, this.random));
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 5.0F, 1.25D, 1.5D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 30));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        GroundPathNavigation navigation = new GroundPathNavigation(this, level);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.92F;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public boolean canHide() {
        if (this.level().getGameTime() == this.hideCacheTick) {
            return this.hideCache;
        }
        this.hideCacheTick = this.level().getGameTime();
        this.hideCache = this.computeCanHide();
        return this.hideCache;
    }

    private boolean computeCanHide() {
        if (this.isBaby()) {
            return false;
        }
        if (this.hideHoldTicks > 0) {
            return true;
        }
        AABB area = this.getBoundingBox().inflate(3.0D, 2.0D, 3.0D);
        if (this.level() instanceof ServerLevel serverLevel) {
            return !serverLevel.getNearbyPlayers(
                    TargetingConditions.forNonCombat().range(3.0D).selector((entity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)),
                    this, area).isEmpty();
        }
        return !this.level().getEntitiesOfClass(Player.class, area,
                player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && this.distanceToSqr(player) <= 9.0D).isEmpty();
    }

    @Override
    public boolean hurtClient(@NotNull DamageSource source) {
        if (this.canHide()) {
            this.hideHoldTicks = 10;
        }
        return super.hurtClient(source);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.canHide() ? strength * 0.25D : strength, x, z);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (this.canHide()) {
            this.hideHoldTicks = 10;
        }
        return super.hurtServer(level, source, this.canHide() ? amount * 0.5F : amount);
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.canHide()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
            vec3 = vec3.multiply(0.0D, 1.0D, 0.0D);
        }
        if (!this.isInWater()) {
            vec3 = vec3.multiply(0.35D, 1.0D, 0.35D);
        }
        super.travel(vec3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.hideHoldTicks > 0) {
            this.hideHoldTicks--;
            this.hideCacheTick = -1L;
        }
        if (!this.level().isClientSide()) {
            boolean hiding = this.canHide();
            if (hiding) {
                this.jumping = false;
                this.xxa = 0.0F;
                this.zza = 0.0F;
                this.getNavigation().stop();
                if (!this.wasHiding) {
                    this.playSound(NaturalistSoundEvents.GIANT_ISOPOD_ROLL.get(), 0.6F, 1.0F);
                }
            }
            this.wasHiding = hiding;
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
        boolean hiding = this.canHide();
        if (!hiding && this.wasHidingClient) {
            this.hideEndTicks = HIDE_END_ANIM_TICKS;
        } else if (this.hideEndTicks > 0) {
            this.hideEndTicks--;
        }
        this.wasHidingClient = hiding;
        if (hiding) {
            this.hideEndTicks = 0;
        }

        boolean hideEnding = !hiding && this.hideEndTicks > 0;
        boolean posing = hiding || hideEnding;
        boolean swimming = !posing && !this.onGround() && this.isInWater();
        boolean moving = !posing && !swimming && NaturalistAnimal.isVisiblyMoving(this);

        this.hideAnimationState.animateWhen(hiding, this.tickCount);
        this.hideEndAnimationState.animateWhen(hideEnding, this.tickCount);
        this.swimAnimationState.animateWhen(swimming, this.tickCount);
        this.walkAnimationState.animateWhen(moving, this.tickCount);
        this.idleAnimationState.animateWhen(!posing && !swimming && !moving, this.tickCount);
    }
    //endregion

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
