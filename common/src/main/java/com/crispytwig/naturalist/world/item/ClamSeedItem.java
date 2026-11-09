package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClamSeedItem extends Item {
    public ClamSeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos spawnPos = level.getBlockState(clicked).getCollisionShape(level, clicked).isEmpty()
                ? clicked
                : clicked.relative(face);
        return this.place(serverLevel, context.getPlayer(), context.getItemInHand(), spawnPos, true, !clicked.equals(spawnPos) && face == Direction.UP);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = hit.getBlockPos();
        if (!(level.getBlockState(pos).getBlock() instanceof LiquidBlock)) {
            return InteractionResult.PASS;
        }
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hit.getDirection(), stack)) {
            return InteractionResult.FAIL;
        }
        return this.place(serverLevel, player, stack, pos, false, false);
    }

    private InteractionResult place(ServerLevel level, @Nullable Player player, ItemStack stack, BlockPos pos, boolean tryMoveDown, boolean movedUp) {
        Clam clam = NaturalistEntityTypes.CLAM.get().spawn(level, Clam::becomeSeedling, pos, EntitySpawnReason.SPAWN_ITEM_USE, tryMoveDown, movedUp);
        if (clam == null) {
            return InteractionResult.FAIL;
        }
        clam.setPersistenceRequired();
        if (player != null) {
            float yaw = Mth.wrapDegrees(player.getYRot() + 180.0F);
            clam.snapTo(clam.getX(), clam.getY(), clam.getZ(), yaw, 0.0F);
            clam.yBodyRot = yaw;
            clam.yHeadRot = yaw;
        }
        stack.consume(1, player);
        level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        return InteractionResult.SUCCESS;
    }
}
