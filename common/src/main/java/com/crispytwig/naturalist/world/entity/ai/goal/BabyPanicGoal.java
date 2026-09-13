package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import org.jetbrains.annotations.NotNull;

public class BabyPanicGoal extends PanicGoal {
    public BabyPanicGoal(@NotNull PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    protected boolean shouldPanic() {
        return mob.getLastHurtByMob() != null && mob.isBaby() || mob.isOnFire();
    }
}
