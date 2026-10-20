package com.crispytwig.naturalist.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BetterWallClimberNavigation extends GroundPathNavigation {
    @Nullable
    private BlockPos pathToPosition;

    public BetterWallClimberNavigation(@NotNull Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public @NotNull Path createPath(@NotNull BlockPos pos, int accuracy) {
        this.pathToPosition = pos;
        return super.createPath(pos, accuracy);
    }

    @Override
    public @NotNull Path createPath(Entity entity, int i) {
        this.pathToPosition = entity.blockPosition();
        return super.createPath(entity, i);
    }

    @Override
    public boolean moveTo(@NotNull Entity entity, double speed) {
        return this.moveTo(this.createPath(entity, 0), speed);
    }

    @Override
    public void tick() {
        if (!this.isDone()) {
            super.tick();
        } else {
            if (this.pathToPosition != null) {
                if (!this.pathToPosition.closerToCenterThan(this.mob.position(),
                        Math.max(this.mob.getBbWidth(), 1.0D))
                        && (!(this.mob.getY() > (double) this.pathToPosition.getY())
                        || !(new BlockPos(this.pathToPosition.getX(), (int) this.mob.getY(),
                        this.pathToPosition.getZ())).closerToCenterThan(this.mob.position(),
                        Math.max(this.mob.getBbWidth(), 1.0D)))) {
                    this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(),
                            this.pathToPosition.getZ(), this.speedModifier);
                } else {
                    this.pathToPosition = null;
                }
            }
        }
    }
}
