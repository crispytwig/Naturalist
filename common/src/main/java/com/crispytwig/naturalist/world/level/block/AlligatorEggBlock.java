package com.crispytwig.naturalist.world.level.block;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class AlligatorEggBlock extends TurtleEggBlock {
    public AlligatorEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.shouldUpdateHatchLevel(level, pos)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, NaturalistSoundEvents.GATOR_EGG_CRACK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.setBlock(pos, state.setValue(HATCH, i + 1), 2);
            } else {
                level.playSound(null, pos, NaturalistSoundEvents.GATOR_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.removeBlock(pos, false);
                for (int j = 0; j < state.getValue(EGGS); ++j) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    Alligator alligator = NaturalistEntityTypes.ALLIGATOR.get().create(level, EntitySpawnReason.BREEDING);
                    if (alligator != null) {
                        alligator.setAge(-24000);
                        alligator.snapTo((double) pos.getX() + 0.3 + (double) j * 0.2, pos.getY(), (double) pos.getZ() + 0.3, 0.0f, 0.0f);
                        level.addFreshEntity(alligator);
                    }
                }
            }
        }
    }

    @Override
    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(Level level, BlockPos pos) {
        float chance = level.environmentAttributes().getValue(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, pos);
        return chance > 0.0F && level.getRandom().nextFloat() < chance;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!entity.isSteppingCarefully()) {
            this.destroyEgg(level, state, pos, entity, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, double fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void destroyEgg(@NotNull Level level, BlockState state, @NotNull BlockPos pos, Entity entity, int chance) {
        if (!(level instanceof ServerLevel serverLevel) || !this.canDestroyEgg(serverLevel, entity)) {
            return;
        }
        if (level.getRandom().nextInt(chance) == 0 && state.is(this)) {
            this.decreaseEggs(level, pos, state);
        }
    }

    private boolean canDestroyEgg(ServerLevel level, Entity entity) {
        if (!(entity instanceof Alligator) && !(entity.is(NaturalistEntityTypeTags.SAFE_EGG_WALKERS))) {
            if (!(entity instanceof LivingEntity)) {
                return false;
            } else {
                return entity instanceof Player || level.getGameRules().get(GameRules.MOB_GRIEFING);
            }
        } else {
            return false;
        }
    }

    private void decreaseEggs(@NotNull Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, NaturalistSoundEvents.GATOR_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + level.getRandom().nextFloat() * 0.2f);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }
}
