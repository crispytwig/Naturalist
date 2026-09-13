package com.crispytwig.naturalist.world.level.block;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class OstrichEggBlock extends TurtleEggBlock {
    private static final VoxelShape EGG_AABB = Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);

    public OstrichEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!this.shouldUpdateHatchLevel(level)) {
            return;
        }
        int hatchStage = state.getValue(HATCH);
        if (hatchStage < 2) {
            level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_CRACK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
            level.setBlock(pos, state.setValue(HATCH, hatchStage + 1), 2);
        } else {
            level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
            level.removeBlock(pos, false);
            level.levelEvent(2001, pos, Block.getId(state));
            Ostrich baby = NaturalistEntityTypes.OSTRICH.get().create(level, EntitySpawnReason.BREEDING);
            if (baby != null) {
                baby.setAge(-24000);
                baby.snapTo(pos.getX() + 0.3, pos.getY(), pos.getZ() + 0.3, 0.0F, 0.0F);
                level.addFreshEntity(baby);
            }
        }
    }

    @Override
    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(@NotNull Level level) {
        return level.getRandom().nextInt(3) == 0;
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

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide() && !player.getAbilities().instabuild) {
            this.angerNearbyOstriches(level, pos, player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyEgg(@NotNull Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
        if (!(level instanceof ServerLevel serverLevel) || !this.canDestroyEgg(serverLevel, entity)) {
            return;
        }
        if (level.getRandom().nextInt(chance) == 0 && state.is(this)) {
            level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + level.getRandom().nextFloat() * 0.2f);
            level.destroyBlock(pos, false);
            if (entity instanceof Player player) {
                this.angerNearbyOstriches(level, pos, player);
            }
        }
    }

    private void angerNearbyOstriches(@NotNull Level level, BlockPos pos, Player player) {
        for (Ostrich ostrich : level.getEntitiesOfClass(Ostrich.class, new AABB(pos).inflate(16.0, 8.0, 16.0),
                entity -> !entity.isBaby() && !entity.isTame() && entity.owns(pos))) {
            ostrich.onOwnedEggDestroyed(pos, player);
        }
    }

    private boolean canDestroyEgg(@NotNull ServerLevel level, Entity entity) {
        if (entity instanceof Ostrich || entity.is(NaturalistEntityTypeTags.SAFE_EGG_WALKERS)) {
            return false;
        }
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        return entity instanceof Player || level.getGameRules().get(GameRules.MOB_GRIEFING);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return EGG_AABB;
    }
}
