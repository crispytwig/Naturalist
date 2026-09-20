package com.crispytwig.naturalist.world.item;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    public static void spawnItemOnEntity(@NotNull LivingEntity entity, ItemStack stack) {
        Level level = entity.level();

        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                ItemEntity dropped = player.createItemStackToDrop(stack, true, false);
                if (dropped != null) {
                    level.addFreshEntity(dropped);
                }
            }
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
            itemEntity.setPickUpDelay(0);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
            level.addFreshEntity(itemEntity);
        }
    }
}
