package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NaturalistItemTagsProvider extends ItemTagsProvider {
    public NaturalistItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(NaturalistItemTags.ALLIGATOR_FOOD_ITEMS)
                .add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT);

        tag(NaturalistItemTags.BEAR_FURS)
                .add(NaturalistRegistry.FUR.get())
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "bear_fur"));

        tag(NaturalistItemTags.BEAR_TEMPT_ITEMS)
                .add(Items.SALMON, Items.COOKED_SALMON, Items.HONEYCOMB, Items.SWEET_BERRIES)
                .add(NaturalistRegistry.VENISON.get())
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "raw_moose_ribs"))
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "cooked_moose_ribs"));

        tag(NaturalistItemTags.BIRD_FOOD_ITEMS)
                .add(Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS);

        tag(NaturalistItemTags.BOAR_FOOD_ITEMS)
                .add(Items.CARROT, Items.POTATO, Items.BEETROOT);

        tag(NaturalistItemTags.CRAB_FOOD)
                .add(Items.TROPICAL_FISH);

        tag(NaturalistItemTags.RAT_FOOD)
                .add(Items.BREAD);

        tag(NaturalistItemTags.DUCK_FOOD_ITEMS)
                .addTag(Tags.Items.FOODS_RAW_FISH);

        tag(NaturalistItemTags.EGGS)
                .add(NaturalistRegistry.TORTOISE_EGG.get().asItem())
                .add(NaturalistRegistry.ALLIGATOR_EGG.get().asItem())
                .add(NaturalistRegistry.DUCK_EGG.get())
                .add(Items.EGG);

        tag(NaturalistItemTags.GIRAFFE_FOOD_ITEMS)
                .add(Items.APPLE, Items.GOLDEN_APPLE, Items.HAY_BLOCK);

        tag(NaturalistItemTags.LIZARD_TEMPT_ITEMS)
                .add(Items.SPIDER_EYE);

        tag(NaturalistItemTags.SHEARS)
                .add(Items.SHEARS)
                .addOptionalTag(Tags.Items.TOOLS_SHEAR);

        tag(NaturalistItemTags.SNAKE_TEMPT_ITEMS)
                .add(Items.CHICKEN, Items.RABBIT, Items.RABBIT_FOOT, Items.EGG);

        tag(NaturalistItemTags.TORTOISE_TEMPT_ITEMS)
                .add(Items.BAMBOO, Items.DANDELION, Items.BROWN_MUSHROOM, Items.CACTUS);
    }
}
