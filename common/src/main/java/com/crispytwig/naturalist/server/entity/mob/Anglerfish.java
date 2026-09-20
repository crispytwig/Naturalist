package com.crispytwig.naturalist.server.entity.mob;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.registry.NaturalistTags;
import com.crispytwig.naturalist.server.entity.base.HuntingAnimal;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.server.entity.variant.MobVariant;
import com.crispytwig.naturalist.server.entity.variant.MobVariantUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.server.entity.util.AnimationTimer;
import com.crispytwig.naturalist.server.entity.util.AnimationSoundPlayer;
import com.crispytwig.naturalist.server.entity.util.AnimationSoundTrack;
import com.crispytwig.naturalist.server.entity.util.FishSwimTilt;
import com.crispytwig.naturalist.server.entity.util.SmoothAnimationState;

@SuppressWarnings("unused")
public class Anglerfish extends AbstractFish implements HuntingAnimal, DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"red", "glow"};

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Anglerfish.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_HAS_TARGET = SynchedEntityData.defineId(Anglerfish.class, EntityDataSerializers.BOOLEAN);

    private int huntingCooldown;

    public final FishSwimTilt swimTilt = new FishSwimTilt();

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimFastAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack SWIM_SOUNDS = AnimationSoundTrack.builder(1.0F, true)
            .at(0.05F, NaturalistSoundEvents.ANGLERFISH_SWIM, 0.3F, 1.0F)
            .build();
    private static final AnimationSoundTrack SWIM_FAST_SOUNDS = AnimationSoundTrack.builder(0.5F, true)
            .at(0.05F, NaturalistSoundEvents.ANGLERFISH_SWIM, 0.3F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.swimAnimationState, SWIM_SOUNDS)
            .add(this.swimFastAnimationState, SWIM_FAST_SOUNDS);
    private final AnimationTimer attackAnimTimer = new AnimationTimer(7);

    public Anglerfish(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.MOVEMENT_SPEED, 1.2D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, NaturalistMobVariants.ANGLERFISH_RED.location().toString());
        builder.define(DATA_HAS_TARGET, false);
    }

    public boolean hasSwimTarget() {
        return this.entityData.get(DATA_HAS_TARGET);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.ANGLERFISH_RED;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/anglerfish/red.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public boolean isGlowing() {
        return NaturalistMobVariants.ANGLERFISH_GLOW.location().equals(this.getVariantLocation());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        this.saveHuntingCooldown(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.loadHuntingCooldown(compound);
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
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        boolean result = super.killedEntity(level, killed);
        if (result) {
            this.startHuntingCooldown();
        }
        return result;
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, this::saveVariant);
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(custom);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        this.loadVariant(tag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.ANGLERFISH_BUCKET.get());
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return NaturalistSoundEvents.CATFISH_FLOP.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.ANGLERFISH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.ANGLERFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.ANGLERFISH_DEATH.get();
    }
    //endregion

    //region Spawning
    public static boolean checkAnglerfishSpawnRules(EntityType<? extends WaterAnimal> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, level, reason, pos, random) || isGlowSquidWater(level, pos);
    }

    private static boolean isGlowSquidWater(ServerLevelAccessor level, BlockPos pos) {
        return pos.getY() <= level.getLevel().getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getBlockState(pos).is(Blocks.WATER);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        if (reason != MobSpawnType.BUCKET) {
            if (isGlowSquidWater(level, this.blockPosition())) {
                MobVariantUtil.byKey(level.registryAccess(), NaturalistMobVariants.ANGLERFISH_VARIANT, NaturalistMobVariants.ANGLERFISH_GLOW)
                        .ifPresent(this::setVariant);
            } else {
                this.selectVariantForSpawn(level);
            }
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2.2D, true) {
            @Override
            protected void checkAndPerformAttack(@NotNull LivingEntity target) {
                double reach = this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
                if (this.isTimeToAttack() && this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ()) <= reach) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                }
            }
        });
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 20));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, 10, true, false,
                entity -> this.canHunt() && entity.getType().is(NaturalistTags.EntityTypes.ANGLERFISH_HOSTILES)));
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide && source.getEntity() instanceof LivingEntity attacker && attacker != this) {
            this.setTarget(attacker);
        }
        return hurt;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_HAS_TARGET, this.getTarget() != null);
            this.tickHuntingCooldown();
        }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
            this.swimTilt.tick(this);
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        boolean hasTarget = this.hasSwimTarget();
        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);
        this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimFastAnimationState.animateWhen(inWater && hasTarget, this.tickCount);
        this.swimAnimationState.animateWhen(inWater && !hasTarget, this.tickCount);
    }
    //endregion

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
