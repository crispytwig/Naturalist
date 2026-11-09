package com.crispytwig.naturalist.common.level.levelgen.feature;

import com.crispytwig.naturalist.common.level.block.MeerkatWarrenBlock;
import com.crispytwig.naturalist.common.level.block.entity.MeerkatWarrenBlockEntity;
import com.crispytwig.naturalist.common.registry.NaturalistRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.NotNull;

public class MeerkatWarrenFeature implements Feature {
    public static final MapCodec<MeerkatWarrenFeature> CODEC = MapCodec.unit(MeerkatWarrenFeature::new);

    @Override
    public @NotNull MapCodec<MeerkatWarrenFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(@NotNull WorldGenLevel level, @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource random, @NotNull BlockPos origin) {
        boolean placed = false;
        int attempts = Mth.randomBetweenInclusive(random, 5, 9);
        for (int i = 0; i < attempts; i++) {
            BlockPos column = origin.offset(offset(random), 0, offset(random));
            for (int y = 1; y >= -6; y--) {
                BlockPos pos = column.above(y);
                if (level.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
                    int roll = random.nextInt(11);
                    int meerkats = roll < 5 ? 0 : roll < 8 ? 1 : roll < 10 ? 2 : 3;
                    level.setBlock(pos, NaturalistRegistry.MEERKAT_WARREN.get().defaultBlockState().setValue(MeerkatWarrenBlock.MEERKATS, meerkats), 2);
                    if (level.getBlockEntity(pos) instanceof MeerkatWarrenBlockEntity warren) {
                        for (int count = 0; count < meerkats; count++) {
                            warren.storeMeerkat(MeerkatWarrenBlockEntity.Occupant.create(random.nextInt(599)));
                        }
                    }
                    placed = true;
                    break;
                }
            }
        }
        return placed;
    }

    private static int offset(RandomSource random) {
        return Mth.clamp(Mth.floor(random.nextGaussian() * 2.0D), -4, 4);
    }
}
