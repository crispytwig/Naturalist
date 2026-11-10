package com.crispytwig.naturalist.common.entity.manatee;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.common.registry.NaturalistRegistry;
import com.crispytwig.naturalist.common.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.common.tags.NaturalistItemTags;
import com.crispytwig.naturalist.common.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.common.entity.SmoothAnimationState;
import com.crispytwig.naturalist.common.entity.ai.goal.FollowNearestGoal;
import com.crispytwig.naturalist.common.entity.ai.goal.GoToWaterGoal;
import com.crispytwig.naturalist.common.entity.ai.goal.RangedSwimGoal;
import com.crispytwig.naturalist.common.entity.BucketableAquaticAnimal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

@SuppressWarnings("unused")
public class Manatee extends BucketableAquaticAnimal {
    //region Data
    private static final EntityDataAccessor<Boolean> SURFACING = SynchedEntityData.defineId(Manatee.class, EntityDataSerializers.BOOLEAN);

    private static final int TIMER_UNARMED = -1;

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState rollAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState surfaceAnimationState = new SmoothAnimationState(0.139F);
    public final AquaticBodyTilt bodyTilt = new AquaticBodyTilt(new AquaticBodyTilt.Settings(
            new AquaticBodyTilt.Speed(3.0F, 0.15F, 1.6F, 2.5F, 0.1F, 1.0F),
            new AquaticBodyTilt.Pitching(12.0F, 45.0F, 2.0F, 1.5F, 22.0F, 0.8F),
            new AquaticBodyTilt.Turning(5.0F, 0.8F, 16.0F, 1.4F, 20.0F),
            new AquaticBodyTilt.Segment(0.6F, 5.0F),
            new AquaticBodyTilt.Segment(0.3F, 3.0F),
            new AquaticBodyTilt.Segment(0.9F, 1.6F),
            new AquaticBodyTilt.Segment(0.5F, 0.9F),
            0.6F, 0.45F, 1.1F));

    private int surfaceTimer = TIMER_UNARMED;
    private int rollDelay = TIMER_UNARMED;
    private int rollTicks;

    public Manatee(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl<>(this, 85, 15, 0.5F, 0.42F, false);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.12D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SURFACING, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/manatee/manatee.png");
    }

    @Override
    protected float getBabySpawnChance() {
        return 0.1F;
    }

    @Override
    protected float getFoodHealAmount(@NotNull ItemStack stack) {
        return 2.0F;
    }

    @Override
    protected boolean canBeBucketed() {
        return this.isBaby();
    }

    public boolean isSurfacing() {
        return this.entityData.get(SURFACING);
    }

    private void setSurfacing(boolean surfacing) {
        this.entityData.set(SURFACING, surfacing);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Surfacing", this.isSurfacing());
        output.putInt("SurfaceTimer", this.surfaceTimer);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSurfacing(input.getBooleanOr("Surfacing", false));
        this.surfaceTimer = input.getIntOr("SurfaceTimer", TIMER_UNARMED);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.MANATEE_BUCKET.get());
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.MANATEE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.MANATEE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return NaturalistSoundEvents.MANATEE_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? 2.0F : 0.9F + this.random.nextFloat() * 0.2F;
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(NaturalistItemTags.MANATEE_FOOD_ITEMS);
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
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, this::isFood, false));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowNearestGoal<>(this, Manatee.class, other -> this.isBaby() && !other.isBaby(), 24.0D, 1.5D, 1.2D));
        this.goalSelector.addGoal(4, new FollowNearestGoal<>(this, Player.class, player -> !this.isBaby() && player.isPassenger(), 16.0D, 3.0D, 1.0D));
        this.goalSelector.addGoal(5, new SurfaceGoal(this));
        this.goalSelector.addGoal(6, new RangedSwimGoal(this, 40, 2));
    }

    @Override
    protected void adjustSwimMovement() {
        if (this.isSurfacing()) {
            Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x, this.getFluidHeight(FluidTags.WATER) > this.getBbHeight() * 0.7D ? Math.min(movement.y + 0.03D, 0.12D) : movement.y * 0.5D, movement.z);
        }
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (!this.isInWater()) {
            this.surfaceTimer = TIMER_UNARMED;
            this.setSurfacing(false);
            return;
        }
        if (this.surfaceTimer == TIMER_UNARMED) {
            this.surfaceTimer = Mth.randomBetweenInclusive(this.random, 1600, 3200);
        }
        if (--this.surfaceTimer > 0) {
            return;
        }
        boolean surfacing = !this.isSurfacing();
        this.setSurfacing(surfacing);
        this.surfaceTimer = surfacing ? Mth.randomBetweenInclusive(this.random, 160, 280) : TIMER_UNARMED;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            return;
        }
        this.bodyTilt.tick(this);
        this.swimAnimationState.animateWhen(true, this.tickCount);
        this.surfaceAnimationState.animateWhen(this.isSurfacing(), this.tickCount);
        if (this.isSurfacing() || !this.isInWater()) {
            this.rollTicks = 0;
            this.rollDelay = TIMER_UNARMED;
        } else if (this.rollTicks > 0) {
            if (--this.rollTicks == 0) {
                this.rollDelay = TIMER_UNARMED;
            }
        } else {
            if (this.rollDelay == TIMER_UNARMED) {
                this.rollDelay = Mth.randomBetweenInclusive(this.random, 1200, 3000);
            }
            if (this.rollDelay > 0) {
                this.rollDelay--;
            } else if (this.getDeltaMovement().horizontalDistance() <= 0.05D) {
                this.rollTicks = 100;
            }
        }
        this.rollAnimationState.animateWhen(this.rollTicks > 0, this.tickCount);
    }
    //endregion

    //region Goals
    static class SurfaceGoal extends Goal {
        private final Manatee manatee;

        SurfaceGoal(Manatee manatee) {
            this.manatee = manatee;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.manatee.isSurfacing();
        }

        @Override
        public void start() {
            this.manatee.getNavigation().stop();
        }
    }
    //endregion
}
