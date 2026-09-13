package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.world.level.levelgen.feature.AntHillFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QueenAntItem extends Item {
    private static final int BLOCKED_PARTICLE_COLOR = 0xFF3333;

    public QueenAntItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clicked = context.getClickedPos();
        BlockPos base = AntHillFeature.isReplaceable(level.getBlockState(clicked)) ? clicked : clicked.above();
        List<BlockPos> blockingBlocks = AntHillFeature.findSmallHillBlockers(level, base);
        if (!blockingBlocks.isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {
                for (BlockPos pos : blockingBlocks) {
                    serverLevel.sendParticles(new DustParticleOptions(BLOCKED_PARTICLE_COLOR, 1.0F), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 4, 0.25D, 0.25D, 0.25D, 0.0D);
                }
            }
            return InteractionResult.FAIL;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        AntHillFeature.placeSmallHill(serverLevel, base, serverLevel.getRandom(), player != null ? player.getUUID() : null);
        serverLevel.playSound(null, base, SoundEvents.ROOTED_DIRT_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (player == null || !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
