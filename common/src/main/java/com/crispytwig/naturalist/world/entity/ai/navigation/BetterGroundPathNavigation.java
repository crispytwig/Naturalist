package com.crispytwig.naturalist.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BetterGroundPathNavigation extends GroundPathNavigation {
    public BetterGroundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    public boolean canCutCorner(@NotNull PathType pathType) {
        return (pathType != PathType.FIRE_IN_NEIGHBOR && pathType != PathType.ON_TOP_OF_POWDER_SNOW
                && pathType != PathType.DAMAGING_IN_NEIGHBOR && pathType != PathType.WALKABLE_DOOR);
    }
    @Override
    protected void followThePath() {
        boolean shouldAdvance;
        Vec3 tempMobPos = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() * 0.75F;
        BlockPos nextPos = Objects.requireNonNull(this.path).getNextNodePos();
        double x = Math.abs(this.mob.getX() - ((double)nextPos.getX() + 0.5));
        double y = Math.abs(this.mob.getY() - (double)nextPos.getY());
        double z = Math.abs(this.mob.getZ() - ((double)nextPos.getZ() + 0.5));
        shouldAdvance = x < (double) this.maxDistanceToWaypoint && z < (double) this.maxDistanceToWaypoint && y < 1.0;
        if (shouldAdvance || canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(tempMobPos)) {
            this.path.advance();
        }
        this.doStuckDetection(tempMobPos);
    }

    private boolean shouldTargetNextNodeInDirection(@NotNull Vec3 currentPos) {
        if (Objects.requireNonNull(this.path).getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        }
        Vec3 nextPos = Vec3.atBottomCenterOf(this.path.getNextNodePos());
        if (!currentPos.closerThan(nextPos, 2.0)) {
            return false;
        }
        Vec3 nextPos2 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
        return nextPos2.subtract(nextPos).dot(currentPos.subtract(nextPos)) > 0.0;
    }
}
