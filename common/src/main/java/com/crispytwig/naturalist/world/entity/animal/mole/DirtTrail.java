package com.crispytwig.naturalist.world.entity.animal.mole;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

public class DirtTrail extends Entity {
    private static final EntityDataAccessor<Boolean> SMALL = SynchedEntityData.defineId(DirtTrail.class, EntityDataSerializers.BOOLEAN);
    private static final int LIFETIME = 50;

    public final SmoothAnimationState spawnAnimationState = SmoothAnimationState.instant();

    public DirtTrail(EntityType<? extends DirtTrail> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(SMALL, false);
    }

    public boolean isSmall() {
        return this.entityData.get(SMALL);
    }

    public void setSmall(boolean small) {
        this.entityData.set(SMALL, small);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.spawnAnimationState.animateWhen(true, this.tickCount);
        } else if (this.level() instanceof ServerLevel serverLevel && this.tickCount >= LIFETIME) {
            double scale = this.isSmall() ? 0.6D : 1.0D;
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()),
                    this.getX(), this.getY() + 0.1D * scale, this.getZ(),
                    this.isSmall() ? 12 : 20, 0.25D * scale, 0.05D, 0.25D * scale, 0.05D);
            this.discard();
        }
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
    }
}
