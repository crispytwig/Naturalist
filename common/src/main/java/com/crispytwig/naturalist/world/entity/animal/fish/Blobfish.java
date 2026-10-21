package com.crispytwig.naturalist.world.entity.animal.fish;

import com.crispytwig.naturalist.world.item.ItemHelper;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;

import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.BlobfishStayDeepGoal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.FishSwimTilt;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

@SuppressWarnings("unused")
public class Blobfish extends AbstractFish implements DataDrivenVariantAnimal {
    //region Data
    private static final int CONVERSION_DURATION = 100;

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Blobfish.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_GRAY = SynchedEntityData.defineId(Blobfish.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING = SynchedEntityData.defineId(Blobfish.class, EntityDataSerializers.BOOLEAN);

    private int conversionTime;

    public final FishSwimTilt swimTilt = new FishSwimTilt();

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();

    private static final AnimationSoundTrack SWIM_SOUNDS = AnimationSoundTrack.builder(2.0F, true)
            .at(0.05F, NaturalistSoundEvents.BLOBFISH_SWIM, 0.3F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.swimAnimationState, SWIM_SOUNDS);

    public Blobfish(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    public static boolean checkBlobfishSpawnRules(EntityType<? extends WaterAnimal> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, level, reason, pos, random)
                || (pos.getY() <= level.getLevel().getSeaLevel() - 33 && level.getFluidState(pos).is(FluidTags.WATER));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
        this.entityData.define(DATA_GRAY, true);
        this.entityData.define(DATA_CONVERTING, false);
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/blobfish/pink.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public int getDeepY() {
        return this.level().getSeaLevel() - 33;
    }

    public boolean isGray() {
        return this.entityData.get(DATA_GRAY);
    }

    public void setGray(boolean gray) {
        this.entityData.set(DATA_GRAY, gray);
    }

    public boolean isConverting() {
        return this.entityData.get(DATA_CONVERTING);
    }

    private boolean wantsGray() {
        return this.isInWaterOrBubble() && this.getY() <= this.getDeepY();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        compound.putBoolean("Gray", this.isGray());
        compound.putInt("ConversionTime", this.conversionTime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.setGray(compound.getBoolean("Gray"));
        this.conversionTime = compound.getInt("ConversionTime");
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.BLOBFISH_BUCKET.get());
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        ItemHelper.updateTag(stack, this::saveVariant);
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        this.loadVariant(tag);
        this.setGray(false);
    }
    //endregion

    //region Spawning
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (reason != MobSpawnType.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        this.setGray(this.wantsGray());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Axolotl.class, 6.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(2, new BlobfishStayDeepGoal(this, 1.0D, 20));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                entity -> entity instanceof Crab || entity instanceof Snail));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.tickConversion();
        }
    }

    private void tickConversion() {
        boolean wantGray = this.wantsGray();
        if (wantGray == this.isGray()) {
            this.conversionTime = 0;
        } else if (++this.conversionTime >= CONVERSION_DURATION) {
            this.setGray(wantGray);
            this.conversionTime = 0;
            this.onTransform();
        }
        this.entityData.set(DATA_CONVERTING, this.conversionTime > 0);
    }

    private void onTransform() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(NaturalistRegistry.BLOBFISH.get()));
            for (int i = 0; i < 16; i++) {
                serverLevel.sendParticles(particle, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 1, 0.0, 0.0, 0.0, 0.05);
            }
        }
        this.playSound(NaturalistSoundEvents.BLOBFISH_HURT.get(), this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return NaturalistSoundEvents.BLOBFISH_FLOP.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.BLOBFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BLOBFISH_DEATH.get();
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
        boolean idle = this.onGround() && !NaturalistAnimal.isVisiblyMoving(this);
        this.idleAnimationState.animateWhen(idle, this.tickCount);
        this.swimAnimationState.animateWhen(!idle, this.tickCount);
    }
    //endregion

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
}
