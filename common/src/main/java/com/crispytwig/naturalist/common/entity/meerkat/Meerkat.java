package com.crispytwig.naturalist.common.entity.meerkat;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.common.entity.Catchable;
import com.crispytwig.naturalist.common.entity.FollowingPet;
import com.crispytwig.naturalist.common.entity.HuntingAnimal;
import com.crispytwig.naturalist.common.entity.NaturalistAnimal;
import com.crispytwig.naturalist.common.entity.SleepingAnimal;
import com.crispytwig.naturalist.common.entity.SmoothAnimationState;
import com.crispytwig.naturalist.common.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.common.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.common.entity.scorpion.DesertScorpion;
import com.crispytwig.naturalist.common.entity.snail.Snail;
import com.crispytwig.naturalist.common.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.common.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.common.registry.NaturalistRegistry;
import com.crispytwig.naturalist.common.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.common.tags.NaturalistItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.crispytwig.naturalist.common.level.block.entity.MeerkatWarrenBlockEntity;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Path;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class Meerkat extends TamableAnimal implements FollowingPet, Catchable, HuntingAnimal, SleepingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LOOKOUT = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALERT = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ALERT_YAW = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.INT);

    private static final double ALERT_RADIUS = 16.0D;
    private static final double ALERT_RELEASE_RADIUS = 19.0D;
    private static final double ALERT_HEIGHT = 6.0D;
    private static final double PANIC_RADIUS = 4.0D;
    private static final double GROUP_RADIUS = 12.0D;
    private static final int DARK_LIGHT_MAX = 7;
    private static final int DARK_COOLDOWN = 200;
    private static final int LOOKOUT_COOLDOWN = 600;
    private static final int SENTINEL_REST = 700;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState lookoutAnimationState = new SmoothAnimationState();

    private boolean followingOwner = true;
    private int huntingCooldown;
    private int sleepCooldown;
    private int lookoutTicks;
    private int lookoutCooldown;
    private int pendingLookout;
    private int sentinelTicks;
    private int sentinelRest;
    private int darkChirpTicks;
    private int darkCooldown;
    private int settleTicks;
    private int chirpDelay;
    private boolean alertUnison;
    @Nullable
    private Mob alertThreat;

    @Nullable
    private BlockPos warrenPos;
    private int stayOutOfWarrenCountdown;
    private int remainingCooldownBeforeLocatingNewWarren;
    private GoToWarrenGoal goToWarrenGoal;

    private float lookoutBlend;
    private float lookoutBlendO;
    private float aimYaw;
    private float aimYawO;

    public Meerkat(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(FROM_HAND, false);
        builder.define(SLEEPING, false);
        builder.define(LOOKOUT, false);
        builder.define(ALERT, false);
        builder.define(ALERT_YAW, 0);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/meerkat/meerkat.png");
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
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
        if (!sleeping) {
            this.sleepCooldown = Mth.randomBetweenInclusive(this.random, 40, 400);
        }
    }

    public boolean isLookout() {
        return this.entityData.get(LOOKOUT);
    }

    public boolean isAlert() {
        return this.entityData.get(ALERT);
    }

    public float getAimYaw(float partialTick) {
        return Mth.rotLerp(partialTick, this.aimYawO, this.aimYaw);
    }

    public float getLookoutBlend(float partialTick) {
        return Mth.lerp(partialTick, this.lookoutBlendO, this.lookoutBlend);
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.6F : 1.0F;
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
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("FromHand", this.fromHand());
        this.saveHuntingCooldown(output);
        FollowingPet.savePet(this, output);
        output.storeNullable("warren_pos", BlockPos.CODEC, this.warrenPos);
        output.putInt("CannotEnterWarrenTicks", this.stayOutOfWarrenCountdown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setFromHand(input.getBooleanOr("FromHand", false));
        this.loadHuntingCooldown(input);
        FollowingPet.loadPet(this, input);
        this.warrenPos = input.read("warren_pos", BlockPos.CODEC).orElse(null);
        this.stayOutOfWarrenCountdown = input.getIntOr("CannotEnterWarrenTicks", 0);
    }

    @Override
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(tag);
        tag.putInt("Age", this.getAge());
        Catchable.saveTamableDataToHandTag(this, tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);
        this.setAge(tag.getIntOr("Age", 0));
        Catchable.loadTamableDataFromHandTag(this, tag);
    }

    @Override
    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.MEERKAT.get());
    }

    @Nullable
    @Override
    public SoundEvent getPickupSound() {
        return null;
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    protected int getBaseExperienceReward(@NotNull ServerLevel level) {
        return 3;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : NaturalistSoundEvents.MEERKAT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.MEERKAT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.MEERKAT_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? 1.5F : 0.9F + this.random.nextFloat() * 0.2F;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(NaturalistItemTags.MEERKAT_FOOD_ITEMS);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        Meerkat baby = NaturalistEntityTypes.MEERKAT.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(partner, this.random));
            if (this.isTame()) {
                baby.setOwnerReference(this.getOwnerReference());
                baby.setTame(true, true);
            }
        }
        return baby;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        if (this.random.nextFloat() < 0.15F) {
            this.setAge(BABY_START_AGE);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData != null ? spawnGroupData : new AgeableMobGroupData(false));
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new EnterWarrenGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 10.0F, 1.2D, 1.4D, entity -> !this.isSleeping() && !this.isTame() && !this.isAlert() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !entity.isCrouching() && !entity.getMainHandItem().is(NaturalistItemTags.MEERKAT_TEMPT_ITEMS)));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Mob.class, 10.0F, 1.2D, 1.4D, entity -> !this.isSleeping() && !this.isAlert() && entity instanceof Enemy));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2D, stack -> stack.is(NaturalistItemTags.MEERKAT_TEMPT_ITEMS), false));
        this.goalSelector.addGoal(5, new LocateWarrenGoal(this));
        this.goToWarrenGoal = new GoToWarrenGoal(this);
        this.goalSelector.addGoal(5, this.goToWarrenGoal);
        this.goalSelector.addGoal(6, new SleepGoal<>(this));
        this.goalSelector.addGoal(6, new PetFollowOwnerGoal(this, 1.2D, 10.0F, 3.0F));
        this.goalSelector.addGoal(6, new DarkScoutGoal(this));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.3D, true));
        this.goalSelector.addGoal(10, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(11, new StandWatchGoal(this));
        this.goalSelector.addGoal(12, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(13, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(14, new LookAtPlayerGoal(this, Player.class, 6.0F, 0.02F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity, serverLevel) -> !this.isBaby() && this.canHunt() && (entity instanceof Snail || entity instanceof DesertScorpion)));
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed, @NotNull DamageSource source) {
        if (killed instanceof Snail || killed instanceof DesertScorpion) {
            this.setHuntingCooldown(6000);
        }
        return super.killedEntity(level, killed, source);
    }

    @Override
    public boolean canSleep() {
        if (this.sleepCooldown > 0 || !this.onGround() || this.isInWaterOrRain() || !this.level().isDarkOutside() || this.level().isThundering()
                || this.getTarget() != null || this.isOrderedToSit() || this.warrenPos != null && this.wantsToEnterWarren()) {
            return false;
        }
        return this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0D),
                entity -> entity != this && !(entity instanceof Meerkat) && !entity.isCrouching() && !this.isOwnedBy(entity)).isEmpty();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (!this.isTame() || this.isOwnedBy(player)) {
            Optional<InteractionResult> caught = Catchable.catchAnimal(player, hand, this, true);
            if (caught.isPresent()) {
                return caught.get();
            }
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(NaturalistRegistry.GRUB.get())) {
            if (!this.isTame()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, stack);
                    if (this.random.nextFloat() < 0.4F) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            if (this.isOwnedBy(player) && this.getHealth() < this.getMaxHealth()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, stack);
                    this.heal(4.0F);
                    this.playSound(NaturalistAnimal.eatingSound(stack), 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
        }
        if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide()) {
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);
                }
                this.usePlayerItem(player, hand, stack);
                this.heal(2.0F);
                this.playSound(NaturalistAnimal.eatingSound(stack), 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.isTame() && this.isOwnedBy(player) && stack.isEmpty() && !player.isSecondaryUseActive()) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        boolean hurt = super.hurtServer(level, source, amount);
        if (hurt) {
            this.setSleeping(false);
            this.clearWatch();
        }
        return hurt;
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        this.tickHuntingCooldown();
        this.sleepCooldown = Math.max(0, this.sleepCooldown - 1);
        this.lookoutCooldown = Math.max(0, this.lookoutCooldown - 1);
        this.sentinelRest = Math.max(0, this.sentinelRest - 1);
        this.darkCooldown = Math.max(0, this.darkCooldown - 1);
        this.stayOutOfWarrenCountdown = Math.max(0, this.stayOutOfWarrenCountdown - 1);
        this.remainingCooldownBeforeLocatingNewWarren = Math.max(0, this.remainingCooldownBeforeLocatingNewWarren - 1);
        if (this.warrenPos != null && this.tickCount % 20 == 0 && level.isLoaded(this.warrenPos) && this.getWarren() == null) {
            this.dropWarren();
        }
        if (this.lookoutTicks > 0 && --this.lookoutTicks == 0) {
            this.lookoutCooldown = LOOKOUT_COOLDOWN;
        }
        if (this.sentinelTicks > 0 && --this.sentinelTicks == 0) {
            this.sentinelRest = SENTINEL_REST;
        }
        if (this.darkChirpTicks > 0 && --this.darkChirpTicks == 0) {
            this.darkCooldown = DARK_COOLDOWN;
        }
        if (this.settleTicks > 0 && --this.settleTicks == 0) {
            this.alertThreat = null;
        }
        if (this.isInLove()) {
            this.clearWatch();
            this.lookoutCooldown = LOOKOUT_COOLDOWN;
        }
        if (this.pendingLookout > 0 && --this.pendingLookout == 0 && this.canStandWatch() && this.isStill()) {
            this.lookoutTicks = this.isBaby() ? 80 : 130;
        }
        int phase = this.tickCount + this.getId();
        if (phase % 80 == 0) {
            this.tryStartLookout();
        }
        if (phase % 10 == 0) {
            this.scanForThreats(level);
        }
        if (phase % 40 == 0) {
            this.tryStartSentinel(level);
        }
        this.trackThreat();
        this.entityData.set(LOOKOUT, this.lookoutTicks > 0 || this.sentinelTicks > 0 || this.darkChirpTicks > 0 || this.alertThreat != null);
    }

    public void setWarrenPos(@Nullable BlockPos warrenPos) {
        this.warrenPos = warrenPos;
    }

    public void setStayOutOfWarrenCountdown(int ticks) {
        this.stayOutOfWarrenCountdown = ticks;
    }

    private boolean wantsToEnterWarren() {
        return this.stayOutOfWarrenCountdown <= 0 && this.getTarget() == null && !this.isTame() && !this.hasCustomName() && !this.isInLove() && !this.level().isBrightOutside();
    }

    @Nullable
    private MeerkatWarrenBlockEntity getWarren() {
        return this.warrenPos != null && this.level().getBlockEntity(this.warrenPos) instanceof MeerkatWarrenBlockEntity warren ? warren : null;
    }

    private void dropWarren() {
        this.warrenPos = null;
        this.remainingCooldownBeforeLocatingNewWarren = 200;
    }

    private boolean isStill() {
        return this.onGround() && this.getDeltaMovement().horizontalDistance() <= 0.05D;
    }

    private boolean canStandWatch() {
        return this.lookoutTicks <= 0 && this.lookoutCooldown <= 0 && this.alertThreat == null && this.sentinelTicks <= 0
                && !this.isInLove() && !this.isSleeping() && !this.isOrderedToSit();
    }

    private void tryStartLookout() {
        if (this.isBaby() || !this.canStandWatch() || !this.isStill() || this.random.nextFloat() >= 0.03F) {
            return;
        }
        this.lookoutTicks = 130;
        for (Meerkat other : this.level().getEntitiesOfClass(Meerkat.class, this.getBoundingBox().inflate(10.0D), other -> other != this && other.canStandWatch() && other.pendingLookout <= 0)) {
            if (other.isBaby() && this.random.nextFloat() >= 0.75F) {
                continue;
            }
            other.pendingLookout = other.isBaby() ? Mth.randomBetweenInclusive(this.random, 8, 30) : Mth.randomBetweenInclusive(this.random, 20, 80);
        }
    }

    private double horizontalDistanceTo(LivingEntity entity) {
        return Math.sqrt(Mth.square(entity.getX() - this.getX()) + Mth.square(entity.getZ() - this.getZ()));
    }

    private void scanForThreats(ServerLevel level) {
        if (this.isBaby() || this.isTame()) {
            if (this.alertThreat != null) {
                this.clearWatch();
            }
            return;
        }
        Mob threat = null;
        double distance = 0.0D;
        boolean unison = false;
        if (!this.isSleeping() && !this.isInLove()) {
            List<Mob> enemies = level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(ALERT_RELEASE_RADIUS, ALERT_HEIGHT, ALERT_RELEASE_RADIUS), entity -> entity instanceof Enemy && entity.isAlive());
            enemies.sort(Comparator.comparingDouble(this::horizontalDistanceTo));
            if (enemies.isEmpty() || this.horizontalDistanceTo(enemies.getFirst()) > PANIC_RADIUS) {
                for (Mob enemy : enemies.subList(0, Math.min(3, enemies.size()))) {
                    double enemyDistance = this.horizontalDistanceTo(enemy);
                    if (enemyDistance <= (this.alertThreat != null ? ALERT_RELEASE_RADIUS : ALERT_RADIUS) && this.hasLineOfSight(enemy)) {
                        threat = enemy;
                        distance = enemyDistance;
                        break;
                    }
                }
                if (threat == null) {
                    for (Meerkat other : level.getEntitiesOfClass(Meerkat.class, this.getBoundingBox().inflate(GROUP_RADIUS, ALERT_HEIGHT, GROUP_RADIUS), other -> other != this && other.alertThreat != null && other.settleTicks <= 0 && !other.alertUnison)) {
                        double leadDistance = this.horizontalDistanceTo(other.alertThreat);
                        if (leadDistance > PANIC_RADIUS && leadDistance <= 26.0D && Math.abs(other.alertThreat.getY() - this.getY()) <= ALERT_HEIGHT && this.hasLineOfSight(other.alertThreat)) {
                            threat = other.alertThreat;
                            distance = leadDistance;
                            unison = true;
                            break;
                        }
                    }
                }
            }
        }
        if (threat != null) {
            boolean starting = this.alertThreat == null;
            this.alertThreat = threat;
            this.alertUnison = unison;
            if (starting || this.settleTicks > 0) {
                this.settleTicks = 0;
                this.entityData.set(ALERT, true);
                this.chirpDelay = this.chirpInterval(distance);
            }
            if (starting) {
                this.lookoutTicks = 0;
                this.darkChirpTicks = 0;
                this.stopSentinel();
                this.playAlarm(distance);
            }
        } else if (this.alertThreat != null && this.settleTicks <= 0) {
            this.settleTicks = 20;
            this.entityData.set(ALERT, false);
        }
    }

    private float threatCloseness(double distance) {
        return Mth.clamp((float) ((distance - PANIC_RADIUS) / (ALERT_RADIUS - PANIC_RADIUS)), 0.0F, 1.0F);
    }

    private int chirpInterval(double distance) {
        return Math.max(4, Math.round(Mth.lerp(this.threatCloseness(distance), 12.0F, 55.0F) * (this.alertUnison ? 2.0F : 1.0F) * (1.0F + (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
    }

    private void playAlarm(double distance) {
        float closeness = this.threatCloseness(distance);
        this.playSound(NaturalistSoundEvents.MEERKAT_ALERT.get(), (this.alertUnison ? 1.0F : 1.2F) - closeness * 0.35F, 1.1F - closeness * 0.15F + this.random.nextFloat() * 0.06F - 0.03F);
    }

    private void trackThreat() {
        if (this.alertThreat == null || this.settleTicks > 0 || !this.alertThreat.isAlive()) {
            return;
        }
        double dx = this.alertThreat.getX() - this.getX();
        double dz = this.alertThreat.getZ() - this.getZ();
        if (--this.chirpDelay <= 0) {
            double distance = Math.sqrt(dx * dx + dz * dz);
            this.playAlarm(distance);
            this.chirpDelay = this.chirpInterval(distance);
        }
        float targetYaw = (float) (Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);
        this.entityData.set(ALERT_YAW, Math.floorMod(Math.round(targetYaw), 360));
        float yaw = this.getYRot() + Mth.clamp(Mth.wrapDegrees(targetYaw - this.getYRot()), -14.0F, 14.0F);
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
        this.setYHeadRot(yaw);
    }

    private void stopSentinel() {
        if (this.sentinelTicks > 0) {
            this.sentinelTicks = 0;
            this.sentinelRest = SENTINEL_REST;
        }
    }

    private void clearWatch() {
        this.lookoutTicks = 0;
        this.pendingLookout = 0;
        this.darkChirpTicks = 0;
        this.settleTicks = 0;
        this.alertThreat = null;
        this.entityData.set(ALERT, false);
        this.stopSentinel();
    }

    private void tryStartSentinel(ServerLevel level) {
        if (this.isBaby() || this.isTame() || this.isSleeping() || this.isOrderedToSit() || this.isInLove() || this.sentinelTicks > 0 || this.sentinelRest > 0 || this.alertThreat != null || !this.onGround()) {
            return;
        }
        List<Meerkat> group = level.getEntitiesOfClass(Meerkat.class, this.getBoundingBox().inflate(GROUP_RADIUS, ALERT_HEIGHT, GROUP_RADIUS), other -> !other.isBaby() && !other.isTame());
        if (group.size() < 2 || group.stream().anyMatch(other -> other.sentinelTicks > 0 || other.sentinelRest > 0 || other.alertThreat != null)) {
            return;
        }
        Vec3 center = group.stream().map(Meerkat::position).reduce(Vec3.ZERO, Vec3::add).scale(1.0D / group.size());
        if (group.stream().filter(Meerkat::onGround).min(Comparator.comparingDouble(other -> other.position().distanceToSqr(center))).orElse(null) == this) {
            this.lookoutTicks = 0;
            this.sentinelTicks = 400;
        }
    }

    private boolean isScout() {
        return !this.isBaby() && this.isTame() && !this.isSleeping() && !this.isOrderedToSit() && !this.isFollowingOwner();
    }

    private void chirpAtDark() {
        this.darkChirpTicks = 70;
        this.playSound(NaturalistSoundEvents.MEERKAT_ALERT.get(), 0.8F, 1.2F);
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            return;
        }
        double speed = this.getDeltaMovement().horizontalDistance();
        boolean moving = this.onGround() && NaturalistAnimal.isVisiblyMoving(this);
        boolean sleeping = this.isSleeping();
        boolean sitting = !sleeping && this.isInSittingPose() && this.onGround();
        boolean running = !sleeping && !sitting && moving && speed > 0.135D;
        boolean standing = this.isLookout() && !sitting && !sleeping && this.onGround() && speed <= 0.02D;

        this.idleAnimationState.animateWhen(!sleeping && !sitting && !moving, this.tickCount);
        this.walkAnimationState.animateWhen(!sleeping && !sitting && moving && !running, this.tickCount);
        this.runAnimationState.animateWhen(running, this.tickCount);
        this.sleepAnimationState.animateWhen(sleeping, this.tickCount);
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.lookoutAnimationState.animateWhen(standing, this.tickCount);
        if (this.isAlert() && this.lookoutAnimationState.getAccumulatedTime() >= 5500L) {
            this.lookoutAnimationState.seek(750L);
        }

        this.lookoutBlendO = this.lookoutBlend;
        this.lookoutBlend += ((standing ? 1.0F : 0.0F) - this.lookoutBlend) * 0.5F;
        this.aimYawO = this.aimYaw;
        this.aimYaw += Mth.wrapDegrees((this.isAlert() ? Mth.wrapDegrees(this.entityData.get(ALERT_YAW) - this.yBodyRot) : 0.0F) - this.aimYaw) * 0.5F;
    }
    //endregion

    //region Goals
    static class EnterWarrenGoal extends Goal {
        private final Meerkat meerkat;

        EnterWarrenGoal(Meerkat meerkat) {
            this.meerkat = meerkat;
        }

        @Override
        public boolean canUse() {
            if (this.meerkat.warrenPos != null && this.meerkat.wantsToEnterWarren() && this.meerkat.warrenPos.closerToCenterThan(this.meerkat.position(), 2.0D)) {
                MeerkatWarrenBlockEntity warren = this.meerkat.getWarren();
                if (warren != null) {
                    if (!warren.isFull()) {
                        return true;
                    }
                    this.meerkat.warrenPos = null;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            MeerkatWarrenBlockEntity warren = this.meerkat.getWarren();
            if (warren != null) {
                warren.addOccupant(this.meerkat);
            }
        }
    }

    static class LocateWarrenGoal extends Goal {
        private final Meerkat meerkat;

        LocateWarrenGoal(Meerkat meerkat) {
            this.meerkat = meerkat;
        }

        @Override
        public boolean canUse() {
            return this.meerkat.remainingCooldownBeforeLocatingNewWarren == 0 && this.meerkat.warrenPos == null && this.meerkat.wantsToEnterWarren();
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.meerkat.remainingCooldownBeforeLocatingNewWarren = 200;
            List<BlockPos> warrens = this.findNearbyWarrensWithSpace();
            if (!warrens.isEmpty()) {
                for (BlockPos pos : warrens) {
                    if (!this.meerkat.goToWarrenGoal.isTargetBlacklisted(pos)) {
                        this.meerkat.warrenPos = pos;
                        return;
                    }
                }
                this.meerkat.goToWarrenGoal.clearBlacklist();
                this.meerkat.warrenPos = warrens.getFirst();
            }
        }

        private List<BlockPos> findNearbyWarrensWithSpace() {
            BlockPos origin = this.meerkat.blockPosition();
            List<BlockPos> warrens = new ArrayList<>();
            for (int chunkX = SectionPos.blockToSectionCoord(origin.getX() - 20); chunkX <= SectionPos.blockToSectionCoord(origin.getX() + 20); chunkX++) {
                for (int chunkZ = SectionPos.blockToSectionCoord(origin.getZ() - 20); chunkZ <= SectionPos.blockToSectionCoord(origin.getZ() + 20); chunkZ++) {
                    LevelChunk chunk = this.meerkat.level().getChunkSource().getChunkNow(chunkX, chunkZ);
                    if (chunk != null) {
                        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                            if (blockEntity instanceof MeerkatWarrenBlockEntity warren && !warren.isFull() && blockEntity.getBlockPos().closerThan(origin, 20.0D)) {
                                warrens.add(blockEntity.getBlockPos());
                            }
                        }
                    }
                }
            }
            warrens.sort(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
            return warrens;
        }
    }

    static class GoToWarrenGoal extends Goal {
        private final Meerkat meerkat;
        private final List<BlockPos> blacklistedTargets = new ArrayList<>();
        private int travellingTicks;
        private int ticksStuck;
        @Nullable
        private Path lastPath;

        GoToWarrenGoal(Meerkat meerkat) {
            this.meerkat = meerkat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.meerkat.warrenPos != null
                    && this.meerkat.warrenPos.closerThan(this.meerkat.blockPosition(), 48.0D)
                    && this.meerkat.wantsToEnterWarren()
                    && !this.hasReachedTarget(this.meerkat.warrenPos)
                    && this.meerkat.getWarren() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            this.meerkat.getNavigation().stop();
            this.meerkat.getNavigation().resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            BlockPos target = this.meerkat.warrenPos;
            if (target == null) {
                return;
            }
            PathNavigation navigation = this.meerkat.getNavigation();
            this.travellingTicks++;
            if (this.travellingTicks > this.adjustedTickDelay(2400)) {
                this.dropAndBlacklistWarren();
            } else if (!navigation.isInProgress()) {
                if (!target.closerThan(this.meerkat.blockPosition(), 16.0D)) {
                    Vec3 next = DefaultRandomPos.getPosTowards(this.meerkat, 10, 7, Vec3.atBottomCenterOf(target), Mth.PI / 10.0F);
                    if (next != null) {
                        navigation.setMaxVisitedNodesMultiplier(0.5F);
                        navigation.moveTo(next.x, next.y, next.z, 1.0D);
                    }
                } else if (!this.pathfindDirectlyTowards(target)) {
                    this.dropAndBlacklistWarren();
                } else if (this.lastPath != null && navigation.getPath().sameAs(this.lastPath)) {
                    if (++this.ticksStuck > 60) {
                        this.meerkat.dropWarren();
                        this.ticksStuck = 0;
                    }
                } else {
                    this.lastPath = navigation.getPath();
                }
            }
        }

        private boolean pathfindDirectlyTowards(BlockPos target) {
            PathNavigation navigation = this.meerkat.getNavigation();
            navigation.setMaxVisitedNodesMultiplier(10.0F);
            navigation.moveTo(target.getX(), target.getY() + 1, target.getZ(), target.closerThan(this.meerkat.blockPosition(), 3.0D) ? 1 : 2, 1.0D);
            return navigation.getPath() != null && navigation.getPath().canReach();
        }

        private boolean isTargetBlacklisted(BlockPos target) {
            return this.blacklistedTargets.contains(target);
        }

        private void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistWarren() {
            this.blacklistedTargets.add(this.meerkat.warrenPos);
            while (this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.removeFirst();
            }
            this.meerkat.dropWarren();
        }

        private boolean hasReachedTarget(BlockPos target) {
            return target.closerThan(this.meerkat.blockPosition(), 2.0D);
        }
    }

    static class StandWatchGoal extends Goal {
        private final Meerkat meerkat;

        StandWatchGoal(Meerkat meerkat) {
            this.meerkat = meerkat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.meerkat.isLookout() && this.meerkat.onGround();
        }

        @Override
        public void start() {
            this.meerkat.getNavigation().stop();
        }
    }

    static class DarkScoutGoal extends Goal {
        private final Meerkat meerkat;
        private Vec3 spot;
        private Vec3 from;
        private int ticks;
        private boolean reached;

        DarkScoutGoal(Meerkat meerkat) {
            this.meerkat = meerkat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if ((this.meerkat.tickCount + this.meerkat.getId()) % 100 != 0 || !this.meerkat.isScout() || this.meerkat.darkCooldown > 0 || this.meerkat.darkChirpTicks > 0) {
                return false;
            }
            Level level = this.meerkat.level();
            BlockPos here = this.meerkat.blockPosition();
            if (!level.canSeeSky(here) && level.getMaxLocalRawBrightness(here) <= DARK_LIGHT_MAX) {
                this.meerkat.chirpAtDark();
                return false;
            }
            for (int i = 0; i < 14; i++) {
                float angle = this.meerkat.getRandom().nextFloat() * Mth.TWO_PI;
                float radius = 3.0F + this.meerkat.getRandom().nextFloat() * 5.0F;
                BlockPos pos = BlockPos.containing(this.meerkat.getX() + Mth.cos(angle) * radius, this.meerkat.getY() + this.meerkat.getRandom().nextInt(5) - 2, this.meerkat.getZ() + Mth.sin(angle) * radius);
                if (this.isDarkSpot(level, pos)) {
                    this.spot = Vec3.atBottomCenterOf(pos);
                    return true;
                }
            }
            this.meerkat.darkCooldown = DARK_COOLDOWN;
            return false;
        }

        private boolean isDarkSpot(Level level, BlockPos pos) {
            BlockState below = level.getBlockState(pos.below());
            if (!level.getBlockState(pos).isAir() || !level.getBlockState(pos.above()).isAir() || below.isAir() || !below.getFluidState().isEmpty()
                    || level.canSeeSky(pos) || level.getMaxLocalRawBrightness(pos) > DARK_LIGHT_MAX) {
                return false;
            }
            return level.clip(new ClipContext(this.meerkat.getEyePosition(), Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.meerkat)).getType() == HitResult.Type.MISS;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.reached && this.meerkat.isScout() && this.ticks < 400;
        }

        @Override
        public void start() {
            this.ticks = 0;
            this.reached = false;
            this.from = this.meerkat.position();
            this.meerkat.getNavigation().moveTo(this.spot.x, this.spot.y, this.spot.z, 1.1D);
        }

        @Override
        public void tick() {
            this.ticks++;
            if (this.ticks == 60 && this.meerkat.position().distanceToSqr(this.from) < 1.0D) {
                this.ticks = 400;
            } else if (this.meerkat.position().distanceToSqr(this.spot) <= 5.0625D) {
                this.reached = true;
                this.meerkat.getNavigation().stop();
                this.meerkat.chirpAtDark();
            }
        }

        @Override
        public void stop() {
            if (!this.reached) {
                this.meerkat.darkCooldown = DARK_COOLDOWN;
            }
        }
    }
    //endregion
}
