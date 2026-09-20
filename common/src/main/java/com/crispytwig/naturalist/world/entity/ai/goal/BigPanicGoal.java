package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BigPanicGoal extends PanicGoal {
    private static final int SEARCH_DISTANCE = 15;
    private static final int VERTICAL_CHECK_DISTANCE = 4;

    public BigPanicGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    protected boolean findRandomPosition() {
        Vec3 randomPos = DefaultRandomPos.getPos(this.mob, SEARCH_DISTANCE, VERTICAL_CHECK_DISTANCE);
        if (randomPos == null) {
            return false;
        } else {
            this.posX = randomPos.x;
            this.posY = randomPos.y;
            this.posZ = randomPos.z;
            return true;
        }
    }

    @Nullable
    @Override
    protected BlockPos lookForWater(LevelReader level, Entity entity, int range) {
        BlockPos entityPos = entity.blockPosition();
        if (!level.getBlockState(entityPos).getCollisionShape(level, entityPos).isEmpty()) {
            return null;
        }
        return level.findBlocksInBoxByManhattanDistance(entityPos, range + 5, 2)
                .filterState(state -> state.getFluidState().is(FluidTags.WATER))
                .findFirst()
                .orElse(null);
    }
}
