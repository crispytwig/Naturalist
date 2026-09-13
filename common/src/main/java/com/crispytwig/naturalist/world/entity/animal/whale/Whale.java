package com.crispytwig.naturalist.world.entity.animal.whale;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.WhaleDiveGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.WhaleSeekDeeperWaterGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.WhaleSurfaceGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.WhaleSwimGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.BeachedMob;
import com.crispytwig.naturalist.world.entity.BodyChain;
import com.crispytwig.naturalist.world.entity.MobPart;
import com.crispytwig.naturalist.world.level.MultipartLevel;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;

@SuppressWarnings("unused")
public class Whale extends Animal implements MultipartMob, DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Whale.class, EntityDataSerializers.STRING);

    private static final double[] PART_Z = {4.0D, 1.5D, -1.0D, -3.5D, -5.3D};
    private static final float[][] PART_SIZES = {{2.2F, 2.2F}, {3.0F, 2.5F}, {3.0F, 2.5F}, {2.2F, 2.0F}, {1.4F, 1.2F}};

    private static final float MAX_TILT = 40.0F;
    private static final float BREACH_TILT = 60.0F;
    private static final int BODY_PUSH_BAIL = 20;

    private int blowholeCooldown;
    private boolean diving;
    private int bodyPushTicks;
    private int flopCooldown;

    private final MobPart[] parts;
    private boolean partsRegistered;

    public float xBodyRot;
    public float xBodyRotO;
    private float finLag;
    private float finLagO;
    private float frontDroop;
    private float frontDroopO;
    private float backDroop;
    private float backDroopO;
    private final BodyChain chain = new BodyChain(0.15F,
            new float[]{0.35F, 0.14F, 0.11F, 0.09F},
            new float[]{0.24F, 0.12F, 0.1F, 0.08F},
            5.0F, 30.0F, 0.06F);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();

    public Whale(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new WhaleMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 6);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.parts = new MobPart[PART_Z.length];
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i] = new MobPart(this, PART_SIZES[i][0], PART_SIZES[i][1]);
        }
    }

    @Override
    public MobPart[] getMobParts() {
        return this.parts;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/whale/whale.png");
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
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide() && this.isAlive() && this.isInWater()) {
            this.setAirSupply(this.getMaxAirSupply());
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
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (source.getEntity() instanceof Jellyfish || source.getEntity() instanceof Pufferfish) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.WHALE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.WHALE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return NaturalistSoundEvents.WHALE_DEATH.get();
    }
    //endregion

    //region Spawning
    public static boolean checkWhaleSpawnRules(EntityType<Whale> type, ServerLevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above(1)).is(FluidTags.WATER)
                && level.getFluidState(pos.above(2)).is(FluidTags.WATER);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        AgeableMobGroupData groupData;
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(false);
        }
        if ((groupData = (AgeableMobGroupData) spawnData).getGroupSize() > 0) {
            this.setAge(-24000);
        }
        groupData.increaseGroupSizeByOne();
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.COOKED_COD)
                || stack.is(Items.SALMON) || stack.is(Items.COOKED_SALMON)
                || stack.is(NaturalistRegistry.CRAB_MEAT.get()) || stack.is(NaturalistRegistry.COOKED_CRAB_MEAT.get());
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return this.isInWater() && otherAnimal.isInWater() && super.canMate(otherAnimal);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Whale baby = NaturalistEntityTypes.WHALE.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(1, new WhaleSeekDeeperWaterGoal(this));
        this.goalSelector.addGoal(2, new WhaleSurfaceGoal(this));
        this.goalSelector.addGoal(3, new WhaleDiveGoal(this));
        this.goalSelector.addGoal(4, new WhaleSwimGoal(this, 1.0D, 2));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            if (travelVector.y != 0.0D) {
                Vec3 dm = this.getDeltaMovement();
                this.setDeltaMovement(dm.x, Mth.lerp(0.25D, dm.y, travelVector.y * this.getSpeed() * 10.0D), dm.z);
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
            if (!this.isInWater() && this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 1.0D, 0.3D));
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide()) {
                this.usePlayerItem(player, hand, stack);
                this.heal(4.0F);
                this.playSound(NaturalistAnimal.eatingSound(stack), 1.0F, 1.0F);
            }
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
            this.resolveBodyCollisions();
        } else {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimAnimationState.animateWhen(inWater && moving, this.tickCount);
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
        float scale = this.isBaby() ? 0.5F : 1.0F;
        for (int i = 0; i < this.parts.length; i++) {
            MobPart part = this.parts[i];
            part.updateScale(scale);
            double z = PART_Z[i] * scale;
            double px = this.getX() - sin * z * cosPitch;
            double pz = this.getZ() + cos * z * cosPitch;
            double py = this.getY() + (this.getBbHeight() - part.getBbHeight()) * 0.5D + sinPitch * z;
            double zRaw = PART_Z[i];
            double droopRatio = zRaw / (zRaw > 0.0D ? PART_Z[0] : PART_Z[PART_Z.length - 1]);
            py -= (zRaw > 0.0D ? this.frontDroop : this.backDroop) * droopRatio * droopRatio * 0.9D * scale;
            part.setOldPosAndRot();
            part.setPos(px, py, pz);
        }
    }

    private void resolveBodyCollisions() {
        if (MobPart.resolveBodyCollisions(this, this.parts)) {
            this.bodyPushTicks = Math.min(this.bodyPushTicks + 2, 2 * BODY_PUSH_BAIL);
            this.positionParts();
        } else if (this.bodyPushTicks > 0) {
            this.bodyPushTicks--;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.xBodyRotO = this.xBodyRot;
        this.finLagO = this.finLag;
        this.frontDroopO = this.frontDroop;
        this.backDroopO = this.backDroop;

        float targetPitch = 0.0F;
        Vec3 movement = this.getDeltaMovement();
        boolean grounded = this.onGround() || this.verticalCollisionBelow;
        if (!grounded && movement.horizontalDistanceSqr() > 1.0E-7
                && (this.isInWater() || movement.lengthSqr() > 0.03D)) {
            targetPitch = -((float) (Mth.atan2(movement.y, movement.horizontalDistance()) * Mth.RAD_TO_DEG));
            float maxTilt = this.isInWater() ? MAX_TILT : BREACH_TILT;
            targetPitch = Mth.clamp(targetPitch, -maxTilt, maxTilt);
        }
        this.xBodyRot += (targetPitch - this.xBodyRot) * (grounded ? 0.25F : 0.07F);

        this.chain.tick(this.yBodyRot, this.xBodyRot, targetPitch);

        this.finLag += (this.xBodyRot - this.finLag) * 0.1F;

        this.frontDroop += ((this.isInWater() && this.isPartAboveWater(this.parts[0]) ? 1.0F : 0.0F) - this.frontDroop) * 0.08F;
        this.backDroop += ((this.isInWater() && this.isPartAboveWater(this.parts[3]) ? 1.0F : 0.0F) - this.backDroop) * 0.08F;

        if (!this.level().isClientSide()) {
            if (this.blowholeCooldown > 0) {
                this.blowholeCooldown--;
            } else if (this.canSpray()) {
                this.spray();
                this.blowholeCooldown = 160 + this.random.nextInt(160);
            }
            this.flopCooldown = BeachedMob.tickFlopping(this, this.flopCooldown, SoundEvents.GUARDIAN_FLOP);
        }
    }

    public boolean canSpray() {
        return this.isInWater() && this.level().getFluidState(BlockPos.containing(this.getX(), this.getY() + this.getBbHeight() + 1.0D, this.getZ())).isEmpty();
    }

    private void spray() {
        Vec3 forward = this.getForward();
        double reach = this.isBaby() ? 1.0D : 2.0D;
        double sx = this.getX() + forward.x * reach;
        double sy = this.getY() + this.getBbHeight() * 0.9D;
        double sz = this.getZ() + forward.z * reach;
        ServerLevel serverLevel = (ServerLevel) this.level();
        serverLevel.sendParticles(ParticleTypes.CLOUD, sx, sy, sz, 14, 0.15D, 0.35D, 0.15D, 0.12D);
        serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, sx, sy, sz, 8, 0.15D, 0.25D, 0.15D, 0.0D);
        this.playSound(NaturalistSoundEvents.WHALE_BLOWHOLE.get(), 1.2F, 1.0F);
    }

    public float getXBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.xBodyRotO, this.xBodyRot);
    }

    public float getRenderYaw(float partialTick) {
        return this.chain.getRenderYaw(partialTick);
    }

    public float getZBodyRot(float partialTick) {
        return this.chain.getRoll(partialTick);
    }

    public float getFinLag(float partialTick) {
        return Mth.lerp(partialTick, this.finLagO, this.finLag);
    }

    public float getFrontDroop(float partialTick) {
        return Mth.lerp(partialTick, this.frontDroopO, this.frontDroop);
    }

    public float getBackDroop(float partialTick) {
        return Mth.lerp(partialTick, this.backDroopO, this.backDroop);
    }

    public boolean isWaterAt(BlockPos pos) {
        return this.level().getFluidState(pos).is(FluidTags.WATER);
    }

    public boolean isDiving() {
        return this.diving;
    }

    public void setDiving(boolean diving) {
        this.diving = diving;
    }

    public boolean isGrindingTerrain() {
        return this.bodyPushTicks >= BODY_PUSH_BAIL;
    }

    private boolean isPartAboveWater(MobPart part) {
        return !this.level().isWaterAt(BlockPos.containing(part.getX(), part.getY() + part.getBbHeight() * 0.5D, part.getZ()));
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        return this.chain.getSegmentYawOffset(index, partialTick);
    }

    public float getSegmentPitchOffset(int index, float partialTick) {
        return this.chain.getSegmentPitchOffset(index, partialTick, this.getXBodyRot(partialTick));
    }

    static class WhaleMoveControl extends MoveControl {
        private final int maxTurnX;

        WhaleMoveControl(Whale whale) {
            super(whale);
            this.maxTurnX = 45;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setSpeed(0.0F);
                this.mob.setXxa(0.0F);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
                return;
            }
            this.operation = Operation.WAIT;
            double dx = this.wantedX - this.mob.getX();
            double dy = this.wantedY - this.mob.getY();
            double dz = this.wantedZ - this.mob.getZ();
            if (dx * dx + dy * dy + dz * dz < 2.5000003E-7D) {
                this.mob.setZza(0.0F);
                return;
            }
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F, 4));
            this.mob.yBodyRot = this.mob.getYRot();
            this.mob.yHeadRot = this.mob.getYRot();
            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            if (this.mob.isInWater()) {
                this.mob.setSpeed(speed * 0.02F);
                double horizontal = Math.sqrt(dx * dx + dz * dz);
                float pitch = 0.0F;
                if (Math.abs(dy) > 1.0E-5D || Math.abs(horizontal) > 1.0E-5D) {
                    pitch = (float) -(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG);
                    pitch = Mth.clamp(Mth.wrapDegrees(pitch), -this.maxTurnX, this.maxTurnX);
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), pitch, 5.0F));
                }
                this.mob.zza = Mth.cos(pitch * Mth.DEG_TO_RAD) * speed;
                this.mob.yya = -Mth.sin(pitch * Mth.DEG_TO_RAD) * speed;
            } else {
                this.mob.setSpeed(speed * 0.1F);
            }
        }
    }
    //endregion
}
