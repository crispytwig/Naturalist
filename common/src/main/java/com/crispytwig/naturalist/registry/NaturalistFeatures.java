package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import com.crispytwig.naturalist.world.level.levelgen.feature.AntHillFeature;

public class NaturalistFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, Naturalist.MOD_ID);

    public static final DeferredHolder<Feature<?>, AntHillFeature> ANT_HILL_SMALL = FEATURES.register("ant_hill_small", () -> new AntHillFeature(false));
    public static final DeferredHolder<Feature<?>, AntHillFeature> ANT_HILL_BIG = FEATURES.register("ant_hill_big", () -> new AntHillFeature(true));
}
