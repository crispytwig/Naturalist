package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistBiomeTags;
import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import net.minecraft.resources.ResourceKey;

public class NaturalistBiomeTagsProvider extends TagsProvider<Biome> {
    private static final TagKey<Biome> ATMOSPHERIC_IS_RAINFOREST = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("atmospheric", "is_rainforest"));
    private static final ResourceKey<Biome> ATMOSPHERIC_KOUSA_JUNGLE = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("atmospheric", "kousa_jungle"));

    private static ResourceKey<Biome> bop(String name) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("biomesoplenty", name));
    }

    private static ResourceKey<Biome> terralith(String name) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("terralith", name));
    }

    public NaturalistBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(biomeTag("naturalist:variant/tiger/black_panther"))
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DARK_FOREST)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"));

        tag(biomeTag("naturalist:variant/tiger/leopard"))
                .add(Biomes.SPARSE_JUNGLE)
                .addTag(BiomeTags.IS_MOUNTAIN).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .addOptional(bop("rainforest"))
                .addOptional(terralith("cloud_forest"));

        tag(biomeTag("naturalist:variant/tiger/white_tiger"))
                .add(Biomes.BAMBOO_JUNGLE).add(Biomes.CHERRY_GROVE)
                .add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE)
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("wintry_forest"));

        tag(biomeTag("naturalist:variant/tortoise/black"))
                .addTag(BiomeTags.IS_JUNGLE)
                .add(Biomes.DARK_FOREST);

        tag(NaturalistBiomeTags.HAS_ANT_HILL)
                .addOptionalTag(BiomeTags.IS_JUNGLE).addOptionalTag(BiomeTags.IS_SAVANNA);

        tag(NaturalistBiomeTags.HAS_ALLIGATOR)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_BASS)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_BEAR)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_TAIGA)
                .addOptional(bop("coniferous_forest")).addOptional(bop("forested_field"))
                .addOptional(bop("orchard")).addOptional(bop("pumpkin_patch"))
                .addOptional(bop("seasonal_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("fir_clearing"))
                .addOptional(bop("maple_woods")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("wintry_forest"))
                .addOptional(terralith("yellowstone"));

        tag(NaturalistBiomeTags.HAS_BLUEJAY)
                .addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_HILL)
                .add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES)
                .addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(Tags.Biomes.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_CONIFEROUS_TREE).addOptionalTag(Tags.Biomes.IS_TAIGA)
                .addOptionalTag(Tags.Biomes.IS_ICY).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptional(ATMOSPHERIC_KOUSA_JUNGLE)
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("coniferous_forest"))
                .addOptional(bop("highland")).addOptional(bop("jade_cliffs"))
                .addOptional(bop("maple_woods")).addOptional(bop("snowy_maple_woods"))
                .addOptional(bop("moor")).addOptional(bop("muskeg"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("alpine_highlands"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("emerald_peaks"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("haze_mountain"))
                .addOptional(terralith("highlands"))
                .addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("stony_spires"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_BOAR)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland")).addOptional(bop("woodland"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cold_shrubland"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("temperate_highlands"));

        tag(NaturalistBiomeTags.HAS_BUTTERFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("bayou")).addOptional(bop("field"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("pasture"))
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_CANARY)
                .addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptional(bop("crag")).addOptional(bop("jade_cliffs")).addOptional(bop("moor"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("alpine_highlands"))
                .addOptional(terralith("emerald_peaks"))
                .addOptional(terralith("haze_mountain"))
                .addOptional(terralith("highlands"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains"))
                .addOptional(terralith("stony_spires"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"));

        tag(NaturalistBiomeTags.HAS_CARDINAL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_SANDY)
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("lavender_field")).addOptional(bop("maple_woods"))
                .addOptional(bop("mystic_grove")).addOptional(bop("orchard"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_CATFISH)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_CORAL_SNAKE)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_RIVER)
                .add(Biomes.BEACH).add(Biomes.STONY_SHORE)
                .addOptionalTag(Tags.Biomes.IS_BEACH).addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_RIVER)
                .addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_CRAB)
                .add(Biomes.BEACH).add(Biomes.STONY_SHORE)
                .addOptionalTag(Tags.Biomes.IS_BEACH);

        tag(NaturalistBiomeTags.HAS_DEER)
                .addTag(BiomeTags.IS_FOREST).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_FOREST)
                .addOptional(bop("orchard")).addOptional(bop("pasture"))
                .addOptional(bop("redwood_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("snowy_fir_clearing"))
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_DRAGONFLY)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_DUCK)
                .add(Biomes.SWAMP).addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("prairie"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_ELEPHANT)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_FINCH)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("lavender_field")).addOptional(bop("mediterranean_forest"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_FIREFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_MUSHROOM)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("pasture"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_GIANT_ISOPOD)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_JELLYFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_ANGLERFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_RAY)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_BLOBFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_WHALE)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_PIRANHA)
                .addOptionalTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .add(Biomes.LUSH_CAVES);

        tag(NaturalistBiomeTags.HAS_GIRAFFE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_HIPPO)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_LION)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_LIZARD)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptional(bop("field")).addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_MAMMOTH)
                .add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES)
                .add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_PEAKS)
                .addOptional(bop("snowblossom_grove"))
                .addOptional(bop("snowy_coniferous_forest"))
                .addOptional(bop("snowy_fir_clearing"))
                .addOptional(bop("snowy_maple_woods"));

        tag(NaturalistBiomeTags.HAS_MOLE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.MEADOW)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("grassland")).addOptional(bop("lavender_field"))
                .addOptional(bop("mystic_grove")).addOptional(bop("orchard"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_BLACK_BEAR)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).add(Biomes.GROVE)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_TAIGA)
                .addOptional(bop("coniferous_forest")).addOptional(bop("forested_field"))
                .addOptional(bop("orchard")).addOptional(bop("pumpkin_patch"))
                .addOptional(bop("seasonal_forest")).addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_TIGER)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_MOUNTAIN).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DARK_FOREST)
                .add(Biomes.DESERT).add(Biomes.CHERRY_GROVE)
                .add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE).addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("rainforest")).addOptional(bop("mystic_grove"))
                .addOptional(bop("bayou"))
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("cloud_forest")).addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_KOMODO_DRAGON)
                .addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.DESERT).add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE)
                .addOptional(bop("lush_desert")).addOptional(bop("mediterranean_forest"))
                .addOptional(bop("old_growth_woodland")).addOptional(bop("scrubland"))
                .addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_OSTRICH)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptional(bop("mediterranean_forest")).addOptional(bop("old_growth_woodland"))
                .addOptional(bop("scrubland")).addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_DESERT_SCORPION)
                .add(Biomes.DESERT)
                .addTag(BiomeTags.IS_BADLANDS)
                .addOptional(bop("lush_desert"));

        tag(NaturalistBiomeTags.HAS_GREAT_WHITE_SHARK)
                .add(Biomes.WARM_OCEAN)
                .add(Biomes.LUKEWARM_OCEAN)
                .add(Biomes.DEEP_LUKEWARM_OCEAN);

        tag(NaturalistBiomeTags.HAS_TURKEY)
                .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST)
                .add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.GROVE)
                .addOptional(bop("fir_clearing")).addOptional(bop("lavender_field"))
                .addOptional(bop("mystic_grove")).addOptional(bop("pasture"))
                .addOptional(bop("snowy_fir_clearing"));

        tag(NaturalistBiomeTags.HAS_CAPYBARA)
                .add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE).add(Biomes.BAMBOO_JUNGLE)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("rainforest")).addOptional(bop("wetland"));

        tag(NaturalistBiomeTags.HAS_HEDGEHOG)
                .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.MEADOW).add(Biomes.TAIGA)
                .addOptional(bop("coniferous_forest")).addOptional(bop("field"))
                .addOptional(bop("forested_field")).addOptional(bop("grassland"))
                .addOptional(bop("lavender_field")).addOptional(bop("orchard"))
                .addOptional(bop("pasture")).addOptional(bop("pumpkin_patch"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_JUNGLE_SCORPION)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptional(bop("rainforest"));

        tag(NaturalistBiomeTags.HAS_RAT)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("grassland")).addOptional(bop("lavender_field"))
                .addOptional(bop("orchard")).addOptional(bop("overgrown_greens"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_RATTLESNAKE)
                .addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_SANDY).addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("lush_desert"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("gravel_desert"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_RHINO)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_ROBIN)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("field")).addOptional(bop("orchard"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("pasture"))
                .addOptional(bop("pumpkin_patch")).addOptional(bop("seasonal_forest"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"));

        tag(NaturalistBiomeTags.HAS_SNAIL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .add(Biomes.LUSH_CAVES).add(Biomes.DRIPSTONE_CAVES).add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_RIVER).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_UNDERGROUND).addOptionalTag(Tags.Biomes.IS_MUSHROOM)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("orchard")).addOptional(bop("wetland"))
                .addOptional(bop("woodland"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("wintry_forest"))
                .addOptional(terralith("yellowstone"));

        tag(NaturalistBiomeTags.HAS_SNAKE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS).addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("wetland")).addOptional(bop("lavender_field"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_SPARROW)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("mystic_grove")).addOptional(bop("prairie"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cold_shrubland"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_STARFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_CLAM)
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_OCEAN);

        tag(NaturalistBiomeTags.HAS_TORTOISE)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptional(bop("lush_desert")).addOptional(bop("rainforest"))
                .addOptional(bop("bayou"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"));

        tag(NaturalistBiomeTags.HAS_VULTURE)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("dryland")).addOptional(bop("highland"))
                .addOptional(bop("lush_desert")).addOptional(bop("lush_savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("bryce_canyon"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("gravel_desert"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_ZEBRA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        coldBlacklist(NaturalistBiomeTags.BLACKLIST_ALLIGATOR);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_BASS);
        tag(NaturalistBiomeTags.BLACKLIST_BEAR)
                .addOptionalTag(Tags.Biomes.IS_HOT).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST);
        tag(NaturalistBiomeTags.BLACKLIST_BLACK_BEAR)
                .addOptionalTag(Tags.Biomes.IS_HOT).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_BLUEJAY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_BOAR);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_BUTTERFLY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_CANARY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_CARDINAL);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_CATFISH);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_CLAM);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_CORAL_SNAKE);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_CRAB);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_DEER);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_DESERT_SCORPION);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_JUNGLE_SCORPION);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_DRAGONFLY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_DUCK);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_ELEPHANT);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_FINCH);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_FIREFLY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_FOREST_FOXES);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_FOREST_RABBITS);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_GIANT_ISOPOD);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_GREAT_WHITE_SHARK);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_JELLYFISH);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_ANGLERFISH);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_RAY);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_BLOBFISH);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_PIRANHA);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_GIRAFFE);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_HIPPO);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_LION);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_LIZARD);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_MAMMOTH);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_MOLE);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_RAT);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_TIGER);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_KOMODO_DRAGON);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_OSTRICH);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_TURKEY);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_CAPYBARA);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_HEDGEHOG);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_RATTLESNAKE);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_RHINO);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_ROBIN);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_SNAIL);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_SNAKE);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_SPARROW);
        coldMarineBlacklist(NaturalistBiomeTags.BLACKLIST_STARFISH);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_TORTOISE);
        emptyBlacklist(NaturalistBiomeTags.BLACKLIST_VULTURE);
        coldBlacklist(NaturalistBiomeTags.BLACKLIST_ZEBRA);
        tag(NaturalistBiomeTags.BLACKLIST_WHALE).add(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
    }

    private void coldBlacklist(TagKey<Biome> tag) {
        tag(tag).addOptionalTag(Tags.Biomes.IS_ICY).addOptionalTag(Tags.Biomes.IS_SNOWY);
    }

    private void coldMarineBlacklist(TagKey<Biome> tag) {
        tag(tag).addOptionalTag(Tags.Biomes.IS_ICY).addOptionalTag(Tags.Biomes.IS_SNOWY)
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);
    }

    private void emptyBlacklist(TagKey<Biome> tag) {
        tag(tag);
    }

    private static TagKey<Biome> biomeTag(String id) {
        return TagKey.create(Registries.BIOME, Identifier.parse(id));
    }
}
