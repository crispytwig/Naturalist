package com.crispytwig.naturalist.world.level.block.entity;

import com.crispytwig.naturalist.world.level.block.AntHillBlock;
import com.crispytwig.naturalist.world.entity.PetTargeting;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.registry.NaturalistBlockEntities;

import java.util.UUID;

public class AntHillBlockEntity extends BlockEntity {
    private static final int POLL_INTERVAL = 10;
    private static final double DEFEND_RANGE_SQR = 24.0D * 24.0D;
    private static final int STORAGE_SIZE = 9;

    @Nullable
    private UUID owner;
    private final SimpleContainer storage = new SimpleContainer(STORAGE_SIZE);

    public AntHillBlockEntity(BlockPos pos, BlockState state) {
        super(NaturalistBlockEntities.ANT_HILL.get(), pos, state);
    }

    @Nullable
    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
        this.setChanged();
    }

    public SimpleContainer getStorage() {
        return this.storage;
    }

    public ItemStack storeFood(ItemStack stack) {
        ItemStack leftover = this.storage.addItem(stack);
        this.setChanged();
        return leftover;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("Owner", UUIDUtil.CODEC, this.owner);
        this.storage.storeAsItemList(output.list("Storage", ItemStack.CODEC));
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.owner = input.read("Owner", UUIDUtil.CODEC).orElse(null);
        this.storage.fromItemList(input.listOrEmpty("Storage", ItemStack.CODEC));
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.storage);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AntHillBlockEntity hill) {
        if (hill.owner == null || (level.getGameTime() + pos.asLong()) % POLL_INTERVAL != 0 || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        int workers = state.getValue(AntHillBlock.WORKERS);
        if (workers <= 0) {
            return;
        }
        Player player = level.getPlayerByUUID(hill.owner);
        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + 0.5D;
        double centerZ = pos.getZ() + 0.5D;
        if (player == null || player.distanceToSqr(centerX, centerY, centerZ) > DEFEND_RANGE_SQR) {
            return;
        }
        LivingEntity threat = null;
        if (player.tickCount - player.getLastHurtByMobTimestamp() < POLL_INTERVAL) {
            threat = player.getLastHurtByMob();
        } else if (player.tickCount - player.getLastHurtMobTimestamp() < POLL_INTERVAL) {
            threat = player.getLastHurtMob();
        }
        if (threat == null || !threat.isAlive()
                || !PetTargeting.wantsToAttack(threat, player)
                || threat.distanceToSqr(centerX, centerY, centerZ) > DEFEND_RANGE_SQR) {
            return;
        }
        BlockPos releasePos = AntHillBlock.findReleasePos(serverLevel, pos, serverLevel.getRandom());
        if (releasePos == null) {
            return;
        }
        serverLevel.setBlock(pos, state.setValue(AntHillBlock.WORKERS, 0), 3);
        AntHillBlock.playEnterLeaveEffects(serverLevel, pos, serverLevel.getRandom());
        for (int i = 0; i < workers; i++) {
            Ant defender = AntHillBlock.releaseAnt(serverLevel, releasePos, serverLevel.getRandom(), hill.owner);
            if (defender != null) {
                defender.setTarget(threat);
            }
        }
    }
}
