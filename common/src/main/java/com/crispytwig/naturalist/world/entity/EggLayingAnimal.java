package com.crispytwig.naturalist.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface EggLayingAnimal {
    boolean hasEgg();
    void setHasEgg(boolean hasEgg);
    boolean isLayingEgg();
    void setLayingEgg(boolean isLayingEgg);
    int getLayEggCounter();
    void setLayEggCounter(int layEggCounter);
    Block getEggBlock();
    TagKey<Block> getEggLayableBlockTag();

    default BlockState createEggBlockState(int eggCount) {
        return this.getEggBlock().defaultBlockState();
    }

    default void onEggLaid(BlockPos pos) {
    }
}
