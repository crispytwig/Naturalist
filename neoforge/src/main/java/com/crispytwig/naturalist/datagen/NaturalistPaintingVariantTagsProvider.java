package com.crispytwig.naturalist.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NaturalistPaintingVariantTagsProvider extends TagsProvider<PaintingVariant> {
    public NaturalistPaintingVariantTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.PAINTING_VARIANT, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(TagKey.create(Registries.PAINTING_VARIANT, Identifier.parse("minecraft:placeable")))
                .add(ResourceKey.create(Registries.PAINTING_VARIANT, Identifier.parse("naturalist:bear_necessities")));
    }
}
