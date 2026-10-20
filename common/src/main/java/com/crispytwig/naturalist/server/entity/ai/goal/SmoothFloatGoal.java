package com.crispytwig.naturalist.server.entity.ai.goal;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SmoothFloatGoal extends Goal {
    private final Mob mob;

    public SmoothFloatGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        mob.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold() || this.mob.isInLava();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold() + 0.1D) {
            Vec3 movement = this.mob.getDeltaMovement();
            if (movement.y < 0.05D) {
                this.mob.setDeltaMovement(movement.x, Math.min(movement.y + 0.02D, 0.05D), movement.z);
            }
        }
    }

    @Override
    public void start() {
        this.mob.setNoGravity(true);
    }

    @Override
    public void stop() {
        this.mob.setNoGravity(false);
    }
}
