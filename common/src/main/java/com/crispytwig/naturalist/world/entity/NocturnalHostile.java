package com.crispytwig.naturalist.world.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface NocturnalHostile {
    float DARKNESS_THRESHOLD = 0.5F;

    default boolean isInDarkness() {
        return ((LivingEntity) this).getLightLevelDependentMagicValue() < DARKNESS_THRESHOLD;
    }

    default boolean isNightTime() {
        return ((LivingEntity) this).level().isDarkOutside();
    }

    default boolean isDarkOrNight() {
        return this.isInDarkness() || this.isNightTime();
    }

    static boolean isAttackablePlayer(LivingEntity target) {
        return target instanceof Player player && !player.isCreative() && !player.isSpectator();
    }
}
