package com.crispytwig.naturalist.world.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantaneousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AntivenomMobEffect extends InstantaneousMobEffect {
    public AntivenomMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantaneousEffect(@NotNull ServerLevel level, @Nullable Entity source, @Nullable Entity indirectSource, @NotNull LivingEntity target, int amplifier, double health) {
        target.removeEffect(MobEffects.POISON);
    }
}
