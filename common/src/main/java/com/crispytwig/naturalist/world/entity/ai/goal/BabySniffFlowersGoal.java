package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BabySniffFlowersGoal extends MoveToBlockGoal {
    private static final int MAX_SNIFF_TICKS = 100;
    private final PathfinderMob mob;
    private final SoundEvent sniffSound;
    private final int horizontalRange;
    private final int verticalRange;
    protected int ticksWaited;

    public BabySniffFlowersGoal(@NotNull PathfinderMob mob, double speedModifier, int searchRange, int verticalSearchRange, SoundEvent sniffSound) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.mob = mob;
        this.sniffSound = sniffSound;
        this.horizontalRange = searchRange;
        this.verticalRange = verticalSearchRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }

    @Override
    public double acceptedDistance() {
        return 2.0D;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 100 == 0;
    }

    @Override
    protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.FLOWERS);
    }

    @Override
    protected boolean findNearestBlock() {
        BlockPos origin = this.mob.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        List<BlockPos> candidates = new ArrayList<>();
        for (int y = this.verticalSearchStart; y <= this.verticalRange; y = y > 0 ? -y : 1 - y) {
            for (int x = -this.horizontalRange; x <= this.horizontalRange; x++) {
                for (int z = -this.horizontalRange; z <= this.horizontalRange; z++) {
                    mutable.setWithOffset(origin, x, y - 1, z);
                    if (this.mob.isWithinHome(mutable) && this.isValidTarget(this.mob.level(), mutable)) {
                        candidates.add(mutable.immutable());
                    }
                }
            }
        }
        if (candidates.isEmpty()) {
            return false;
        }
        this.blockPos = candidates.get(this.mob.getRandom().nextInt(candidates.size()));
        return true;
    }

    @Override
    protected @NotNull BlockPos getMoveToTarget() {
        return this.blockPos;
    }

    @Override
    public boolean canUse() {
        return this.mob.isBaby() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.ticksWaited < MAX_SNIFF_TICKS && this.mob.isBaby() && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.ticksWaited = 0;
    }

    @Override
    public void tick() {
        if (this.isReachedTarget()) {
            this.mob.getNavigation().stop();
            if (this.ticksWaited % 20 == 0 && this.sniffSound != null) {
                this.mob.playSound(this.sniffSound, 1.0F, 1.0F);
            }
            ++this.ticksWaited;
        }
        Vec3 lookTarget = this.flowerCenter();
        this.mob.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z, 30.0F, this.mob.getMaxHeadXRot());
        super.tick();
    }

    private Vec3 flowerCenter() {
        BlockState state = this.mob.level().getBlockState(this.blockPos);
        VoxelShape shape = state.getShape(this.mob.level(), this.blockPos);
        if (shape.isEmpty()) {
            return Vec3.atCenterOf(this.blockPos);
        }
        return shape.bounds().getCenter().add(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
    }
}
