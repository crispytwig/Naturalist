package com.crispytwig.naturalist.world.level.levelgen.feature;

import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.block.AntHillBlock;
import com.crispytwig.naturalist.world.level.block.entity.AntHillBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AntHillFeature implements Feature {
    public static final MapCodec<AntHillFeature> SMALL_CODEC = MapCodec.unit(() -> new AntHillFeature(false));
    public static final MapCodec<AntHillFeature> BIG_CODEC = MapCodec.unit(() -> new AntHillFeature(true));

    private static final BlockPos[] SMALL_LAYOUT = {
            new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1),
            new BlockPos(-1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(0, 1, 1),
            new BlockPos(0, 2, 0)
    };
    private static final BlockPos[] BIG_LAYOUT = {
            new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1),
            new BlockPos(-1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(1, 1, 0), new BlockPos(0, 1, 1),
            new BlockPos(0, 2, 0), new BlockPos(0, 2, 1),
            new BlockPos(0, 3, 0)
    };

    private final boolean big;

    public AntHillFeature(boolean big) {
        this.big = big;
    }

    @Override
    public @NotNull MapCodec<AntHillFeature> codec() {
        return this.big ? BIG_CODEC : SMALL_CODEC;
    }

    @Override
    public boolean place(@NotNull WorldGenLevel level, @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource random, @NotNull BlockPos origin) {
        if (!level.getBlockState(origin.below()).is(BlockTags.SUBSTRATE_OVERWORLD)) {
            return false;
        }
        BlockPos[] layout = this.big ? BIG_LAYOUT : SMALL_LAYOUT;
        Rotation rotation = Rotation.getRandom(random);
        if (!findBlockingPositions(level, origin, layout, rotation).isEmpty()) {
            return false;
        }
        buildHill(level, origin, random, layout, rotation, null);
        return true;
    }

    public static boolean isReplaceable(BlockState state) {
        return state.canBeReplaced() || state.is(NaturalistRegistry.ANT_HILL.get()) || state.is(BlockTags.LEAVES);
    }

    public static List<BlockPos> findSmallHillBlockers(LevelReader level, BlockPos origin) {
        Set<BlockPos> blockers = new LinkedHashSet<>();
        for (Rotation rotation : Rotation.values()) {
            blockers.addAll(findBlockingPositions(level, origin, SMALL_LAYOUT, rotation));
        }
        return new ArrayList<>(blockers);
    }

    public static void placeSmallHill(LevelAccessor level, BlockPos origin, RandomSource random, @Nullable UUID owner) {
        buildHill(level, origin, random, SMALL_LAYOUT, Rotation.getRandom(random), owner);
    }

    private static List<BlockPos> findBlockingPositions(LevelReader level, BlockPos origin, BlockPos[] layout, Rotation rotation) {
        List<BlockPos> blockers = new ArrayList<>();
        for (BlockPos offset : layout) {
            BlockPos pos = origin.offset(offset.rotate(rotation));
            if (!isReplaceable(level.getBlockState(pos))) {
                blockers.add(pos);
            }
        }
        return blockers;
    }

    private static void buildHill(LevelAccessor level, BlockPos origin, RandomSource random, BlockPos[] layout, Rotation rotation, @Nullable UUID owner) {
        List<BlockPos> positions = new ArrayList<>(layout.length);
        for (BlockPos offset : layout) {
            positions.add(origin.offset(offset.rotate(rotation)));
        }
        List<BlockPos> closed = new ArrayList<>(positions);
        List<BlockPos> holePositions = new ArrayList<>();
        int holes = 4 + random.nextInt(3);
        for (int i = 0; i < holes; i++) {
            holePositions.add(closed.remove(random.nextInt(closed.size())));
        }
        int queenIndex = random.nextInt(holePositions.size());
        for (BlockPos pos : closed) {
            level.setBlock(pos, NaturalistRegistry.ANT_HILL.get().defaultBlockState(), 3);
        }
        for (int i = 0; i < holePositions.size(); i++) {
            boolean queen = i == queenIndex;
            BlockState state = NaturalistRegistry.ANT_HILL.get().defaultBlockState()
                    .setValue(AntHillBlock.OPEN, true)
                    .setValue(AntHillBlock.WORKERS, queen ? 0 : sampleWorkers(random))
                    .setValue(AntHillBlock.HAS_QUEEN, queen);
            level.setBlock(holePositions.get(i), state, 3);
        }
        if (owner != null) {
            for (BlockPos pos : positions) {
                if (level.getBlockEntity(pos) instanceof AntHillBlockEntity hill) {
                    hill.setOwner(owner);
                }
            }
        }
    }

    private static int sampleWorkers(RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 25) {
            return 0;
        }
        return roll < 80 ? 1 : 2;
    }
}
