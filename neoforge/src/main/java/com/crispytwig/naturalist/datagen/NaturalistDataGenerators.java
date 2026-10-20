package com.crispytwig.naturalist.datagen;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

public class NaturalistDataGenerators {
    public static void gatherClientData(GatherDataEvent.Client event) {
        gatherData(event);
    }

    public static void gatherServerData(GatherDataEvent.Server event) {
        gatherData(event);
    }

    private static void gatherData(GatherDataEvent event) {
        event.createProvider(NaturalistBlockTagsProvider::new);
        event.createProvider(NaturalistItemTagsProvider::new);
        event.createProvider(NaturalistEntityTypeTagsProvider::new);
        event.createProvider(NaturalistBiomeTagsProvider::new);
        event.createProvider(NaturalistPaintingVariantTagsProvider::new);
    }

    public static void gatherRegistryEntries(GatherDataRegistryEntriesEvent event) {
        event.gatherFor("naturalist", "minecraft")
                .add(Registries.LOOT_TABLE, NaturalistLootTableProvider.create()::run)
                .add(NaturalistRecipeProvider.create());
    }
}
