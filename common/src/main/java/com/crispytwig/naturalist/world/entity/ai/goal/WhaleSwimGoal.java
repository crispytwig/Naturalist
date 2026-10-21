package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WhaleSwimGoal extends RandomSwimmingGoal {
    private static final float FRONT_ANGLE = 45.0F;
    private static final double ARRIVE_DIST_SQR = 16.0D;
    private static final int STUCK_CHECKS = 40;
    private static final int MAX_LEG_CHECKS = 300;

    private final Whale whale;
    private Vec3 targetPos;
    private Vec3 lastPos = Vec3.ZERO;
    private int stuckChecks;
    private int legChecks;

    public WhaleSwimGoal(Whale whale, double speedModifier, int interval) {
        super(whale, speedModifier, interval);
        this.whale = whale;
    }

    @Override
    public boolean canUse() {
        if (!this.forceTrigger && this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
            return false;
        }
        Vec3 pos = this.getPosition();
        if (pos == null) {
            return false;
        }
        this.wantedX = pos.x;
        this.wantedY = pos.y;
        this.wantedZ = pos.z;
        this.forceTrigger = false;
        this.targetPos = pos;
        this.lastPos = this.whale.position();
        this.stuckChecks = 0;
        this.legChecks = 0;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (++this.legChecks > MAX_LEG_CHECKS) {
            return false;
        }
        Vec3 now = this.whale.position();
        this.stuckChecks = now.distanceToSqr(this.lastPos) < 0.0025D ? this.stuckChecks + 1 : 0;
        this.lastPos = now;
        if (this.stuckChecks > STUCK_CHECKS) {
            return false;
        }
        if (this.targetPos != null && now.distanceToSqr(this.targetPos) < ARRIVE_DIST_SQR) {
            return false;
        }
        return !this.mob.getNavigation().isDone();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.whale.getRandom().nextFloat() < 0.7F) {
            Vec3 ahead = this.findPositionAhead();
            if (ahead != null) {
                return ahead;
            }
        }
        for (int i = 0; i < 12; i++) {
            Vec3 pos = BehaviorUtils.getRandomSwimmablePos(this.mob, 24, 8);
            if (pos != null && this.isOpenWater(pos)) {
                return pos;
            }
        }
        return this.keepSubmerged(BehaviorUtils.getRandomSwimmablePos(this.mob, 24, 8));
    }

    @Nullable
    private Vec3 keepSubmerged(@Nullable Vec3 pos) {
        if (pos != null && !this.whale.level().isWaterAt(BlockPos.containing(pos).above(3))) {
            return pos.add(0.0D, -3.0D, 0.0D);
        }
        return pos;
    }

    @Nullable
    private Vec3 findPositionAhead() {
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.whale.yBodyRot);
        for (int i = 0; i < 10; i++) {
            Vec3 pos = this.whale.position()
                    .add(forward.yRot((this.whale.getRandom().nextFloat() * 2.0F - 1.0F) * FRONT_ANGLE * Mth.DEG_TO_RAD)
                            .scale(14.0D + this.whale.getRandom().nextFloat() * 8.0D))
                    .add(0.0D, -8.0D + this.whale.getRandom().nextFloat() * 12.0D, 0.0D);
            if (this.isOpenWater(pos)) {
                return pos;
            }
        }
        return null;
    }

    private boolean isOpenWater(Vec3 pos) {
        BlockPos center = BlockPos.containing(pos);
        return this.whale.level().isWaterAt(center) && this.whale.level().isWaterAt(center.above(3)) && this.whale.level().isWaterAt(center.below())
                && this.whale.level().isWaterAt(center.offset(2, 0, 0)) && this.whale.level().isWaterAt(center.offset(-2, 0, 0))
                && this.whale.level().isWaterAt(center.offset(0, 0, 2)) && this.whale.level().isWaterAt(center.offset(0, 0, -2));
    }
}
