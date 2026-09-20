package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import com.crispytwig.naturalist.world.level.levelgen.feature.AntHillFeature;

public class NaturalistFeatures {
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE_TYPE, Naturalist.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<AntHillFeature>> ANT_HILL_SMALL = FEATURES.register("ant_hill_small", () -> AntHillFeature.SMALL_CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<AntHillFeature>> ANT_HILL_BIG = FEATURES.register("ant_hill_big", () -> AntHillFeature.BIG_CODEC);
}
