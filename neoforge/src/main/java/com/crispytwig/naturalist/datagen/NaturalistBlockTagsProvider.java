package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NaturalistBlockTagsProvider extends BlockTagsProvider {
    public NaturalistBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(NaturalistBlockTags.FIREFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.DRAGONFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS);

        tag(NaturalistBlockTags.BUTTERFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS);

        tag(NaturalistBlockTags.VULTURES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.AIR)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAND);

        tag(NaturalistBlockTags.DUCKS_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD, Blocks.DIRT, Blocks.WATER);

        tag(NaturalistBlockTags.RHINO_CHARGE_BREAKABLE)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS, Blocks.FERN, Blocks.TALL_GRASS, Blocks.LARGE_FERN);

        tag(NaturalistBlockTags.VULTURE_PERCH_BLOCKS)
                .add(Blocks.CACTUS)
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.CATTAIL_PLACEABLE)
                .add(Blocks.MUD, Blocks.DIRT, Blocks.GRASS_BLOCK);

        tag(NaturalistBlockTags.ALLIGATOR_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.MUD);

        tag(NaturalistBlockTags.TORTOISE_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.MUD);

        tag(NaturalistBlockTags.MOTHS_ATTRACTED_TO)
                .add(Blocks.TORCH, Blocks.GLOWSTONE, Blocks.SEA_LANTERN, Blocks.JACK_O_LANTERN,
                        Blocks.BEACON, Blocks.END_ROD, Blocks.SHROOMLIGHT, Blocks.CAMPFIRE,
                        Blocks.SOUL_CAMPFIRE, Blocks.LANTERN, Blocks.SOUL_LANTERN);

        tag(NaturalistBlockTags.SHELLSTONE)
                .add(NaturalistRegistry.SHELLSTONE.get())
                .add(NaturalistRegistry.SHELLSTONE_STAIRS.get())
                .add(NaturalistRegistry.SHELLSTONE_SLAB.get())
                .add(NaturalistRegistry.SHELLSTONE_WALL.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICKS.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_WALL.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_SLAB.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_WALL.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get());
    }
}
