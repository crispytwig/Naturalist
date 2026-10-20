package com.crispytwig.naturalist.neoforge.registry;

import com.mojang.serialization.MapCodec;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.modifiers.AddAnimalsBiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NaturalistBiomeModifiers {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Naturalist.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<AddAnimalsBiomeModifier>> ADD_ANIMALS_CODEC =
            BIOME_MODIFIERS.register("add_animals", () -> MapCodec.unit(AddAnimalsBiomeModifier::new));
}
