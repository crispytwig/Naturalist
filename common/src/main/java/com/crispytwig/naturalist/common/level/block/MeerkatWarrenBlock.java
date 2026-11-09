package com.crispytwig.naturalist.common.level.block;

import com.crispytwig.naturalist.common.level.block.entity.MeerkatWarrenBlockEntity;
import com.crispytwig.naturalist.common.registry.NaturalistBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MeerkatWarrenBlock extends Block implements EntityBlock {
    public static final int MAX_INSIDE = 4;
    public static final IntegerProperty MEERKATS = IntegerProperty.create("meerkats", 0, MAX_INSIDE);

    public MeerkatWarrenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MEERKATS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(MEERKATS);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MeerkatWarrenBlockEntity(pos, state);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide() || type != NaturalistBlockEntities.MEERKAT_WARREN.get()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<MeerkatWarrenBlockEntity>) MeerkatWarrenBlockEntity::serverTick;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof MeerkatWarrenBlockEntity warren) {
            warren.emptyAllLivingFromWarren(serverLevel);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
