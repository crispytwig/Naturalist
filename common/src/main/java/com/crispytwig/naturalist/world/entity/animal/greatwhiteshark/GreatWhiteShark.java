package com.crispytwig.naturalist.world.entity.animal.greatwhiteshark;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.HuntingAnimal;
import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.BeachedMob;
import com.crispytwig.naturalist.world.entity.BodyChain;
import com.crispytwig.naturalist.world.entity.MobPart;
import com.crispytwig.naturalist.world.level.MultipartLevel;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.item.component.SwingAnimation;

public class GreatWhiteShark extends Animal implements MultipartMob, HuntingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(GreatWhiteShark.class, EntityDataSerializers.STRING);

    private static final double[] PART_Z = {2.0D, -1.5D, -2.9D};
    private static final float[][] PART_SIZES = {{1.2F, 1.2F}, {1.0F, 1.0F}, {0.8F, 1.2F}};

    private static final float MAX_TILT = 40.0F;
    private static final float ATTACK_CONE_COS = Mth.cos(60.0F * Mth.DEG_TO_RAD);
    private static final Identifier AGGRO_SPEED_BOOST_ID = Naturalist.location("shark_aggro_speed_boost");
    private static final AttributeModifier AGGRO_SPEED_BOOST = new AttributeModifier(AGGRO_SPEED_BOOST_ID, 0.4D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.GREAT_WHITE_SHARK_FOOD_ITEMS);

    private int flopCooldown;
    private int huntingCooldown;

    private final MobPart[] parts;
    private boolean partsRegistered;

    public float xBodyRot;
    public float xBodyRotO;
    private final BodyChain chain = new BodyChain(0.2F,
            new float[]{0.35F, 0.16F, 0.12F},
            new float[]{0.24F, 0.12F, 0.1F},
            4.0F, 25.0F, 0.1F);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimFastAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack SWIM_SOUNDS = AnimationSoundTrack.builder(1.5F, true)
            .at(0.0F, NaturalistSoundEvents.GREAT_WHITE_SHARK_SWIM, 1.0F, 1.0F)
            .build();
    private static final AnimationSoundTrack SWIM_FAST_SOUNDS = AnimationSoundTrack.builder(0.9167F, true)
            .at(0.0F, NaturalistSoundEvents.GREAT_WHITE_SHARK_SWIM_FAST, 1.0F, 1.0F)
            .build();
    private static final AnimationSoundTrack ATTACK_SOUNDS = AnimationSoundTrack.builder(0.7F, false)
            .at(0.0F, NaturalistSoundEvents.GREAT_WHITE_SHARK_ATTACK, 1.0F, 1.0F)
            .build();
    private static final AnimationSoundTrack FLOP_SOUNDS = AnimationSoundTrack.builder(0.5833F, true)
            .at(0.0F, NaturalistSoundEvents.GREAT_WHITE_SHARK_FLOP, 1.0F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.swimAnimationState, SWIM_SOUNDS)
            .add(this.swimFastAnimationState, SWIM_FAST_SOUNDS)
            .add(this.attackAnimationState, ATTACK_SOUNDS)
            .add(this.flopAnimationState, FLOP_SOUNDS);
    private final AnimationTimer attackAnimTimer = new AnimationTimer(14);

    public GreatWhiteShark(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl<>(this, 85, 10, 0.1F, 0.5F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.parts = new MobPart[PART_Z.length];
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i] = new MobPart(this, PART_SIZES[i][0], PART_SIZES[i][1]);
        }
    }

    @Override
    public MobPart[] getMobParts() {
        return this.parts;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/great_white_shark.png");
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
    public void baseTick() {
        int air = this.getAirSupply();
        super.baseTick();
        if (!this.isAlive() || this.isInWater()) {
            this.setAirSupply(this.getMaxAirSupply());
            return;
        }
        this.setAirSupply(air - 1);
        if (this.getAirSupply() == -20) {
            this.setAirSupply(0);
            if (this.level() instanceof ServerLevel serverLevel) {
                this.hurtServer(serverLevel, this.damageSources().drown(), 2.0F);
            }
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        this.saveHuntingCooldown(output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.loadHuntingCooldown(input);
    }

    @Override
    public int getHuntingCooldown() {
        return this.huntingCooldown;
    }

    @Override
    public void setHuntingCooldown(int ticks) {
        this.huntingCooldown = ticks;
    }
    //endregion

    //region Spawning
    @SuppressWarnings("unused")
    public static boolean checkGreatWhiteSharkSpawnRules(EntityType<GreatWhiteShark> entityType, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader level) {
        return level.isUnobstructed(this);
    }

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
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SharkAttackGoal(this));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 10));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false, false,
                (entity, level) -> this.canHunt() && entity.is(NaturalistEntityTypeTags.GREAT_WHITE_SHARK_HOSTILES) && entity.isInWater()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false,
                (player, level) -> this.canHunt() && player.isInWater() && this.getLightLevelDependentMagicValue() < 0.5F));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        super.setTarget(target);
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
        if (FOOD_ITEMS.test(stack) && this.getHealth() < this.getMaxHealth()) {
            this.heal(2.0F);
            this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
            stack.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        if (this.parts != null) {
            MobPart.assignIds(this.parts, id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.partsRegistered && this.level() instanceof MultipartLevel multipart) {
            MobPart.registerAll(multipart, this.parts);
            this.partsRegistered = true;
        }
        this.positionParts();
        MobPart.pushEntities(this, this.parts);
        if (!this.level().isClientSide()) {
            if (MobPart.resolveBodyCollisions(this, this.parts)) {
                this.positionParts();
            }
        } else {
            this.setupAnimationStates();
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean aggressive = this.isAggressive();
        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.isSwinging()), this.tickCount);
        this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimFastAnimationState.animateWhen(inWater && moving && aggressive, this.tickCount);
        this.swimAnimationState.animateWhen(inWater && moving && !aggressive, this.tickCount);
        this.idleAnimationState.animateWhen(inWater && !moving, this.tickCount);
    }

    @Override
    public void remove(Entity.@NotNull RemovalReason reason) {
        if (this.partsRegistered && this.level() instanceof MultipartLevel multipart) {
            MobPart.unregisterAll(multipart, this.parts);
            this.partsRegistered = false;
        }
        super.remove(reason);
    }

    private void positionParts() {
        float yaw = this.chain.getRenderYaw() * Mth.DEG_TO_RAD;
        float pitch = this.xBodyRot * Mth.DEG_TO_RAD;
        float sin = Mth.sin(yaw);
        float cos = Mth.cos(yaw);
        float cosPitch = Mth.cos(pitch);
        float sinPitch = -Mth.sin(pitch);
        for (int i = 0; i < this.parts.length; i++) {
            MobPart part = this.parts[i];
            double z = PART_Z[i];
            double px = this.getX() - sin * z * cosPitch;
            double pz = this.getZ() + cos * z * cosPitch;
            double py = this.getY() + (this.getBbHeight() - part.getBbHeight()) * 0.5D + sinPitch * z;
            part.setOldPosAndRot();
            part.setPos(px, py, pz);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.xBodyRotO = this.xBodyRot;
        float targetPitch = 0.0F;
        Vec3 movement = this.getDeltaMovement();
        if (this.isInWater() && movement.horizontalDistance() > 0.01D) {
            targetPitch = -((float) (Mth.atan2(movement.y, movement.horizontalDistance()) * Mth.RAD_TO_DEG));
            targetPitch = Mth.clamp(targetPitch, -MAX_TILT, MAX_TILT);
        }
        this.xBodyRot += (targetPitch - this.xBodyRot) * 0.1F;
        this.chain.tick(this.yBodyRot, this.xBodyRot, targetPitch);
        if (!this.level().isClientSide()) {
            this.tickHuntingCooldown();
            this.flopCooldown = BeachedMob.tickFlopping(this, this.flopCooldown, null);
        }
    }

    @Override
    protected @NotNull AABB getAttackBoundingBox(double horizontalExpansion) {
        return super.getAttackBoundingBox(horizontalExpansion).inflate(0.9D, 0.5D, 0.9D);
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        boolean aggressive = this.getTarget() != null;
        this.setAggressive(aggressive);
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            if (aggressive && !speed.hasModifier(AGGRO_SPEED_BOOST_ID)) {
                speed.addTransientModifier(AGGRO_SPEED_BOOST);
            } else if (!aggressive && speed.hasModifier(AGGRO_SPEED_BOOST_ID)) {
                speed.removeModifier(AGGRO_SPEED_BOOST_ID);
            }
        }
    }

    public boolean isFacing(LivingEntity target) {
        Vec3 toTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D)
                .subtract(this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ());
        if (toTarget.lengthSqr() < 1.0E-4D) {
            return true;
        }
        return Vec3.directionFromRotation(this.xBodyRot, this.chain.getRenderYaw()).dot(toTarget.normalize()) >= ATTACK_CONE_COS;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.GREAT_WHITE_SHARK_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.GREAT_WHITE_SHARK_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.GREAT_WHITE_SHARK_DEATH.get();
    }

    static class SharkAttackGoal extends Goal {
        private static final double MIN_CHARGE_DIST_SQR = 25.0D;
        private static final double FUMBLE_DIST_SQR = 6.25D;
        private static final double RETREAT_SPEED = 1.2D;
        private static final int RETREAT_TIME_LIMIT = 60;
        private static final int FUMBLE_TIME_LIMIT = 15;
        private static final int ATTACK_COOLDOWN = 20;

        private final GreatWhiteShark shark;
        private boolean charging;
        private int retreatTicks;
        private int fumbleTicks;
        private int pathRecalcTicks;
        private int attackCooldown;

        SharkAttackGoal(GreatWhiteShark shark) {
            this.shark = shark;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.shark.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            LivingEntity target = this.shark.getTarget();
            if (target == null || this.shark.distanceToSqr(target) >= MIN_CHARGE_DIST_SQR) {
                this.startCharge();
            } else {
                this.startRetreat(target);
            }
        }

        @Override
        public void stop() {
            this.charging = false;
            this.shark.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.shark.getTarget();
            if (target == null) {
                return;
            }
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
            if (this.charging) {
                this.tickCharge(target);
            } else {
                this.tickRetreat(target);
            }
        }

        private void tickCharge(LivingEntity target) {
            this.shark.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--this.pathRecalcTicks <= 0) {
                this.pathRecalcTicks = 4;
                this.shark.getNavigation().moveTo(target, 2.25);
            }
            if (this.attackCooldown <= 0 && this.shark.isWithinMeleeAttackRange(target) && this.shark.getSensing().hasLineOfSight(target) && this.shark.isFacing(target)) {
                this.shark.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);
                this.shark.doHurtTarget(getServerLevel(this.shark), target);
                this.attackCooldown = ATTACK_COOLDOWN;
                this.startRetreat(target);
            } else if (this.shark.distanceToSqr(target) < FUMBLE_DIST_SQR) {
                if (++this.fumbleTicks > FUMBLE_TIME_LIMIT) {
                    this.startRetreat(target);
                }
            } else {
                this.fumbleTicks = 0;
            }
        }

        private void tickRetreat(LivingEntity target) {
            this.retreatTicks++;
            if (this.shark.distanceToSqr(target) >= MIN_CHARGE_DIST_SQR
                    || this.shark.getNavigation().isDone()
                    || this.retreatTicks > RETREAT_TIME_LIMIT) {
                this.startCharge();
            }
        }

        private void startCharge() {
            this.charging = true;
            this.fumbleTicks = 0;
            this.pathRecalcTicks = 0;
        }

        private void startRetreat(LivingEntity target) {
            Vec3 retreatPos = DefaultRandomPos.getPosAway(this.shark, 10, 4, target.position());
            if (retreatPos == null) {
                this.startCharge();
                return;
            }
            this.charging = false;
            this.retreatTicks = 0;
            this.shark.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, RETREAT_SPEED);
        }
    }
    //endregion

    //region Animation
    public float getXBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.xBodyRotO, this.xBodyRot);
    }

    public float getRenderYaw(float partialTick) {
        return this.chain.getRenderYaw(partialTick);
    }

    public float getZBodyRot(float partialTick) {
        return this.chain.getRoll(partialTick);
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        return this.chain.getSegmentYawOffset(index, partialTick);
    }

    public float getSegmentPitchOffset(int index, float partialTick) {
        return this.chain.getSegmentPitchOffset(index, partialTick, this.getXBodyRot(partialTick));
    }
    //endregion
}
