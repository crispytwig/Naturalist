package com.crispytwig.naturalist.common.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GoToWaterGoal extends Goal {
    private final PathfinderMob mob;
    private int cooldown;
    private BlockPos water;

    public GoToWaterGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isInWater() || --this.cooldown > 0) {
            return false;
        }
        this.cooldown = this.adjustedTickDelay(40);
        for (BlockPos pos : BlockPos.withinClippedManhattan(this.mob.blockPosition(), 32, 8, 32)) {
            if (this.mob.level().getFluidState(pos).is(FluidTags.WATER)) {
                this.water = pos.immutable();
                return true;
            }
        }
        this.cooldown = this.adjustedTickDelay(400);
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.isInWater() && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.water.getX() + 0.5D, this.water.getY(), this.water.getZ() + 0.5D, 1.0D);
    }
}
