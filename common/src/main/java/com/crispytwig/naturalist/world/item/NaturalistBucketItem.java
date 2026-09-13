package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.world.entity.variant.MobVariantUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class NaturalistBucketItem extends MobBucketItem {
    private final EntityType<?> variantEntityType;
    private final boolean noFluid;
    private final boolean allowMidWater;
    private final @Nullable String tooltipPrefix;
    private final @Nullable String[] variantNames;

    public NaturalistBucketItem(EntityType<?> entityType, Fluid content, SoundEvent emptySound, Properties properties) {
        this(entityType, content, emptySound, properties, true, null, null);
    }

    @SuppressWarnings("unchecked")
    public NaturalistBucketItem(EntityType<?> entityType, Fluid content, SoundEvent emptySound, Properties properties, boolean allowMidWater, @Nullable String tooltipPrefix, @Nullable String[] variantNames) {
        super((EntityType<? extends Mob>) entityType, content, emptySound, properties);
        this.variantEntityType = entityType;
        this.noFluid = content == Fluids.EMPTY;
        this.allowMidWater = allowMidWater;
        this.tooltipPrefix = tooltipPrefix;
        this.variantNames = variantNames;
    }

    public EntityType<?> getVariantEntityType() {
        return this.variantEntityType;
    }

    public @Nullable String[] getLegacyVariantNames() {
        return this.variantNames;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.noFluid) {
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hit.getType() != HitResult.Type.BLOCK) {
                return InteractionResult.PASS;
            }
            BlockPos pos = hit.getBlockPos();
            Direction direction = hit.getDirection();
            if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos.relative(direction), direction, stack)) {
                return InteractionResult.FAIL;
            }
            return release(level, player, stack, pos);
        }
        if (this.allowMidWater) {
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hit.getType() == HitResult.Type.BLOCK && level.getFluidState(hit.getBlockPos()).is(FluidTags.WATER)) {
                return release(level, player, stack, hit.getBlockPos());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (this.noFluid || player == null) {
            return super.useOn(context);
        }
        BlockPos clicked = context.getClickedPos();
        BlockPos placePos = level.getBlockState(clicked).getCollisionShape(level, clicked).isEmpty()
                ? clicked
                : clicked.relative(context.getClickedFace());
        if (!level.getFluidState(placePos).is(FluidTags.WATER) || !level.mayInteract(player, placePos)) {
            return super.useOn(context);
        }
        ItemStack stack = context.getItemInHand();
        this.checkExtraContent(player, level, stack, placePos);
        this.playEmptySound(player, level, placePos);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.setItemInHand(context.getHand(), getEmptySuccessItem(stack, player));
        return InteractionResult.SUCCESS;
    }

    private InteractionResult release(Level level, Player player, ItemStack stack, BlockPos pos) {
        this.checkExtraContent(player, level, stack, pos);
        this.playEmptySound(player, level, pos);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS.heldItemTransformedTo(getEmptySuccessItem(stack, player));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Identifier id = MobVariantUtil.readVariantId(tag, this.variantNames).orElse(null);
        if (id == null) {
            return;
        }
        HolderLookup.Provider registries = context.registries();
        if (registries != null) {
            Optional<Component> custom = NaturalistMobVariants.findRegistry(this.variantEntityType)
                    .flatMap(registry -> MobVariantUtil.byId(registries, registry, id))
                    .flatMap(holder -> holder.value().tooltip());
            if (custom.isPresent()) {
                tooltip.accept(custom.get().copy().withStyle(ChatFormatting.GRAY));
                return;
            }
        }
        if (this.tooltipPrefix != null) {
            tooltip.accept(Component.translatable(this.tooltipPrefix + id.getPath()).withStyle(ChatFormatting.GRAY));
        }
    }
}
