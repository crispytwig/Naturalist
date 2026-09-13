package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class AttackEggGoal extends RemoveBlockGoal {
    private final SoundEvent breakSound;

    public AttackEggGoal(Block eggBlock, SoundEvent breakSound, PathfinderMob pathfinderMob, double speedModifier, int verticalSearchRange) {
        super(eggBlock, pathfinderMob, speedModifier, verticalSearchRange);
        this.breakSound = breakSound;
    }

    @Override
    public void playDestroyProgressSound(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + level.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public void playBreakSound(@NotNull Level level, @NotNull BlockPos pos) {
        level.playSound(null, pos, this.breakSound, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public double acceptedDistance() {
        return 1.14;
    }
}
