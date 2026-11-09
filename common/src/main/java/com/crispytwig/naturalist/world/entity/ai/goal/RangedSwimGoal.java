package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RangedSwimGoal extends RandomSwimmingGoal {
    private final int verticalRange;

    public RangedSwimGoal(PathfinderMob mob, int interval, int verticalRange) {
        super(mob, 1.0D, interval);
        this.verticalRange = verticalRange;
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater() && super.canUse();
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.mob, 16, this.verticalRange);
    }
}
