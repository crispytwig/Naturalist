package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WhaleDiveGoal extends Goal {
    private static final int MAX_RUN_TICKS = 600;
    private static final int BOTTOM_TICKS = 150;
    private static final int MIN_DEPTH_BELOW = 6;

    private final Whale whale;
    private long nextAllowedTick;
    private int runTicks;
    private int bottomTicks;

    public WhaleDiveGoal(Whale whale) {
        this.whale = whale;
        this.nextAllowedTick = 400 + whale.getRandom().nextInt(400);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if (!this.whale.isInWater() || this.whale.tickCount < this.nextAllowedTick) {
            return false;
        }
        int floorY = this.findFloorAt(this.whale.getX(), this.whale.getZ());
        return floorY != Integer.MIN_VALUE && this.whale.getY() - floorY >= MIN_DEPTH_BELOW;
    }

    @Override
    public boolean canContinueToUse() {
        return this.runTicks < MAX_RUN_TICKS && this.bottomTicks < BOTTOM_TICKS
                && !this.whale.horizontalCollision
                && !this.whale.isGrindingTerrain()
                && this.whale.isInWater();
    }

    @Override
    public void start() {
        this.runTicks = 0;
        this.bottomTicks = 0;
        this.whale.setDiving(true);
        this.whale.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.whale.setDiving(false);
        this.nextAllowedTick = this.whale.tickCount + 800 + this.whale.getRandom().nextInt(800);
    }

    @Override
    public void tick() {
        this.runTicks++;
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.whale.yBodyRot);
        int floorY = Math.max(
                this.findFloorAt(this.whale.getX(), this.whale.getZ()),
                this.findFloorAt(this.whale.getX() + forward.x * 10.0D, this.whale.getZ() + forward.z * 10.0D));
        if (floorY == Integer.MIN_VALUE) {
            return;
        }
        if (this.whale.getY() - floorY < MIN_DEPTH_BELOW) {
            this.bottomTicks++;
        }
        this.whale.getMoveControl().setWantedPosition(
                this.whale.getX() + forward.x * 10.0D, floorY + 4.0D, this.whale.getZ() + forward.z * 10.0D, 1.2D);
    }

    private int findFloorAt(double x, double z) {
        BlockPos.MutableBlockPos pos = BlockPos.containing(x, this.whale.getY(), z).mutable();
        for (int i = 0; i <= 32; i++) {
            if (!this.whale.level().isWaterAt(pos) && !this.whale.level().getBlockState(pos).isAir()) {
                return pos.getY();
            }
            pos.move(0, -1, 0);
        }
        return Integer.MIN_VALUE;
    }
}
