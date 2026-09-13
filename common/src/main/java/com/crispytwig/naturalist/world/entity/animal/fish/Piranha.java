package com.crispytwig.naturalist.world.entity.animal.fish;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.FishSwimTilt;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

@SuppressWarnings("unused")
public class Piranha extends AbstractSchoolingFish implements DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Piranha.class, EntityDataSerializers.STRING);

    @Nullable
    private AbstractSchoolingFish schoolLeader;

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private final AnimationTimer attackAnimTimer = new AnimationTimer(15);

    public final FishSwimTilt swimTilt = new FishSwimTilt();

    public Piranha(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.MOVEMENT_SPEED, 1.2D);
    }

    public static boolean checkPiranhaSpawnRules(EntityType<? extends WaterAnimal> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return pos.getY() <= level.getLevel().getSeaLevel() && level.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/piranha.png");
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
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.PIRANHA_BUCKET.get());
    }
    //endregion

    //region Spawning
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType != EntitySpawnReason.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
        this.goalSelector.addGoal(2, new FollowFlockLeaderGoal(this));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 10));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity, level) -> entity.isInWater()) {
            @Override
            public boolean canUse() {
                return !Piranha.this.isFollower() && super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, 10, true, false, (entity, level) -> entity.is(NaturalistEntityTypeTags.PIRANHA_HOSTILES)) {
            @Override
            public boolean canUse() {
                return !Piranha.this.isFollower() && super.canUse();
            }
        });
    }

    @Override
    public @NotNull AbstractSchoolingFish startFollowing(@NotNull AbstractSchoolingFish leader) {
        this.schoolLeader = leader;
        return super.startFollowing(leader);
    }

    @Override
    public void stopFollowing() {
        super.stopFollowing();
        this.schoolLeader = null;
    }

    @Override
    public int getMaxSchoolSize() {
        return 24;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        boolean hurt = super.hurtServer(level, source, amount);
        if (hurt && !this.level().isClientSide() && source.getEntity() instanceof LivingEntity attacker && attacker != this) {
            this.setTarget(attacker);
            if (this.schoolLeader instanceof Piranha leader && leader.isAlive()) {
                leader.setTarget(attacker);
            }
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
        if (!this.level().isClientSide()) {
            if (this.isFollower() && this.schoolLeader instanceof Piranha leader) {
                LivingEntity leaderTarget = leader.getTarget();
                if (leaderTarget != this.getTarget()) {
                    this.setTarget(leaderTarget);
                }
            }
        }
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return NaturalistSoundEvents.PIRANHA_FLOP.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.PIRANHA_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.PIRANHA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.PIRANHA_DEATH.get();
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            this.swimTilt.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);
        this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimAnimationState.animateWhen(inWater, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
