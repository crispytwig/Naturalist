package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class NaturalistDamageTypes {
    public static final ResourceKey<DamageType> HEDGEHOG_THROW = ResourceKey.create(Registries.DAMAGE_TYPE, Naturalist.location("hedgehog_throw"));

    private NaturalistDamageTypes() {
    }
}
