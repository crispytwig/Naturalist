package com.crispytwig.naturalist.world.level.block;

import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class SnailEggBlock extends Block {
    public static final float HITBOX_WIDTH = 0.4F;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public SnailEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, getSnailEggHatchDelay(level.getRandom()));
    }

    private static int getSnailEggHatchDelay(@NotNull RandomSource random) {
        return random.nextInt(600, 2400);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess ticks, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.hatchSnailEgg(level, pos, random);
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Entity entity, @NotNull InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity.getType().equals(EntityTypes.FALLING_BLOCK)) {
            this.destroyBlock(level, pos);
        }

    }

    private void hatchSnailEgg(ServerLevel level, BlockPos pos, RandomSource random) {
        this.destroyBlock(level, pos);
        level.playSound(null, pos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.spawnBabySnails(level, pos, random);
    }

    private void destroyBlock(Level level, @NotNull BlockPos pos) {
        level.destroyBlock(pos, false);
    }

    private void spawnBabySnails(ServerLevel level, @NotNull BlockPos pos, RandomSource random) {
        int i = random.nextInt(2, 6);

        for(int j = 1; j <= i; ++j) {
            Snail snail = NaturalistEntityTypes.SNAIL.get().create(level, EntitySpawnReason.BREEDING);
            if (snail != null) {
                double d = (double)pos.getX() + this.getRandomSnailPositionOffset(random);
                snail.snapTo(d, pos.getY(), (double)pos.getZ() + this.getRandomSnailPositionOffset(random), (float)random.nextInt(1, 361), 0.0F);
                snail.setPersistenceRequired();
                snail.setAge(-6000);
                level.addFreshEntity(snail);
            }
        }

    }

    private double getRandomSnailPositionOffset(@NotNull RandomSource random) {
        double d = HITBOX_WIDTH / 2.0F;
        return Mth.clamp(random.nextDouble(), d, 1.0 - d);
    }
}
