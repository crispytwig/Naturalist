package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class NaturalistBlockTagsProvider extends BlockTagsProvider {
    public NaturalistBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Naturalist.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(blockTag("c:glass_blocks"))
                .add(block("naturalist:azure_froglass"))
                .add(block("naturalist:crimson_froglass"))
                .add(block("naturalist:verdant_froglass"));

        tag(blockTag("c:glass_panes"))
                .add(block("naturalist:azure_froglass_pane"))
                .add(block("naturalist:crimson_froglass_pane"))
                .add(block("naturalist:verdant_froglass_pane"));

        tag(blockTag("minecraft:impermeable"))
                .add(block("naturalist:crimson_froglass"))
                .add(block("naturalist:azure_froglass"))
                .add(block("naturalist:verdant_froglass"));

        tag(blockTag("minecraft:mineable/pickaxe"))
                .addTag(blockTag("naturalist:shellstone"));

        tag(blockTag("minecraft:slabs"))
                .add(block("naturalist:shellstone_slab"))
                .add(block("naturalist:shellstone_brick_slab"))
                .add(block("naturalist:cut_shellstone_slab"))
                .add(block("naturalist:smooth_shellstone_slab"));

        tag(blockTag("minecraft:stairs"))
                .add(block("naturalist:shellstone_stairs"))
                .add(block("naturalist:shellstone_brick_stairs"))
                .add(block("naturalist:cut_shellstone_stairs"))
                .add(block("naturalist:smooth_shellstone_stairs"));

        tag(blockTag("minecraft:walls"))
                .add(block("naturalist:shellstone_brick_wall"))
                .add(block("naturalist:shellstone_wall"))
                .add(block("naturalist:cut_shellstone_wall"))
                .add(block("naturalist:smooth_shellstone_wall"));

        tag(NaturalistBlockTags.KOMODO_DRAGONS_SPAWNABLE_ON)
                .add(block("minecraft:grass_block"))
                .addTag(blockTag("minecraft:sand"))
                .add(block("minecraft:terracotta"))
                .add(block("minecraft:orange_terracotta"));

        tag(NaturalistBlockTags.OSTRICH_EGG_LAYABLE_ON)
                .addTag(blockTag("minecraft:sand"))
                .addTag(blockTag("minecraft:substrate_overworld"));

        tag(NaturalistBlockTags.SCORPIONS_SPAWNABLE_ON)
                .add(block("minecraft:grass_block"))
                .addTag(blockTag("minecraft:sand"))
                .add(block("minecraft:terracotta"))
                .add(block("minecraft:orange_terracotta"))
                .add(block("minecraft:podzol"))
                .add(block("minecraft:coarse_dirt"))
                .add(block("minecraft:moss_block"));

        tag(NaturalistBlockTags.SNAIL_SHELL_BLACKLIST)
                .add(block("minecraft:cactus"))
                .add(block("minecraft:bamboo"));

        tag(NaturalistBlockTags.FIREFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key())
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.DRAGONFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key())
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.BUTTERFLIES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key())
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.VULTURES_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.AIR.builtInRegistryHolder().key())
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAND);

        tag(NaturalistBlockTags.DUCKS_SPAWNABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key(), Blocks.DIRT.builtInRegistryHolder().key(), Blocks.WATER.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.RHINO_CHARGE_BREAKABLE)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS.builtInRegistryHolder().key(), Blocks.FERN.builtInRegistryHolder().key(), Blocks.TALL_GRASS.builtInRegistryHolder().key(), Blocks.LARGE_FERN.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.VULTURE_PERCH_BLOCKS)
                .add(Blocks.CACTUS.builtInRegistryHolder().key())
                .addTag(BlockTags.LEAVES);

        tag(NaturalistBlockTags.CATTAIL_PLACEABLE)
                .add(Blocks.MUD.builtInRegistryHolder().key(), Blocks.DIRT.builtInRegistryHolder().key(), Blocks.GRASS_BLOCK.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.ALLIGATOR_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.DIRT.builtInRegistryHolder().key(), Blocks.SAND.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.TORTOISE_EGG_LAYABLE_ON)
                .add(Blocks.GRASS_BLOCK.builtInRegistryHolder().key(), Blocks.DIRT.builtInRegistryHolder().key(), Blocks.SAND.builtInRegistryHolder().key(), Blocks.MUD.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.MOTHS_ATTRACTED_TO)
                .add(Blocks.TORCH.builtInRegistryHolder().key(), Blocks.GLOWSTONE.builtInRegistryHolder().key(), Blocks.SEA_LANTERN.builtInRegistryHolder().key(), Blocks.JACK_O_LANTERN.builtInRegistryHolder().key(),
                        Blocks.BEACON.builtInRegistryHolder().key(), Blocks.END_ROD.builtInRegistryHolder().key(), Blocks.SHROOMLIGHT.builtInRegistryHolder().key(), Blocks.CAMPFIRE.builtInRegistryHolder().key(),
                        Blocks.SOUL_CAMPFIRE.builtInRegistryHolder().key(), Blocks.LANTERN.builtInRegistryHolder().key(), Blocks.SOUL_LANTERN.builtInRegistryHolder().key());

        tag(NaturalistBlockTags.SHELLSTONE)
                .add(NaturalistRegistry.SHELLSTONE.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_STAIRS.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_SLAB.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_WALL.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_BRICKS.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_WALL.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.CUT_SHELLSTONE.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.CUT_SHELLSTONE_SLAB.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.CUT_SHELLSTONE_WALL.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get().builtInRegistryHolder().key())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get().builtInRegistryHolder().key());
    }

    private static ResourceKey<Block> block(String id) {
        return ResourceKey.create(Registries.BLOCK, Identifier.parse(id));
    }

    private static TagKey<Block> blockTag(String id) {
        return TagKey.create(Registries.BLOCK, Identifier.parse(id));
    }
}
