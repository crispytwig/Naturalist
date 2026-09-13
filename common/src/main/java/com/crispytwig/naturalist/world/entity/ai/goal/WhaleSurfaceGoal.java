package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WhaleSurfaceGoal extends Goal {
    private static final int MAX_RUN_TICKS = 600;
    private static final int SKIM_TICKS = 60;
    private static final float BREACH_CHANCE = 0.3F;

    private final Whale whale;
    private long nextAllowedTick;
    private int runTicks;
    private int broachTicks;
    private int lungeTicks;
    private int surfaceY;
    private boolean breach;
    private boolean lunged;

    public WhaleSurfaceGoal(Whale whale) {
        this.whale = whale;
        this.nextAllowedTick = 200 + whale.getRandom().nextInt(400);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if (!this.whale.isInWater() || this.whale.isDiving() || this.whale.tickCount < this.nextAllowedTick) {
            return false;
        }
        this.surfaceY = this.findSurfaceAbove();
        return this.surfaceY != Integer.MIN_VALUE;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.runTicks >= MAX_RUN_TICKS || this.broachTicks >= SKIM_TICKS) {
            return false;
        }
        if (this.lunged) {
            if (this.lungeTicks >= 60) {
                return false;
            }
            return this.lungeTicks < 10 || !this.whale.isInWater()
                    || this.whale.getDeltaMovement().y > 0.0D;
        }
        if (this.whale.isGrindingTerrain() || !this.whale.isInWater()) {
            return false;
        }
        this.surfaceY = this.findSurfaceAbove();
        return this.surfaceY != Integer.MIN_VALUE;
    }

    @Override
    public void start() {
        this.runTicks = 0;
        this.broachTicks = 0;
        this.lungeTicks = 0;
        this.lunged = false;
        this.breach = this.whale.getRandom().nextFloat() < BREACH_CHANCE;
        this.whale.getNavigation().stop();
    }

    @Override
    public void stop() {
        if (this.lunged && this.whale.isInWater()) {
            this.splash();
        }
        this.nextAllowedTick = this.whale.tickCount + (this.broachTicks > 0 || this.lunged
                ? 600 + this.whale.getRandom().nextInt(900)
                : 150 + this.whale.getRandom().nextInt(150));
    }

    @Override
    public void tick() {
        this.runTicks++;
        if (this.lunged) {
            this.lungeTicks++;
            return;
        }
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.whale.yBodyRot);
        if (this.breach && this.surfaceY - (this.whale.getY() + this.whale.getBbHeight()) < 2.0D) {
            if (this.whale.level().isWaterAt(BlockPos.containing(this.whale.position().add(forward.scale(12.0D))))) {
                this.whale.setDeltaMovement(forward.x * 0.6D, 0.85D, forward.z * 0.6D);
                this.lunged = true;
                return;
            }
            this.breach = false;
        }
        if (this.whale.canSpray()) {
            this.broachTicks++;
        }
        double wantedY = this.surfaceY - this.whale.getBbHeight() + 0.1D;
        double diff = wantedY - this.whale.getY();
        if (Math.abs(diff) < 1.0D) {
            Vec3 dm = this.whale.getDeltaMovement();
            this.whale.setDeltaMovement(dm.x, Mth.clamp(diff * 0.15D, -0.06D, 0.06D), dm.z);
            wantedY = this.whale.getY();
        }
        this.whale.getMoveControl().setWantedPosition(
                this.whale.getX() + forward.x * 10.0D, wantedY, this.whale.getZ() + forward.z * 10.0D, 1.2D);
    }

    private int findSurfaceAbove() {
        BlockPos.MutableBlockPos pos = BlockPos.containing(
                this.whale.getX(), this.whale.getY() + this.whale.getBbHeight(), this.whale.getZ()).mutable();
        for (int i = 0; i < 48; i++) {
            if (!this.whale.level().isWaterAt(pos)) {
                return this.whale.level().getBlockState(pos).isAir() ? pos.getY() : Integer.MIN_VALUE;
            }
            pos.move(0, 1, 0);
        }
        return Integer.MIN_VALUE;
    }

    private void splash() {
        if (this.whale.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.whale.getX(), this.whale.getY() + this.whale.getBbHeight() * 0.5D, this.whale.getZ(),
                    80, 2.5D, 0.4D, 2.5D, 0.2D);
        }
        this.whale.playSound(SoundEvents.GENERIC_SPLASH, 2.0F, 0.6F);
    }
}
