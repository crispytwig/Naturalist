package com.crispytwig.naturalist.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import java.util.Set;

public class NaturalistDataGenerators {
    public static void gatherClientData(GatherDataEvent.Client event) {
    }

    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider(NaturalistBlockTagsProvider::new);
        event.createProvider(NaturalistItemTagsProvider::new);
        event.createProvider(NaturalistEntityTypeTagsProvider::new);
        event.createProvider(NaturalistBiomeTagsProvider::new);
        event.createProvider(NaturalistPaintingVariantTagsProvider::new);

        event.createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(Registries.LOOT_TABLE, NaturalistLootTableProvider.create()::run)
                .add(NaturalistRecipeProvider.create()), Set.of("naturalist", "minecraft"));
    }
}
