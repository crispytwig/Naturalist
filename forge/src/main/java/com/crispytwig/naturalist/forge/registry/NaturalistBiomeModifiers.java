package com.crispytwig.naturalist.forge.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.modifiers.AddAnimalsBiomeModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Naturalist.MOD_ID);

    public static final RegistryObject<Codec<AddAnimalsBiomeModifier>> ADD_ANIMALS_CODEC =
            BIOME_MODIFIERS.register("add_animals", () -> Codec.unit(AddAnimalsBiomeModifier::new));
}
