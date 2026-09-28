package com.crispytwig.naturalist.server.item;

import com.crispytwig.naturalist.server.entity.base.Catchable;
import com.crispytwig.naturalist.server.entity.base.ContainerBoundWorker;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.server.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CaughtEntityItem extends NaturalistBucketItem {
    private final Supplier<? extends EntityType<?>> typeSup;

    public CaughtEntityItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        this(entitySupplier, soundSupplier, null, null, properties);
    }

    public CaughtEntityItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, @Nullable String tooltipPrefix, @Nullable String[] variantNames, Properties properties) {
        super(entitySupplier.get(), Fluids.EMPTY, soundSupplier.get(), properties, true, tooltipPrefix, variantNames);
        this.typeSup = entitySupplier;
    }

    public EntityType<?> type() {
        return this.typeSup.get();
    }

    public ItemStack capture(@NotNull Mob mob) {
        CompoundTag entityTag = new CompoundTag();
        mob.save(entityTag);
        ItemStack stack = new ItemStack(this);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
        return stack;
    }

    public static InteractionResult giveCaught(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack net, @NotNull Mob mob, @NotNull CaughtEntityItem item) {
        ItemStack caught = item.capture(mob);
        BugNetItem.swing(player.level(), player);
        BugNetItem.playCaughtEffects(player.level(), mob);
        net.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        if (!player.getInventory().add(caught)) {
            ItemHelper.spawnItemOnEntity(player, caught);
        }
        mob.discard();
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private void spawn(ServerLevel serverLevel, ItemStack itemStack, @Nullable Player player, BlockPos pos) {
        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Entity entity;
        if (tag.contains("id")) {
            VoxelShape shape = serverLevel.getBlockState(pos).getCollisionShape(serverLevel, pos);
            double y = pos.getY() + (shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y));
            entity = EntityType.loadEntityRecursive(tag, serverLevel, e -> {
                e.moveTo(pos.getX() + 0.5D, y, pos.getZ() + 0.5D, e.getYRot(), e.getXRot());
                if (e instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
                return e;
            });
            if (entity != null) {
                serverLevel.addFreshEntity(entity);
            }
        } else {
            Consumer<Entity> config = EntityType.<Entity>createDefaultStackConfig(serverLevel, itemStack, player).andThen(e -> {
                if (e instanceof Catchable catchable) {
                    catchable.loadFromHandTag(tag);
                    catchable.setFromHand(true);
                } else if (e instanceof DataDrivenVariantAnimal variantAnimal && tag.contains(DataDrivenVariantAnimal.VARIANT_TAG)) {
                    variantAnimal.loadVariant(tag);
                }
                if (e instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
            });
            entity = ((EntityType<Entity>) this.type()).spawn(serverLevel, config, pos, MobSpawnType.BUCKET, true, false);
        }

        if (entity instanceof ContainerBoundWorker worker) {
            worker.tryAssignWorkstation(pos);
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            this.spawn(serverLevel, containerStack, player, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, itemstack)) {
                this.checkExtraContent(player, level, itemstack, pos);
                this.playEmptySound(player, level, pos);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, player), level.isClientSide());
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public static @NotNull ItemStack getEmptySuccessItem(@NotNull ItemStack bucketStack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.AIR) : bucketStack;
    }
}
