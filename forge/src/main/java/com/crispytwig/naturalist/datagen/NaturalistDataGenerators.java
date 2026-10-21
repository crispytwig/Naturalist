package com.crispytwig.naturalist.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class NaturalistDataGenerators {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new NaturalistBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new NaturalistItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new NaturalistLanguageProvider(output));
        generator.addProvider(event.includeClient(), new NaturalistSoundDefinitionsProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(), NaturalistLootTableProvider.create(output));
        generator.addProvider(event.includeServer(), new NaturalistRecipeProvider(output));
        generator.addProvider(event.includeServer(), NaturalistAdvancementProvider.create(output, lookupProvider, existingFileHelper));

        NaturalistBlockTagsProvider blockTags = new NaturalistBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new NaturalistItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new NaturalistEntityTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new NaturalistBiomeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new NaturalistPaintingVariantTagsProvider(output, lookupProvider, existingFileHelper));
    }
}
