package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.ContainerBoundWorker;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class RatHarvestCropsGoal extends Goal {
    private static final double REACH_SQR = 4.0D;
    private static final int STUCK_TICKS = 200;
    private static final int HARVEST_DELAY = 10;
    private static final int DEPOSIT_INTERVAL = 3;

    private enum Phase {
        HARVEST,
        RETURN,
        DEPOSIT
    }

    private final Rat rat;
    private final double speedModifier;
    private final int radius;

    private Phase phase = Phase.HARVEST;
    private BlockPos target;
    private BlockPos pendingCrop;
    private int cooldown;
    private int pathTicks;
    private int actionDelay;
    private int depositStep;

    public RatHarvestCropsGoal(Rat rat, double speedModifier, int radius) {
        this.rat = rat;
        this.speedModifier = speedModifier;
        this.radius = radius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean canWork() {
        BlockPos workstation = this.rat.getWorkstationPos();
        return workstation != null
                && this.rat.isTame()
                && !this.rat.isBaby()
                && !this.rat.isOrderedToSit()
                && !this.rat.isSleeping()
                && !this.rat.isPassenger()
                && !this.rat.isInWater();
    }

    @Override
    public boolean canUse() {
        if (!this.canWork()) {
            return false;
        }
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        this.cooldown = reducedTickDelay(20 + this.rat.getRandom().nextInt(20));
        BlockPos workstation = this.rat.getWorkstationPos();
        if (this.rat.level().isLoaded(workstation) && ContainerBoundWorker.getContainer(this.rat.level(), workstation) == null) {
            this.rat.dropCarriedItems();
            this.rat.setWorkstation(null);
            return false;
        }
        this.pendingCrop = this.findNearestRipeCrop();
        return this.pendingCrop != null || !this.rat.getCarriedItems().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canWork() && this.target != null;
    }

    @Override
    public void start() {
        this.pathTicks = 0;
        this.actionDelay = 0;
        BlockPos crop = this.rat.isCarryFull() ? null : this.pendingCrop;
        this.pendingCrop = null;
        this.chooseNextTarget(crop);
    }

    @Override
    public void stop() {
        this.target = null;
        this.phase = Phase.HARVEST;
        this.actionDelay = 0;
        this.rat.getNavigation().stop();
        this.cooldown = reducedTickDelay(10);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            this.rat.getLookControl().setLookAt(Vec3.atCenterOf(this.target));
        }
        if (this.actionDelay > 0) {
            this.actionDelay--;
            return;
        }
        if (this.target == null) {
            return;
        }

        if (this.phase == Phase.DEPOSIT) {
            this.depositOne();
            return;
        }

        if (this.phase == Phase.HARVEST && isRipeCrop(this.rat.level(), this.target)) {
            this.chooseNextTarget();
            return;
        }

        if (this.rat.distanceToSqr(Vec3.atCenterOf(this.target)) > REACH_SQR) {
            this.pathTicks++;
            if (this.pathTicks > STUCK_TICKS) {
                if (this.phase == Phase.HARVEST) {
                    this.chooseNextTarget();
                } else {
                    this.target = null;
                }
                return;
            }
            if (this.rat.getNavigation().isDone()) {
                this.moveToTarget();
            }
            return;
        }

        this.rat.getNavigation().stop();
        if (this.phase == Phase.HARVEST) {
            this.harvestInto(this.target);
            this.actionDelay = HARVEST_DELAY + this.rat.getRandom().nextInt(HARVEST_DELAY);
        } else {
            this.phase = Phase.DEPOSIT;
            this.depositStep = 0;
            this.depositOne();
        }
    }

    private void chooseNextTarget() {
        this.chooseNextTarget(this.rat.isCarryFull() ? null : this.findNearestRipeCrop());
    }

    private void chooseNextTarget(BlockPos crop) {
        this.pathTicks = 0;
        if (crop != null) {
            this.phase = Phase.HARVEST;
            this.target = crop;
        } else if (!this.rat.getCarriedItems().isEmpty()) {
            this.phase = Phase.RETURN;
            this.target = this.rat.getWorkstationPos();
        } else {
            this.target = null;
            return;
        }
        this.moveToTarget();
    }

    private void moveToTarget() {
        if (this.target != null) {
            this.rat.getNavigation().moveTo(this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D, this.speedModifier);
        }
    }

    private void harvestInto(BlockPos pos) {
        if (!(this.rat.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        BlockState state = serverLevel.getBlockState(pos);
        if (isRipeCrop(serverLevel, pos)) {
            return;
        }
        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, serverLevel.getBlockEntity(pos), this.rat, ItemStack.EMPTY);
        serverLevel.setBlock(pos, getReplantState(state), Block.UPDATE_ALL);
        serverLevel.levelEvent(2001, pos, Block.getId(state));
        serverLevel.gameEvent(this.rat, GameEvent.BLOCK_CHANGE, pos);

        SimpleContainer cargo = this.rat.getCarriedItems();
        for (ItemStack drop : drops) {
            ItemStack leftover = cargo.addItem(drop);
            if (!leftover.isEmpty()) {
                Block.popResource(serverLevel, pos, leftover);
            }
        }
    }

    private void depositOne() {
        if (!(this.rat.level() instanceof ServerLevel serverLevel)) {
            this.target = null;
            return;
        }
        Container container = ContainerBoundWorker.getContainer(serverLevel, this.rat.getWorkstationPos());
        if (container == null) {
            this.rat.dropCarriedItems();
            this.target = null;
            return;
        }

        SimpleContainer cargo = this.rat.getCarriedItems();
        for (int i = 0; i < cargo.getContainerSize(); i++) {
            ItemStack stack = cargo.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack leftover = HopperBlockEntity.addItem(null, container, stack.copyWithCount(1), null);
            if (leftover.isEmpty()) {
                stack.shrink(1);
                cargo.setItem(i, stack);
                float pitch = 1.0F + 0.05F * (this.depositStep % 16);
                this.depositStep++;
                serverLevel.playSound(null, this.rat.getX(), this.rat.getY(), this.rat.getZ(),
                        SoundEvents.DECORATED_POT_INSERT, SoundSource.NEUTRAL, 0.2F, pitch);
                this.actionDelay = DEPOSIT_INTERVAL;
                return;
            }
        }

        this.target = null;
    }

    private BlockPos findNearestRipeCrop() {
        BlockPos center = this.rat.getWorkstationPos();
        if (center == null) {
            return null;
        }
        Level level = this.rat.level();
        BlockPos ratPos = this.rat.blockPosition();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -this.radius; dx <= this.radius; dx++) {
            for (int dz = -this.radius; dz <= this.radius; dz++) {
                for (int dy = -2; dy <= 2; dy++) {
                    cursor.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (isRipeCrop(level, cursor)) {
                        continue;
                    }
                    double dist = cursor.distSqr(ratPos);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = cursor.immutable();
                    }
                }
            }
        }
        return best;
    }

    private static boolean isRipeCrop(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) {
            return !crop.isMaxAge(state);
        }
        if (block instanceof NetherWartBlock) {
            return state.getValue(NetherWartBlock.AGE) < 3;
        }
        return true;
    }

    private static BlockState getReplantState(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) {
            return crop.getStateForAge(0);
        }
        return state.getBlock().defaultBlockState();
    }
}
