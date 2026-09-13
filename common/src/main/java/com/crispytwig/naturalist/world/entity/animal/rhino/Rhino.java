package com.crispytwig.naturalist.world.entity.animal.rhino;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Rhino extends NaturalistAnimal implements DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Rhino.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> CHARGE_COOLDOWN_TICKS = SynchedEntityData.defineId(Rhino.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(Rhino.class, EntityDataSerializers.BOOLEAN);

    private int stunnedTick;
    private boolean canBePushed = true;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState footAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState stunnedAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();
    private final AnimationTimer attackAnimTimer = new AnimationTimer(10);

    public Rhino(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.ATTACK_DAMAGE, 10.0D).add(Attributes.ATTACK_KNOCKBACK, 1.5D).add(Attributes.KNOCKBACK_RESISTANCE, 0.75D).add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(CHARGE_COOLDOWN_TICKS, 0);
        builder.define(HAS_TARGET, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/rhino.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public void setChargeCooldownTicks(int ticks) {
        this.entityData.set(CHARGE_COOLDOWN_TICKS, ticks);
    }

    public int getChargeCooldownTicks() {
        return this.entityData.get(CHARGE_COOLDOWN_TICKS);
    }

    public boolean hasChargeCooldown() {
        return this.entityData.get(CHARGE_COOLDOWN_TICKS) > 0;
    }

    public void resetChargeCooldownTicks() {
        this.entityData.set(CHARGE_COOLDOWN_TICKS, 42);
    }

    public void setHasTarget(boolean hasTarget) {
        this.entityData.set(HAS_TARGET, hasTarget);
    }

    public boolean hasTarget() {
        return this.entityData.get(HAS_TARGET);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putInt("StunTick", this.stunnedTick);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.stunnedTick = input.getIntOr("StunTick", 0);
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Rhino baby = NaturalistEntityTypes.RHINO.get().create(serverLevel, EntitySpawnReason.BREEDING);
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
        this.goalSelector.addGoal(1, new RhinoMeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new RhinoPrepareChargeGoal(this));
        this.goalSelector.addGoal(3, new RhinoChargeGoal(this, 2.5F));

        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new RhinoNearestAttackablePlayerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, (entity, level) -> entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARVED_PUMPKIN));
    }

    @Override
    public boolean isPushable() {
        return this.canBePushed;
    }

    @Override
    public void knockback(double strength, double x, double z, @NotNull DamageSource source, float damage, boolean comesFromEffect) {
        super.knockback(NaturalistAnimal.babyKnockbackStrength(this, strength), x, z, source, damage, comesFromEffect);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.stunnedTick > 0;
    }

    @Override
    protected void blockedByItem(@NotNull LivingEntity defender, @NotNull DamageSource source, float damage) {
        this.stunnedTick = 60;
        this.resetChargeCooldownTicks();
        this.getNavigation().stop();
        this.playSound(SoundEvents.RAVAGER_STUNNED, 1.0f, 1.0f);
        this.level().broadcastEntityEvent(this, (byte)39);
        defender.push(this);
        defender.hurtMarked = true;
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isSprinting() ? 1 : 50;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isAlive()) {
            return;
        }
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
        if (this.stunnedTick > 0) {
            --this.stunnedTick;
            this.stunEffect();
        }
    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d = this.getX() - (double)this.getBbWidth() * Math.sin(this.yBodyRot * Mth.DEG_TO_RAD) + (this.random.nextDouble() * 0.6 - 0.3);
            double e = this.getY() + (double)this.getBbHeight() - 0.3;
            double f = this.getZ() + (double)this.getBbWidth() * Math.cos(this.yBodyRot * Mth.DEG_TO_RAD) + (this.random.nextDouble() * 0.6 - 0.3);
            this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.4980392156862745F, 0.5137254901960784F, 0.5725490196078431F), d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 39) {
            this.stunnedTick = 60;
        }
    }

    private boolean isWithinYRange(LivingEntity target) {
        if (target == null) {
            return true;
        }
        return !(Math.abs(target.getY() - this.getY()) < 3);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.RHINO_AMBIENT_BABY.get() : NaturalistSoundEvents.RHINO_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.RHINO_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.RHINO_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    static class RhinoPrepareChargeGoal extends Goal {
        protected final Rhino rhino;

        public RhinoPrepareChargeGoal(Rhino rhino) {
            this.rhino = rhino;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null || !target.isAlive() || this.rhino.stunnedTick > 0 || this.rhino.isWithinYRange(target)) {
                this.rhino.resetChargeCooldownTicks();
                return false;
            }
            return target instanceof Player && rhino.hasChargeCooldown();
        }

        @Override
        public void start() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null) {
                return;
            }
            this.rhino.setHasTarget(true);
            this.rhino.resetChargeCooldownTicks();
            this.rhino.canBePushed = false;
        }

        @Override
        public void stop() {
            this.rhino.setHasTarget(false);
            this.rhino.canBePushed = true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null) {
                return;
            }
            this.rhino.getLookControl().setLookAt(target);
            this.rhino.setChargeCooldownTicks(Math.max(0, this.rhino.getChargeCooldownTicks() - 1));
        }
    }

    static class RhinoChargeGoal extends Goal {
        protected final Rhino mob;
        private final double speedModifier;
        private @Nullable Path path;
        private Vec3 chargeDirection;

        public RhinoChargeGoal(Rhino pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.chargeDirection = Vec3.ZERO;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0) {
                return false;
            }
            this.path = this.mob.getNavigation().createPath(target, 0);
            return target instanceof Player && this.path != null;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0) {
                return false;
            }
            return !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            BlockPos blockPosition = this.mob.blockPosition();
            BlockPos target = Objects.requireNonNull(this.path).getTarget();
            this.chargeDirection = new Vec3(blockPosition.getX() - target.getX(), 0.0, blockPosition.getZ() - target.getZ()).normalize();
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            this.mob.resetChargeCooldownTicks();
            this.mob.getNavigation().stop();

            this.mob.swinging = false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.mob.getLookControl().setLookAt(Vec3.atCenterOf(Objects.requireNonNull(this.path).getTarget()));
            if (this.mob.horizontalCollision && this.mob.onGround()) {
                this.mob.jumpFromGround();
            }
            if (getServerLevel(this.mob).getGameRules().get(GameRules.MOB_GRIEFING)) {
                AABB boundingBox = this.mob.getBoundingBox().inflate(0.2);
                for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(boundingBox.minX), Mth.floor(boundingBox.minY), Mth.floor(boundingBox.minZ), Mth.floor(boundingBox.maxX), Mth.floor(boundingBox.maxY), Mth.floor(boundingBox.maxZ))) {
                    BlockState state = this.mob.level().getBlockState(pos);
                    if (!state.is(NaturalistBlockTags.RHINO_CHARGE_BREAKABLE)) continue;
                    this.mob.level().destroyBlock(pos, true, this.mob);
                }
            }
            if (!this.mob.level().isClientSide()) {
                ((ServerLevel) this.mob.level()).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 5, this.mob.getBbWidth() / 4.0F, 0, this.mob.getBbWidth() / 4.0F, 0.01D);
            }
            if (this.mob.level().getGameTime() % 2L == 0L) {
                this.mob.playSound(SoundEvents.HOGLIN_STEP, 0.5F, this.mob.getVoicePitch());
            }
            this.tryToHurt();
        }

        protected void tryToHurt() {
            ServerLevel serverLevel = getServerLevel(this.mob);
            List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.mob, this.mob.getBoundingBox());
            if (!nearbyEntities.isEmpty()) {
                LivingEntity livingEntity = nearbyEntities.getFirst();
                if (!(livingEntity instanceof Rhino)) {
                    DamageSource damageSource = livingEntity.damageSources().mobAttack(this.mob);
                    float damage = (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    livingEntity.hurtServer(serverLevel, damageSource, damage);
                    float speed = Mth.clamp(this.mob.getSpeed() * 1.65f, 0.2f, 3.0f);
                    float shieldBlockModifier = NaturalistAnimal.isBlockedByItem(livingEntity, damageSource, 1.0F) ? 0.5f : 1.0f;
                    livingEntity.knockback(shieldBlockModifier * speed * 2.0D, this.chargeDirection.x(), this.chargeDirection.z(), damageSource, damage);
                    double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0.0, 0.4f * knockbackResistance, 0.0));
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    if (livingEntity.equals(this.mob.getTarget())) {
                        this.stop();
                    }
                }
            }
        }
    }

    static class RhinoNearestAttackablePlayerTargetGoal extends NearestAttackableTargetGoal<Player> {
        private final Rhino rhino;

        public RhinoNearestAttackablePlayerTargetGoal(Rhino mob) {
            super(mob, Player.class, 10, true, true, (entity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity));
            this.rhino = mob;
        }

        @Override
        public boolean canUse() {
            if (this.rhino.isBaby()) {
                return false;
            }
            if (super.canUse()) {
                if (rhino.isWithinYRange(target)) {
                    return false;
                }
                List<Rhino> nearbyEntities = this.rhino.level().getEntitiesOfClass(Rhino.class, this.rhino.getBoundingBox().inflate(8.0, 4.0, 8.0));
                for (Rhino mob : nearbyEntities) {
                    if (!mob.isBaby()) continue;
                    return true;
                }
            }
            return false;
        }
    }

    static class RhinoMeleeAttackGoal extends MeleeAttackGoal {
        public RhinoMeleeAttackGoal(PathfinderMob pathfinderMob, double speedModifier, boolean followEvenIfNotSeen) {
            super(pathfinderMob, speedModifier, followEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof Player) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.swinging = false;
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
        boolean stunned = this.stunnedTick > 0;
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean stomping = !stunned && !moving && this.hasChargeCooldown() && this.hasTarget();

        this.stunnedAnimationState.animateWhen(stunned, this.tickCount);
        this.footAnimationState.animateWhen(stomping, this.tickCount);

        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.swinging), this.tickCount);

        this.walkAnimationState.animateWhen(!stunned && moving && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(!stunned && moving && this.isSprinting(), this.tickCount);
        this.idleAnimationState.animateWhen(!stunned && !moving && !stomping, this.tickCount);
    }
    //endregion
}
