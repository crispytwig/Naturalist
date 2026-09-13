package com.crispytwig.naturalist.world.level.block;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.block.entity.SnailShellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class SnailShellBlock extends Block implements EntityBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    private static Map<Block, Block> POTTED_BY_CONTENT;

    public SnailShellBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, DyeColor.BROWN).setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(COLOR, ROTATION);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SnailShellBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        DyeColor color = DyeColor.BROWN;
        CustomData data = context.getItemInHand().get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            color = tag.getInt("Color").map(DyeColor::byId).orElse(color);
        }
        return this.defaultBlockState().setValue(COLOR, color).setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof SnailShellBlockEntity shell) || getPottedBlock(stack) == null || !shell.getFlower().isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!level.isClientSide()) {
            shell.setFlower(stack.copyWithCount(1));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            SoundType soundType = ((BlockItem) stack.getItem()).getBlock().defaultBlockState().getSoundType();
            level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(getShellStack(state.getValue(COLOR)));
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SnailShellBlockEntity shell && !shell.getFlower().isEmpty()) {
            drops.add(shell.getFlower().copy());
        }
        return drops;
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), 16));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
    }

    public static CustomData colorData(DyeColor color) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Color", color.getId());
        return CustomData.of(tag);
    }

    public static ItemStack getShellStack(DyeColor color) {
        ItemStack stack = new ItemStack(NaturalistRegistry.SNAIL_SHELL.get());
        stack.set(DataComponents.CUSTOM_DATA, colorData(color));
        return stack;
    }

    @Nullable
    public static Block getPottedBlock(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem ? getPottedBlock(blockItem.getBlock()) : null;
    }

    @Nullable
    public static Block getPottedBlock(Block content) {
        if (content.builtInRegistryHolder().is(NaturalistBlockTags.SNAIL_SHELL_BLACKLIST)) {
            return null;
        }
        if (POTTED_BY_CONTENT == null) {
            Map<Block, Block> map = new IdentityHashMap<>();
            for (Block block : BuiltInRegistries.BLOCK) {
                if (block instanceof FlowerPotBlock pot && pot.getPotted() != Blocks.AIR) {
                    map.put(pot.getPotted(), pot);
                }
            }
            POTTED_BY_CONTENT = map;
        }
        return POTTED_BY_CONTENT.get(content);
    }
}
