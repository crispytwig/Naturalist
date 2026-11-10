package com.crispytwig.naturalist.common.entity.whaleshark;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.common.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.common.entity.BucketableAquaticAnimal;
import com.crispytwig.naturalist.common.entity.NaturalistAnimal;
import com.crispytwig.naturalist.common.entity.SmoothAnimationState;
import com.crispytwig.naturalist.common.entity.ai.goal.FollowNearestGoal;
import com.crispytwig.naturalist.common.entity.ai.goal.GoToWaterGoal;
import com.crispytwig.naturalist.common.entity.ai.goal.RangedSwimGoal;
import com.crispytwig.naturalist.common.registry.NaturalistRegistry;
import com.crispytwig.naturalist.common.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.common.tags.NaturalistItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Prediction;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

@SuppressWarnings("unused")
public class WhaleShark extends BucketableAquaticAnimal {
    //region Data
    private static final EntityDataAccessor<Boolean> MOUTH_INKED = SynchedEntityData.defineId(WhaleShark.class, EntityDataSerializers.BOOLEAN);

    private static final int TIMER_UNARMED = -1;

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final AquaticBodyTilt bodyTilt = new AquaticBodyTilt(new AquaticBodyTilt.Settings(
            new AquaticBodyTilt.Speed(2.5F, 0.3F, 1.5F, 6.0F, 0.1F, 1.0F),
            new AquaticBodyTilt.Pitching(24.0F, 45.0F, 2.0F, 1.5F, 18.0F, 0.7F),
            new AquaticBodyTilt.Turning(2.0F, 1.0F, 20.0F, 1.0F, 25.0F),
            new AquaticBodyTilt.Segment(0.5F, 4.0F),
            new AquaticBodyTilt.Segment(0.25F, 2.5F),
            new AquaticBodyTilt.Segment(0.8F, 1.2F),
            new AquaticBodyTilt.Segment(0.5F, 0.7F),
            0.5F, 0.4F, 0.9F));

    private int inkTimer = TIMER_UNARMED;
    @Nullable
    private BlockPos monumentTarget;
    private int seekTicks;
    private float swimAmplitude = 0.35F;
    private float swimAmplitudeO = 0.35F;
    private boolean mouthOpen;

    public WhaleShark(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl<>(this, 85, 5, 0.5F, 0.36F, false);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.14D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOUTH_INKED, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/whale_shark/whale_shark.png");
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.4F : 1.0F;
    }

    @Override
    protected float getBabySpawnChance() {
        return 0.1F;
    }

    @Override
    protected float getFoodHealAmount(@NotNull ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        return stack.is(NaturalistRegistry.CRAB_MEAT.get()) || food != null && food.nutrition() >= 5 ? 4.0F : 2.0F;
    }

    @Override
    protected boolean canBeBucketed() {
        return this.isBaby();
    }

    public boolean isMouthInked() {
        return this.entityData.get(MOUTH_INKED);
    }

    private void setMouthInked(boolean inked) {
        this.entityData.set(MOUTH_INKED, inked);
    }

    public boolean isMouthOpen() {
        return this.mouthOpen;
    }

    public float getSwimAmplitude(float partialTick) {
        return Mth.lerp(partialTick, this.swimAmplitudeO, this.swimAmplitude);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("MouthInked", this.isMouthInked());
        output.putInt("InkTimer", this.inkTimer);
        output.storeNullable("MonumentTarget", BlockPos.CODEC, this.monumentTarget);
        output.putInt("MonumentSeekTicks", this.seekTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setMouthInked(input.getBooleanOr("MouthInked", false));
        this.inkTimer = input.getIntOr("InkTimer", TIMER_UNARMED);
        this.monumentTarget = input.read("MonumentTarget", BlockPos.CODEC).orElse(null);
        this.seekTicks = input.getIntOr("MonumentSeekTicks", 0);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.WHALE_SHARK_BUCKET.get());
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.WHALE_SHARK_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.WHALE_SHARK_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return NaturalistSoundEvents.WHALE_SHARK_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? 2.0F : 0.95F + this.random.nextFloat() * 0.1F;
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(NaturalistItemTags.WHALE_SHARK_FOOD_ITEMS);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (spawnType != EntitySpawnReason.BUCKET && !this.isBaby() && this.random.nextFloat() < 0.25F) {
            this.setMouthInked(true);
        }
        return data;
    }
    //endregion

