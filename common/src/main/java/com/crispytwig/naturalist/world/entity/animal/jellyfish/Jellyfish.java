package com.crispytwig.naturalist.world.entity.animal.jellyfish;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.List;

@SuppressWarnings("unused")
public class Jellyfish extends AbstractFish implements DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"white", "orange", "pink", "blue", "green"};

    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("jellyfish"), "white");

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Jellyfish.class, EntityDataSerializers.STRING);

    private static final float STING_DAMAGE = 2.0F;
    private static final int PULSE_INTERVAL = 20;
    private static final double PULSE_FORCE = 0.18D;
    private int pulseCooldown;

    public float xBodyRot;
    public float xBodyRotO;
    private Vec3 lastMoveDir = Vec3.ZERO;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState landAnimationState = new SmoothAnimationState();

    private static final AnimationSoundTrack SWIM_SOUNDS = AnimationSoundTrack.builder(2.0F, true)
            .at(0.0F, NaturalistSoundEvents.JELLYFISH_SWIM, 0.4F, 1.0F)
            .at(1.0F, NaturalistSoundEvents.JELLYFISH_SWIM, 0.4F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.swimAnimationState, SWIM_SOUNDS);

    public Jellyfish(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, DEFAULT_VARIANT.identifier().toString());
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
        return Naturalist.location("textures/entity/jellyfish/white.png");
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
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
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
        return new ItemStack(NaturalistRegistry.JELLYFISH_BUCKET.get());
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.JELLYFISH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.JELLYFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.JELLYFISH_DEATH.get();
    }
    //endregion

    //region Spawning
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (reason != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.0D, 20));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive()) {
            List<LivingEntity> touching = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D),
                    entity -> entity.isAlive() && !(entity instanceof Jellyfish));
            for (LivingEntity target : touching) {
                target.hurtServer(serverLevel, this.damageSources().mobAttack(this), STING_DAMAGE);
            }

            if (this.pulseCooldown > 0) {
                this.pulseCooldown--;
            }
            if (this.isInWater() && this.pulseCooldown <= 0 && this.getMoveControl().hasWanted()) {
                Vec3 toTarget = new Vec3(this.getMoveControl().getWantedX() - this.getX(),
                        this.getMoveControl().getWantedY() - this.getY(),
                        this.getMoveControl().getWantedZ() - this.getZ());
                if (toTarget.lengthSqr() > 1.0E-4) {
                    this.setDeltaMovement(this.getDeltaMovement().add(toTarget.normalize().scale(PULSE_FORCE)));
                }
                this.pulseCooldown = PULSE_INTERVAL;
            }
        }

        this.xBodyRotO = this.xBodyRot;
        float target = 0.0F;
        if (this.isInWater()) {
            Vec3 movement = this.getDeltaMovement();
            if (movement.lengthSqr() > 1.0E-6) {
                this.lastMoveDir = movement;
            }
            target = -((float) Mth.atan2(this.lastMoveDir.horizontalDistance(), this.lastMoveDir.y)) * (180.0F / (float) Math.PI);
        }
        this.xBodyRot += (target - this.xBodyRot) * 0.1F;
    }

    public float getXBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.xBodyRotO, this.xBodyRot);
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
        boolean inWater = this.isInWater();
        boolean moving = this.getDeltaMovement().lengthSqr() > 1.0E-5;
        this.landAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimAnimationState.animateWhen(inWater && moving, this.tickCount);
        this.idleAnimationState.animateWhen(inWater && !moving, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
