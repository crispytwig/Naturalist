package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WhaleSeekDeeperWaterGoal extends Goal {
    private static final int MAX_RUN_TICKS = 400;

    private final Whale whale;
    private Vec3 target;
    private int runTicks;
    private int retargetTicks;

    public WhaleSeekDeeperWaterGoal(Whale whale) {
        this.whale = whale;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return this.whale.isInWater()
                && this.hasWaterAbove(1.5D)
                && this.whale.getDeltaMovement().lengthSqr() < 0.01D;
    }

    @Override
    public boolean canContinueToUse() {
        return this.runTicks < MAX_RUN_TICKS && this.whale.isInWater() && this.hasWaterAbove(2.0D);
    }

    @Override
    public void start() {
        this.runTicks = 0;
        this.retargetTicks = 0;
        this.target = null;
        this.whale.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.runTicks++;
        if (--this.retargetTicks <= 0 || this.target == null) {
            this.retargetTicks = 40;
            Vec3 found = this.findDeeperWater();
            if (found != null) {
                this.target = found;
            }
        }
        if (this.target != null) {
            this.whale.getMoveControl().setWantedPosition(this.target.x, this.target.y, this.target.z, 1.2D);
        }
    }

    private boolean hasWaterAbove(double height) {
        return !this.whale.level().isWaterAt(BlockPos.containing(this.whale.getX(), this.whale.getY() + height, this.whale.getZ()));
    }

    @Nullable
    private Vec3 findDeeperWater() {
        float startAngle = this.whale.getRandom().nextFloat() * Mth.TWO_PI;
        for (int dist = 4; dist <= 24; dist += 4) {
            for (int i = 0; i < 12; i++) {
                float angle = startAngle + (float) i / 12.0F * Mth.TWO_PI;
                double x = this.whale.getX() + Mth.sin(angle) * dist;
                double z = this.whale.getZ() + Mth.cos(angle) * dist;
                BlockPos pos = BlockPos.containing(x, this.whale.getY(), z);
                if (this.whale.level().isWaterAt(pos) && this.whale.level().isWaterAt(pos.below(2))) {
                    return new Vec3(x, this.whale.getY() - 2.0D, z);
                }
            }
        }
        return null;
    }
}
