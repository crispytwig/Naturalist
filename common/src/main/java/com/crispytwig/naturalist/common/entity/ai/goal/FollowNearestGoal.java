package com.crispytwig.naturalist.common.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Predicate;

public class FollowNearestGoal<T extends LivingEntity> extends Goal {
    private final PathfinderMob mob;
    private final Class<T> type;
    private final Predicate<T> filter;
    private final double range;
    private final double stopDistance;
    private final double speedModifier;
    private T target;
    private int repathDelay;

    public FollowNearestGoal(PathfinderMob mob, Class<T> type, Predicate<T> filter, double range, double stopDistance, double speedModifier) {
        this.mob = mob;
        this.type = type;
        this.filter = filter;
        this.range = range;
        this.stopDistance = stopDistance;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(reducedTickDelay(20)) != 0) {
            return false;
        }
        T nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (T candidate : this.mob.level().getEntitiesOfClass(this.type, this.mob.getBoundingBox().inflate(this.range), this::isValid)) {
            double distance = this.mob.distanceToSqr(candidate);
            if (distance < nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }
        if (nearest == null || nearestDistance <= this.stopDistance * this.stopDistance) {
            return false;
        }
        this.target = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.isValid(this.target) && this.mob.distanceToSqr(this.target) <= this.range * this.range;
    }

    @Override
    public void start() {
        this.repathDelay = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.target, 10.0F, this.mob.getMaxHeadXRot());
        if (--this.repathDelay > 0) {
            return;
        }
        this.repathDelay = this.adjustedTickDelay(10);
        if (this.mob.distanceToSqr(this.target) > this.stopDistance * this.stopDistance) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        } else {
            this.mob.getNavigation().stop();
        }
    }

    private boolean isValid(T candidate) {
        return candidate != this.mob && candidate.isAlive() && this.filter.test(candidate);
    }
}
