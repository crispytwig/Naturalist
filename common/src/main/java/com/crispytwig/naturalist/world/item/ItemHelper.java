package com.crispytwig.naturalist.world.item;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    public static void consume(@NotNull ItemStack stack, int amount, @Nullable LivingEntity entity) {
        if (!(entity instanceof Player player) || !player.getAbilities().instabuild) {
            stack.shrink(amount);
        }
    }

    public static @NotNull CompoundTag copyTag(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? new CompoundTag() : tag.copy();
    }

    public static void mergeTag(@NotNull ItemStack stack, @NotNull CompoundTag data) {
        stack.getOrCreateTag().merge(data);
    }

    public static void updateTag(@NotNull ItemStack stack, @NotNull Consumer<CompoundTag> updater) {
        updater.accept(stack.getOrCreateTag());
    }

    public static void spawnItemOnEntity(@NotNull LivingEntity entity, ItemStack stack) {
        Level level = entity.level();

        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, true, false);
            }
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
            itemEntity.setPickUpDelay(0);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
            level.addFreshEntity(itemEntity);
        }
    }
}
