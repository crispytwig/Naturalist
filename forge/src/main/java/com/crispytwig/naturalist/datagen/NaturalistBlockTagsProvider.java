package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NaturalistBlockTagsProvider extends BlockTagsProvider {
    public NaturalistBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(blockTag("c:glass_blocks"))
                .add(NaturalistRegistry.AZURE_FROGLASS.get())
                .add(NaturalistRegistry.CRIMSON_FROGLASS.get())
                .add(NaturalistRegistry.VERDANT_FROGLASS.get());

        tag(blockTag("c:glass_panes"))
                .add(NaturalistRegistry.AZURE_FROGLASS_PANE.get())
                .add(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get())
                .add(NaturalistRegistry.VERDANT_FROGLASS_PANE.get());

        tag(Tags.Blocks.GLASS)
                .add(NaturalistRegistry.AZURE_FROGLASS.get())
                .add(NaturalistRegistry.CRIMSON_FROGLASS.get())
                .add(NaturalistRegistry.VERDANT_FROGLASS.get());

        tag(Tags.Blocks.GLASS_PANES)
                .add(NaturalistRegistry.AZURE_FROGLASS_PANE.get())
                .add(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get())
                .add(NaturalistRegistry.VERDANT_FROGLASS_PANE.get());

        tag(BlockTags.IMPERMEABLE)
                .add(NaturalistRegistry.CRIMSON_FROGLASS.get())
                .add(NaturalistRegistry.AZURE_FROGLASS.get())
                .add(NaturalistRegistry.VERDANT_FROGLASS.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(NaturalistBlockTags.SHELLSTONE);

        tag(BlockTags.SLABS)
                .add(NaturalistRegistry.SHELLSTONE_SLAB.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_SLAB.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get());

        tag(BlockTags.STAIRS)
                .add(NaturalistRegistry.SHELLSTONE_STAIRS.get())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get());

        tag(BlockTags.WALLS)
                .add(NaturalistRegistry.SHELLSTONE_BRICK_WALL.get())
                .add(NaturalistRegistry.SHELLSTONE_WALL.get())
                .add(NaturalistRegistry.CUT_SHELLSTONE_WALL.get())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get());

        tag(NaturalistBlockTags.ALLIGATOR_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.MUD);

        tag(NaturalistBlockTags.BUTTERFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.GRASS);

        tag(NaturalistBlockTags.CATTAIL_PLACEABLE)
                .add(Blocks.MUD, Blocks.DIRT, Blocks.GRASS_BLOCK);

        tag(NaturalistBlockTags.DRAGONFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.GRASS);

        tag(NaturalistBlockTags.DUCKS_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD, Blocks.DIRT, Blocks.WATER);

        tag(NaturalistBlockTags.FIREFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.MUD)
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.KOMODO_DRAGONS_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK)
                .addTag(BlockTags.SAND)
                .add(Blocks.TERRACOTTA, Blocks.ORANGE_TERRACOTTA);

        tag(NaturalistBlockTags.MOTHS_ATTRACTED_TO)
                .add(Blocks.TORCH, Blocks.GLOWSTONE, Blocks.SEA_LANTERN, Blocks.JACK_O_LANTERN, Blocks.BEACON, Blocks.END_ROD,
                        Blocks.SHROOMLIGHT, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, Blocks.LANTERN, Blocks.SOUL_LANTERN);

        tag(NaturalistBlockTags.OSTRICH_EGG_LAYABLE_ON)
                .addTag(BlockTags.SAND)
                .addTag(BlockTags.DIRT);

        tag(NaturalistBlockTags.RHINO_CHARGE_BREAKABLE)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.GRASS, Blocks.FERN, Blocks.TALL_GRASS, Blocks.LARGE_FERN);

        tag(NaturalistBlockTags.SCORPIONS_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK)
                .addTag(BlockTags.SAND)
                .add(Blocks.TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MOSS_BLOCK);

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

        tag(NaturalistBlockTags.SNAIL_SHELL_BLACKLIST)
                .add(Blocks.CACTUS, Blocks.BAMBOO);

        tag(NaturalistBlockTags.TORTOISE_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.MUD);

        tag(NaturalistBlockTags.VULTURE_PERCH_BLOCKS)
                .add(Blocks.CACTUS)
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.VULTURES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK, Blocks.AIR)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAND);
    }

    private static TagKey<Block> blockTag(String id) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(id));
    }
}