    //region Behavior
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new GoToWaterGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SeekMonumentGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, this::isFood, false));
        this.goalSelector.addGoal(2, new FollowNearestGoal<>(this, WhaleShark.class, other -> this.isBaby() && !other.isBaby(), 24.0D, 3.0D, 1.1D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RangedSwimGoal(this, 40, 9));
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
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return 0.0F;
    }

    private boolean swimTowards(BlockPos target) {
        Vec3 goal = Vec3.atBottomCenterOf(target);
        Vec3 next = this.findWaterPosTowards(goal, 16, 7, Mth.PI / 8.0F);
        if (next == null) {
            next = this.findWaterPosTowards(goal, 8, 4, Mth.HALF_PI);
        }
        if (next == null) {
            next = this.findWaterPosTowards(goal, 8, 5, Mth.HALF_PI);
        }
        return next != null && this.getNavigation().moveTo(next.x, next.y, next.z, 1.0D);
    }

    private @Nullable Vec3 findWaterPosTowards(Vec3 goal, int horizontal, int vertical, float angle) {
        Vec3 pos = DefaultRandomPos.getPosTowards(this, horizontal, vertical, goal, angle);
        if (pos == null) {
            return null;
        }
        BlockPos blockPos = BlockPos.containing(pos);
        return this.level().getFluidState(blockPos).is(FluidTags.WATER) && this.level().getBlockState(blockPos).isPathfindable(PathComputationType.WATER) ? pos : null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(NaturalistRegistry.CRAB.get())) {
            if (this.monumentTarget == null && this.level() instanceof ServerLevel level) {
                BlockPos monument = level.findNearestMapStructure(TagKey.create(Registries.STRUCTURE, Identifier.withDefaultNamespace("on_ocean_monument_maps")), this.blockPosition(), 50, false);
                BlockPos target = monument == null ? null : new BlockPos(monument.getX(), level.getSeaLevel() - 12, monument.getZ());
                if (target != null && this.swimTowards(target)) {
                    this.monumentTarget = target;
                    this.seekTicks = 0;
                    this.playSound(NaturalistAnimal.eatingSound(stack), 1.0F, 1.0F);
                    stack.consume(1, player);
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(0.5D), this.getZ(), 7, this.getBbWidth() * 0.35D, this.getBbHeight() * 0.35D, this.getBbWidth() * 0.35D, 0.01D);
                } else {
                    level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(0.5D), this.getZ(), 7, this.getBbWidth() * 0.35D, this.getBbHeight() * 0.35D, this.getBbWidth() * 0.35D, 0.02D);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.isEmpty() && !this.isBaby() && this.isMouthInked()) {
            if (!this.level().isClientSide()) {
                this.setMouthInked(false);
                this.playSound(NaturalistSoundEvents.WHALE_SHARK_COLLECT_INK.get(), 1.0F, 1.0F);
                player.getInventory().placeItemBackInInventory(new ItemStack(Items.GLOW_INK_SAC), Prediction.SERVER_ONLY);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.isBaby()) {
            this.inkTimer = TIMER_UNARMED;
            this.setMouthInked(false);
        } else if (!this.isMouthInked()) {
            if (this.inkTimer == TIMER_UNARMED) {
                this.inkTimer = Mth.randomBetweenInclusive(this.random, 6000, 12000);
            }
            if (--this.inkTimer <= 0) {
                this.setMouthInked(true);
                this.inkTimer = TIMER_UNARMED;
            }
        }
        if (this.monumentTarget != null) {
            boolean arrived = Mth.length(this.monumentTarget.getX() + 0.5D - this.getX(), this.monumentTarget.getZ() + 0.5D - this.getZ()) <= 12.0D;
            if (arrived || ++this.seekTicks > 12000) {
                if (arrived) {
                    this.playSound(NaturalistSoundEvents.WHALE_SHARK_AMBIENT.get(), 1.0F, this.getVoicePitch());
                }
                this.monumentTarget = null;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            return;
        }
        this.bodyTilt.tick(this);
        this.swimAnimationState.animateWhen(true, this.tickCount);
        this.swimAmplitudeO = this.swimAmplitude;
        this.swimAmplitude += (Mth.clamp(0.35F + (float) Mth.length(this.getX() - this.xo, this.getZ() - this.zo) * 110.0F + (float) Math.abs(this.getY() - this.yo) * 2.0F, 0.35F, 1.3F) - this.swimAmplitude) * (1.0F - (float) Math.exp(-0.1F));

        float animationSpeed = this.bodyTilt.animSpeed.get(1.0F);
        if (animationSpeed > 0.7F ? !this.mouthOpen : animationSpeed < 0.5F && this.mouthOpen) {
            this.mouthOpen = !this.mouthOpen;
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), NaturalistSoundEvents.WHALE_SHARK_MOUTH.get(), this.getSoundSource(), this.mouthOpen ? 1.0F : 0.7F, (this.mouthOpen ? 1.0F : 0.8F) * (this.isBaby() ? 1.414F : 1.0F), false);
        }

        if (this.isMouthInked() && !this.isBaby() && this.tickCount % 15 == 0) {
            Vec3 mouth = this.position().add(Vec3.directionFromRotation(0.0F, this.yBodyRot).scale(3.9375D)).add(0.0D, 0.625D, 0.0D);
            for (int i = 0; i < 3; i++) {
                double speed = (0.1D + this.random.nextDouble() * 0.3D) / 20.0D;
                this.level().addParticle(ParticleTypes.GLOW,
                        mouth.x + (this.random.nextDouble() * 2.0D - 1.0D) * 1.1D,
                        mouth.y + (this.random.nextDouble() * 2.0D - 1.0D) * 0.4D,
                        mouth.z + (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D,
                        (this.random.nextDouble() * 2.0D - 1.0D) * speed,
                        (this.random.nextDouble() * 1.2D - 0.2D) * speed,
                        (this.random.nextDouble() * 2.0D - 1.0D) * speed);
            }
        }
    }
    //endregion

    //region Goals
    static class SeekMonumentGoal extends Goal {
        private final WhaleShark whaleShark;

        SeekMonumentGoal(WhaleShark whaleShark) {
            this.whaleShark = whaleShark;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.whaleShark.monumentTarget != null && this.whaleShark.isInWater();
        }

        @Override
        public void stop() {
            this.whaleShark.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.whaleShark.monumentTarget != null && this.whaleShark.getNavigation().isDone() && !this.whaleShark.swimTowards(this.whaleShark.monumentTarget)) {
                this.whaleShark.monumentTarget = null;
            }
        }
    }
    //endregion
}
