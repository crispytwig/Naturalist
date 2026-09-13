package com.crispytwig.naturalist.world.level.block;

import com.crispytwig.naturalist.registry.NaturalistBlockEntities;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.world.level.block.entity.AntHillBlockEntity;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AntHillBlock extends Block implements EntityBlock {
    public static final int MAX_WORKERS = 3;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final IntegerProperty WORKERS = IntegerProperty.create("workers", 0, MAX_WORKERS);
    public static final BooleanProperty HAS_QUEEN = BooleanProperty.create("has_queen");

    public AntHillBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false).setValue(WORKERS, 0).setValue(HAS_QUEEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(OPEN, WORKERS, HAS_QUEEN);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AntHillBlockEntity(pos, state);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide() || !state.getValue(OPEN) || type != NaturalistBlockEntities.ANT_HILL.get()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<AntHillBlockEntity>) AntHillBlockEntity::serverTick;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(OPEN) && state.getValue(WORKERS) > 0;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextFloat() >= 0.07F) {
            return;
        }
        BlockPos releasePos = findReleasePos(level, pos, random);
        if (releasePos == null) {
            return;
        }
        level.setBlock(pos, state.setValue(WORKERS, state.getValue(WORKERS) - 1), 3);
        playEnterLeaveEffects(level, pos, random);
        releaseAnt(level, releasePos, random, getOwner(level, pos));
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (level instanceof ServerLevel serverLevel) {
            int workers = state.getValue(WORKERS);
            UUID owner = getOwner(serverLevel, pos);
            serverLevel.playSound(null, pos, SoundType.AZALEA.getBreakSound(), SoundSource.BLOCKS, 0.9F, 1.0F);
            if (workers > 0 || state.getValue(HAS_QUEEN)) {
                spawnPoofParticles(serverLevel, pos);
            }
            for (int i = 0; i < workers; i++) {
                Ant ant = releaseAnt(serverLevel, pos, serverLevel.getRandom(), owner);
                if (ant != null && !ant.isOwnedBy(player)) {
                    ant.lookAt(player, 360.0F, 360.0F);
                    ant.setPersistentAngerTarget(EntityReference.of(player));
                    ant.startPersistentAngerTimer();
                    ant.setTarget(player);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public static boolean canAntEnter(LevelReader level, BlockPos pos, @Nullable UUID antOwner) {
        return canAntStore(level, pos, antOwner) && level.getBlockState(pos).getValue(WORKERS) < MAX_WORKERS;
    }

    public static boolean canAntStore(LevelReader level, BlockPos pos, @Nullable UUID antOwner) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof AntHillBlock
                && state.getValue(OPEN)
                && level.getBlockEntity(pos) instanceof AntHillBlockEntity hill
                && Objects.equals(hill.getOwner(), antOwner);
    }

    public static void storeFood(ServerLevel level, BlockPos pos, Ant ant) {
        ItemEntity carried = ant.getCarriedFood();
        if (carried == null || !canAntStore(level, pos, ant.getOwnerUUID()) || !(level.getBlockEntity(pos) instanceof AntHillBlockEntity hill)) {
            return;
        }
        ItemStack leftover = hill.storeFood(carried.getItem().copy());
        ant.consumeCarriedFood();
        if (!leftover.isEmpty()) {
            popResource(level, pos, leftover);
        }
        playEnterLeaveEffects(level, pos, ant.getRandom());
    }

    public static boolean tryEnter(ServerLevel level, BlockPos pos, Ant ant) {
        if (!canAntEnter(level, pos, ant.getOwnerUUID())) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        level.setBlock(pos, state.setValue(WORKERS, state.getValue(WORKERS) + 1), 3);
        playEnterLeaveEffects(level, pos, ant.getRandom());
        ant.discard();
        return true;
    }

    @Nullable
    private static UUID getOwner(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof AntHillBlockEntity hill ? hill.getOwner() : null;
    }

    @Nullable
    public static BlockPos findReleasePos(ServerLevel level, BlockPos pos, RandomSource random) {
        List<BlockPos> validPositions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockState(neighbor).isAir() && !level.getBlockState(neighbor.below()).isAir()) {
                validPositions.add(neighbor);
            }
        }
        return validPositions.isEmpty() ? null : validPositions.get(random.nextInt(validPositions.size()));
    }

    @Nullable
    public static Ant releaseAnt(ServerLevel level, BlockPos pos, RandomSource random, @Nullable UUID owner) {
        Ant ant = NaturalistEntityTypes.ANT.get().create(level, EntitySpawnReason.TRIGGERED);
        if (ant == null) {
            return null;
        }
        ant.snapTo(pos.getX() + 0.25D + random.nextDouble() * 0.5D, pos.getY(), pos.getZ() + 0.25D + random.nextDouble() * 0.5D, random.nextFloat() * 360.0F, 0.0F);
        ant.startHillCooldown();
        if (owner != null) {
            ant.setTame(true, false);
            ant.setOwnerUUID(owner);
        }
        level.addFreshEntity(ant);
        return ant;
    }

    public static void playEnterLeaveEffects(ServerLevel level, BlockPos pos, RandomSource random) {
        level.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 0.9F, 0.9F + random.nextFloat() * 0.2F);
        spawnPoofParticles(level, pos);
    }

    private static void spawnPoofParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 8, 0.25D, 0.25D, 0.25D, 0.0D);
    }
}
