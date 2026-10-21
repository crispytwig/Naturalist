package com.crispytwig.naturalist.world.item;

import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Supplier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.nbt.Tag;
import net.minecraft.core.registries.BuiltInRegistries;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.LegacyVariantRemap;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.world.entity.variant.MobVariantUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class NaturalistBucketItem extends BucketItem {
    private final Supplier<? extends EntityType<?>> entityType;
    private final Supplier<? extends SoundEvent> emptySound;
    private final boolean noFluid;
    private final boolean allowMidWater;
    private final @Nullable String tooltipPrefix;
    private final @Nullable String[] variantNames;

    public NaturalistBucketItem(Supplier<? extends EntityType<?>> entityType, Fluid content, Supplier<? extends SoundEvent> emptySound, Properties properties) {
        this(entityType, content, emptySound, properties, true, null, null);
    }

    public NaturalistBucketItem(Supplier<? extends EntityType<?>> entityType, Fluid content, Supplier<? extends SoundEvent> emptySound, Properties properties, boolean allowMidWater, @Nullable String tooltipPrefix, @Nullable String[] variantNames) {
        super(content, properties);
        this.entityType = entityType;
        this.emptySound = emptySound;
        this.noFluid = content == Fluids.EMPTY;
        this.allowMidWater = allowMidWater;
        this.tooltipPrefix = tooltipPrefix;
        this.variantNames = variantNames;
    }

    public EntityType<?> getVariantEntityType() {
        return this.entityType.get();
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            this.spawn(serverLevel, containerStack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    protected void playEmptySound(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        level.playSound(player, pos, this.emptySound.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel serverLevel, ItemStack bucketedMobStack, BlockPos pos) {
        Entity entity = this.entityType.get().spawn(serverLevel, bucketedMobStack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(bucketedMobStack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }
    }

    public @Nullable String[] getLegacyVariantNames() {
        return this.variantNames;
    }

    public Optional<ResourceLocation> readVariantId(CompoundTag tag) {
        if (BuiltInRegistries.ENTITY_TYPE.getKey(this.getVariantEntityType()).toString().equals(LegacyVariantRemap.BUTTERFLY_ID)
                && tag.contains(DataDrivenVariantAnimal.VARIANT_TAG, Tag.TAG_ANY_NUMERIC)) {
            return Optional.of(new ResourceLocation(LegacyVariantRemap.legacyButterflyVariant(tag.getInt(DataDrivenVariantAnimal.VARIANT_TAG))));
        }
        return MobVariantUtil.readVariantId(tag, this.variantNames);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.noFluid) {
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hit.getType() != HitResult.Type.BLOCK) {
                return InteractionResultHolder.pass(stack);
            }
            BlockPos pos = hit.getBlockPos();
            Direction direction = hit.getDirection();
            if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos.relative(direction), direction, stack)) {
                return InteractionResultHolder.fail(stack);
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
        player.setItemInHand(context.getHand(), release(level, player, context.getItemInHand(), placePos).getObject());
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResultHolder<ItemStack> release(Level level, Player player, ItemStack stack, BlockPos pos) {
        this.checkExtraContent(player, level, stack, pos);
        this.playEmptySound(player, level, pos);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        CompoundTag tag = ItemHelper.copyTag(stack);
        ResourceLocation id = this.readVariantId(tag).orElse(null);
        if (id == null) {
            return;
        }
        HolderLookup.Provider registries = context != null ? context.registryAccess() : null;
        if (registries != null) {
            Optional<Component> custom = NaturalistMobVariants.findRegistry(this.getVariantEntityType())
                    .flatMap(registry -> MobVariantUtil.byId(registries, registry, id))
                    .flatMap(holder -> holder.value().tooltip());
            if (custom.isPresent()) {
                tooltip.add(custom.get().copy().withStyle(ChatFormatting.GRAY));
                return;
            }
        }
        if (this.tooltipPrefix != null) {
            tooltip.add(Component.translatable(this.tooltipPrefix + id.getPath()).withStyle(ChatFormatting.GRAY));
        }
    }
}
