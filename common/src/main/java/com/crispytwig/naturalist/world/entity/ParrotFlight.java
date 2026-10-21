package com.crispytwig.naturalist.world.entity;

import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.world.entity.animal.bird.Bird;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public final class ParrotFlight {
    public static boolean shoulderShouldFlap;
    public static float shoulderPartialTick;

    private ParrotFlight() {
    }

    public static boolean hasBirdOnHead(Player player) {
        for (Entity passenger : player.getPassengers()) {
            if (passenger instanceof Bird) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParrotOnBothShoulders(Player player) {
        return isParrot(player.getShoulderEntityLeft()) && isParrot(player.getShoulderEntityRight());
    }

    public static boolean canAscend(Player player) {
        return NaturalistConfig.isParrotFlightEnabled() && hasParrotOnBothShoulders(player) && hasBirdOnHead(player);
    }

    private static boolean isParrot(CompoundTag tag) {
        return !tag.isEmpty() && EntityType.byString(tag.getString("id")).filter(type -> type == EntityType.PARROT).isPresent();
    }
}
