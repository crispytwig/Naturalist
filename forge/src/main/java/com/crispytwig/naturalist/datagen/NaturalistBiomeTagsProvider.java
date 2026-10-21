package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.tags.NaturalistBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NaturalistBiomeTagsProvider extends BiomeTagsProvider {
    private static final TagKey<Biome> VARIANT_TIGER_BLACK_PANTHER = TagKey.create(Registries.BIOME, Naturalist.location("variant/tiger/black_panther"));
    private static final TagKey<Biome> VARIANT_TIGER_LEOPARD = TagKey.create(Registries.BIOME, Naturalist.location("variant/tiger/leopard"));
    private static final TagKey<Biome> VARIANT_TIGER_WHITE_TIGER = TagKey.create(Registries.BIOME, Naturalist.location("variant/tiger/white_tiger"));
    private static final TagKey<Biome> VARIANT_TORTOISE_BLACK = TagKey.create(Registries.BIOME, Naturalist.location("variant/tortoise/black"));
    private static final TagKey<Biome> ATMOSPHERIC_IS_RAINFOREST = TagKey.create(Registries.BIOME, new ResourceLocation("atmospheric", "is_rainforest"));
    private static final ResourceLocation ATMOSPHERIC_KOUSA_JUNGLE = new ResourceLocation("atmospheric", "kousa_jungle");

    private static TagKey<Biome> c(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation("c", name));
    }

    private static ResourceLocation bop(String name) {
        return new ResourceLocation("biomesoplenty", name);
    }

    private static ResourceLocation terralith(String name) {
        return new ResourceLocation("terralith", name);
    }

    public NaturalistBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(VARIANT_TIGER_BLACK_PANTHER)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DARK_FOREST)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"));

        tag(VARIANT_TIGER_LEOPARD)
                .add(Biomes.SPARSE_JUNGLE)
                .addTag(BiomeTags.IS_MOUNTAIN).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .addOptional(bop("rainforest"))
                .addOptional(terralith("cloud_forest"));

        tag(VARIANT_TIGER_WHITE_TIGER)
                .add(Biomes.BAMBOO_JUNGLE).add(Biomes.CHERRY_GROVE).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE)
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("wintry_forest"));

        tag(VARIANT_TORTOISE_BLACK)
                .addTag(BiomeTags.IS_JUNGLE)
                .add(Biomes.DARK_FOREST);

        tag(NaturalistBiomeTags.HAS_ALLIGATOR)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp")).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("ice_marsh")).addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis")).addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_ANGLERFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_ANT_HILL)
                .addOptionalTag(BiomeTags.IS_JUNGLE).addOptionalTag(BiomeTags.IS_SAVANNA);

        tag(NaturalistBiomeTags.HAS_BASS)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("ice_marsh")).addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis")).addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_BEAR)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_TAIGA)
                .addOptionalTag(c("taiga"))
                .addOptional(bop("coniferous_forest")).addOptional(bop("forested_field")).addOptional(bop("orchard"))
                .addOptional(bop("pumpkin_patch")).addOptional(bop("seasonal_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("fir_clearing")).addOptional(bop("maple_woods"))
                .addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("birch_taiga")).addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands")).addOptional(terralith("lavender_forest"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("snowy_maple_forest")).addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("wintry_forest")).addOptional(terralith("yellowstone"));

        tag(NaturalistBiomeTags.HAS_BLACK_BEAR)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA)
                .add(Biomes.GROVE)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_TAIGA)
                .addOptionalTag(c("taiga"))
                .addOptional(bop("coniferous_forest")).addOptional(bop("forested_field")).addOptional(bop("orchard"))
                .addOptional(bop("pumpkin_patch")).addOptional(bop("seasonal_forest")).addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_BLOBFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_BLUEJAY)
                .addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_HILL)
                .add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES)
                .addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy")).addOptionalTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(c("mountain")).addOptionalTag(Tags.Biomes.IS_CONIFEROUS)
                .addOptionalTag(c("tree_coniferous")).addOptionalTag(BiomeTags.IS_TAIGA).addOptionalTag(c("taiga"))
                .addOptionalTag(c("icy")).addOptionalTag(BiomeTags.IS_HILL).addOptionalTag(c("extreme_hills"))
                .addOptional(ATMOSPHERIC_KOUSA_JUNGLE)
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("coniferous_forest")).addOptional(bop("highland"))
                .addOptional(bop("jade_cliffs")).addOptional(bop("maple_woods")).addOptional(bop("snowy_maple_woods"))
                .addOptional(bop("moor")).addOptional(bop("muskeg"))
                .addOptional(terralith("alpine_grove")).addOptional(terralith("alpine_highlands")).addOptional(terralith("birch_taiga"))
                .addOptional(terralith("emerald_peaks")).addOptional(terralith("forested_highlands"))
                .addOptional(terralith("haze_mountain")).addOptional(terralith("highlands")).addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains")).addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("stony_spires")).addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs")).addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_BOAR)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(bop("woodland"))
                .addOptional(terralith("brushland")).addOptional(terralith("cold_shrubland"))
                .addOptional(terralith("forested_highlands")).addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley")).addOptional(terralith("shrubland"))
                .addOptional(terralith("temperate_highlands"));

        tag(NaturalistBiomeTags.HAS_BUTTERFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp")).addOptionalTag(BiomeTags.IS_FOREST)
                .addOptionalTag(c("forest")).addOptionalTag(Tags.Biomes.IS_PLAINS).addOptionalTag(c("plains"))
                .addOptional(bop("bayou")).addOptional(bop("field")).addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("pasture")).addOptional(bop("prairie"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland")).addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_CANARY)
                .addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(BiomeTags.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(c("mountain"))
                .addOptionalTag(BiomeTags.IS_HILL).addOptionalTag(c("extreme_hills"))
                .addOptional(bop("crag")).addOptional(bop("jade_cliffs")).addOptional(bop("moor"))
                .addOptional(terralith("alpine_grove")).addOptional(terralith("alpine_highlands"))
                .addOptional(terralith("emerald_peaks")).addOptional(terralith("haze_mountain")).addOptional(terralith("highlands"))
                .addOptional(terralith("painted_mountains")).addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains")).addOptional(terralith("stony_spires"))
                .addOptional(terralith("temperate_highlands")).addOptional(terralith("white_cliffs"));

        tag(NaturalistBiomeTags.HAS_CAPYBARA)
                .add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE).add(Biomes.BAMBOO_JUNGLE).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("rainforest")).addOptional(bop("wetland"));

        tag(NaturalistBiomeTags.HAS_CARDINAL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(c("savanna")).addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptionalTag(Tags.Biomes.IS_SANDY).addOptionalTag(c("desert")).addOptionalTag(c("badlands"))
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("lavender_field")).addOptional(bop("maple_woods"))
                .addOptional(bop("mystic_grove")).addOptional(bop("orchard")).addOptional(bop("overgrown_greens"))
                .addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland")).addOptional(terralith("desert_oasis")).addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley")).addOptional(terralith("lush_desert"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("red_oasis")).addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley")).addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_CATFISH)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("ice_marsh")).addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis")).addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_CLAM)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_CORAL_SNAKE)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_RIVER)
                .add(Biomes.BEACH).add(Biomes.STONY_SHORE)
                .addOptionalTag(BiomeTags.IS_BEACH).addOptionalTag(c("beach")).addOptionalTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(c("jungle")).addOptionalTag(BiomeTags.IS_RIVER).addOptionalTag(c("river"))
                .addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest")).addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("rocky_jungle")).addOptional(terralith("tropical_jungle")).addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_CRAB)
                .add(Biomes.BEACH).add(Biomes.STONY_SHORE)
                .addOptionalTag(BiomeTags.IS_BEACH).addOptionalTag(c("beach"));

        tag(NaturalistBiomeTags.HAS_DEER)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.CHERRY_GROVE)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest"))
                .addOptional(bop("orchard")).addOptional(bop("pasture")).addOptional(bop("redwood_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("snowy_fir_clearing"))
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods")).addOptional(bop("lavender_field"))
                .addOptional(bop("mystic_grove"))
                .addOptional(terralith("birch_taiga")).addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley")).addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands")).addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley")).addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley")).addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley")).addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("temperate_highlands")).addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_DESERT_SCORPION)
                .add(Biomes.DESERT)
                .addTag(BiomeTags.IS_BADLANDS)
                .addOptional(bop("lush_desert"));

        tag(NaturalistBiomeTags.HAS_DRAGONFLY)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("ice_marsh")).addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis")).addOptional(terralith("red_oasis"));

        tag(NaturalistBiomeTags.HAS_DUCK)
                .add(Biomes.SWAMP)
                .addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove")).addOptional(bop("orchard"))
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_valley")).addOptional(terralith("brushland")).addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_valley")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("sakura_valley")).addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_ELEPHANT)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_FINCH)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(c("savanna"))
                .addOptional(bop("lavender_field")).addOptional(bop("mediterranean_forest")).addOptional(bop("scrubland"))
                .addOptional(terralith("brushland")).addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("savanna_badlands")).addOptional(terralith("savanna_slopes")).addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_FIREFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains")).addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptionalTag(Tags.Biomes.IS_MUSHROOM).addOptionalTag(c("mushroom"))
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("pasture")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland")).addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland")).addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_GIANT_ISOPOD)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_GIRAFFE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_GREAT_WHITE_SHARK)
                .add(Biomes.WARM_OCEAN).add(Biomes.LUKEWARM_OCEAN).add(Biomes.DEEP_LUKEWARM_OCEAN);

        tag(NaturalistBiomeTags.HAS_HEDGEHOG)
                .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.MEADOW)
                .add(Biomes.TAIGA)
                .addOptional(bop("coniferous_forest")).addOptional(bop("field")).addOptional(bop("forested_field"))
                .addOptional(bop("grassland")).addOptional(bop("lavender_field")).addOptional(bop("orchard"))
                .addOptional(bop("pasture")).addOptional(bop("pumpkin_patch")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_HIPPO)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna")).addOptionalTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(c("jungle")).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(terralith("amethyst_rainforest")).addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("rocky_jungle")).addOptional(terralith("tropical_jungle")).addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_JELLYFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_JUNGLE_SCORPION)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptional(bop("rainforest"));

        tag(NaturalistBiomeTags.HAS_KOMODO_DRAGON)
                .addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.DESERT).add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE)
                .addOptional(bop("lush_desert")).addOptional(bop("mediterranean_forest")).addOptional(bop("old_growth_woodland"))
                .addOptional(bop("scrubland")).addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_LION)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_LIZARD)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp")).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(c("desert")).addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest"))
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna")).addOptionalTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(c("jungle"))
                .addOptional(bop("field")).addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest")).addOptional(terralith("brushland"))
                .addOptional(terralith("desert_canyon")).addOptional(terralith("desert_oasis")).addOptional(terralith("desert_spires"))
                .addOptional(terralith("hot_shrubland")).addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("lush_desert")).addOptional(terralith("rocky_jungle")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes")).addOptional(terralith("shrubland")).addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_MAMMOTH)
                .add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_PEAKS)
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_coniferous_forest"))
                .addOptional(bop("snowy_fir_clearing")).addOptional(bop("snowy_maple_woods"));

        tag(NaturalistBiomeTags.HAS_MOLE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.MEADOW)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains"))
                .addOptional(bop("grassland")).addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_OSTRICH)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptional(bop("mediterranean_forest")).addOptional(bop("old_growth_woodland")).addOptional(bop("scrubland"))
                .addOptional(bop("woodland"));

        tag(NaturalistBiomeTags.HAS_PIRANHA)
                .addOptionalTag(BiomeTags.IS_JUNGLE).addOptionalTag(c("jungle"))
                .add(Biomes.LUSH_CAVES);

        tag(NaturalistBiomeTags.HAS_RAT)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains"))
                .addOptional(bop("grassland")).addOptional(bop("lavender_field")).addOptional(bop("orchard"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"));

        tag(NaturalistBiomeTags.HAS_RATTLESNAKE)
                .addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_SANDY).addOptionalTag(c("desert")).addOptionalTag(c("badlands"))
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna")).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("lush_desert")).addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands")).addOptional(terralith("desert_canyon")).addOptional(terralith("desert_oasis"))
                .addOptional(terralith("desert_spires")).addOptional(terralith("gravel_desert")).addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lush_desert")).addOptional(terralith("painted_mountains")).addOptional(terralith("red_oasis"))
                .addOptional(terralith("savanna_badlands")).addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_RAY)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_RHINO)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.HAS_ROBIN)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(c("mountain")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains"))
                .addOptional(bop("field")).addOptional(bop("orchard")).addOptional(bop("overgrown_greens")).addOptional(bop("pasture"))
                .addOptional(bop("pumpkin_patch")).addOptional(bop("seasonal_forest"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("cloud_forest")).addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("temperate_highlands")).addOptional(terralith("white_cliffs"));

        tag(NaturalistBiomeTags.HAS_SNAIL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_HILL)
                .addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.LUSH_CAVES).add(Biomes.DRIPSTONE_CAVES)
                .add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(c("savanna")).addOptionalTag(BiomeTags.IS_RIVER).addOptionalTag(c("river"))
                .addOptionalTag(BiomeTags.IS_HILL).addOptionalTag(c("extreme_hills")).addOptionalTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(c("mountain")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains")).addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptionalTag(Tags.Biomes.IS_UNDERGROUND).addOptionalTag(c("underground")).addOptionalTag(Tags.Biomes.IS_MUSHROOM)
                .addOptionalTag(c("mushroom"))
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("orchard")).addOptional(bop("wetland"))
                .addOptional(bop("woodland"))
                .addOptional(terralith("alpine_grove")).addOptional(terralith("blooming_valley")).addOptional(terralith("brushland"))
                .addOptional(terralith("cloud_forest")).addOptional(terralith("forested_highlands")).addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_forest")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove")).addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp")).addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland")).addOptional(terralith("temperate_highlands")).addOptional(terralith("warm_river"))
                .addOptional(terralith("wintry_forest")).addOptional(terralith("yellowstone"));

        tag(NaturalistBiomeTags.HAS_SNAKE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(BiomeTags.IS_FOREST).addOptionalTag(c("forest")).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(c("plains")).addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp"))
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland")).addOptional(bop("lavender_field"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland")).addOptional(terralith("forested_highlands"))
                .addOptional(terralith("hot_shrubland")).addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley")).addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley")).addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove")).addOptional(terralith("sakura_valley")).addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistBiomeTags.HAS_SPARROW)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_PLAINS).addOptionalTag(c("plains"))
                .addOptional(bop("mystic_grove")).addOptional(bop("prairie")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau")).addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland")).addOptional(terralith("cold_shrubland")).addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_valley")).addOptional(terralith("shrubland"));

        tag(NaturalistBiomeTags.HAS_STARFISH)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_TIGER)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_MOUNTAIN).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DARK_FOREST).add(Biomes.DESERT).add(Biomes.CHERRY_GROVE)
                .add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES).add(Biomes.GROVE)
                .addOptionalTag(BiomeTags.IS_JUNGLE).addOptionalTag(c("jungle")).addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptionalTag(c("swamp"))
                .addOptional(bop("rainforest")).addOptional(bop("mystic_grove")).addOptional(bop("bayou"))
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("cloud_forest")).addOptional(terralith("orchid_swamp")).addOptional(terralith("wintry_forest"));

        tag(NaturalistBiomeTags.HAS_TORTOISE)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(c("swamp")).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(c("desert")).addOptionalTag(BiomeTags.IS_JUNGLE).addOptionalTag(c("jungle"))
                .addOptional(bop("lush_desert")).addOptional(bop("rainforest")).addOptional(bop("bayou"))
                .addOptional(terralith("amethyst_rainforest")).addOptional(terralith("desert_oasis"))
                .addOptional(terralith("lush_desert")).addOptional(terralith("orchid_swamp")).addOptional(terralith("red_oasis"))
                .addOptional(terralith("rocky_jungle")).addOptional(terralith("tropical_jungle"));

        tag(NaturalistBiomeTags.HAS_TURKEY)
                .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.GROVE)
                .addOptional(bop("fir_clearing")).addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("pasture")).addOptional(bop("snowy_fir_clearing"));

        tag(NaturalistBiomeTags.HAS_VULTURE)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna")).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(c("desert"))
                .addOptional(bop("dryland")).addOptional(bop("highland")).addOptional(bop("lush_desert"))
                .addOptional(bop("lush_savanna")).addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands")).addOptional(terralith("ashen_savanna")).addOptional(terralith("bryce_canyon"))
                .addOptional(terralith("desert_canyon")).addOptional(terralith("desert_spires")).addOptional(terralith("gravel_desert"))
                .addOptional(terralith("hot_shrubland")).addOptional(terralith("lush_desert"))
                .addOptional(terralith("painted_mountains")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes")).addOptional(terralith("warped_mesa"));

        tag(NaturalistBiomeTags.HAS_WHALE)
                .addOptionalTag(BiomeTags.IS_OCEAN).addOptionalTag(c("ocean"));

        tag(NaturalistBiomeTags.HAS_ZEBRA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.IS_SAVANNA).addOptionalTag(c("savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna")).addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistBiomeTags.BLACKLIST_ALLIGATOR)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_ANGLERFISH)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_BASS);

        tag(NaturalistBiomeTags.BLACKLIST_BEAR)
                .addOptionalTag(Tags.Biomes.IS_HOT).addOptionalTag(c("climate_hot")).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST);

        tag(NaturalistBiomeTags.BLACKLIST_BLACK_BEAR)
                .addOptionalTag(Tags.Biomes.IS_HOT).addOptionalTag(c("climate_hot")).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST);

        tag(NaturalistBiomeTags.BLACKLIST_BLOBFISH)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_BLUEJAY);

        tag(NaturalistBiomeTags.BLACKLIST_BOAR);

        tag(NaturalistBiomeTags.BLACKLIST_BUTTERFLY)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_CANARY);

        tag(NaturalistBiomeTags.BLACKLIST_CAPYBARA);

        tag(NaturalistBiomeTags.BLACKLIST_CARDINAL);

        tag(NaturalistBiomeTags.BLACKLIST_CATFISH);

        tag(NaturalistBiomeTags.BLACKLIST_CLAM)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_CORAL_SNAKE)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_CRAB)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_DEER);

        tag(NaturalistBiomeTags.BLACKLIST_DESERT_SCORPION);

        tag(NaturalistBiomeTags.BLACKLIST_DRAGONFLY)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_DUCK);

        tag(NaturalistBiomeTags.BLACKLIST_ELEPHANT)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_FINCH);

        tag(NaturalistBiomeTags.BLACKLIST_FIREFLY)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_FOREST_FOXES);

        tag(NaturalistBiomeTags.BLACKLIST_FOREST_RABBITS);

        tag(NaturalistBiomeTags.BLACKLIST_GIANT_ISOPOD)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_GIRAFFE)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_GREAT_WHITE_SHARK);

        tag(NaturalistBiomeTags.BLACKLIST_HEDGEHOG);

        tag(NaturalistBiomeTags.BLACKLIST_HIPPO)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_JELLYFISH)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_JUNGLE_SCORPION);

        tag(NaturalistBiomeTags.BLACKLIST_KOMODO_DRAGON);

        tag(NaturalistBiomeTags.BLACKLIST_LION)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_LIZARD)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_MAMMOTH);

        tag(NaturalistBiomeTags.BLACKLIST_MOLE)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_OSTRICH);

        tag(NaturalistBiomeTags.BLACKLIST_PIRANHA);

        tag(NaturalistBiomeTags.BLACKLIST_RAT);

        tag(NaturalistBiomeTags.BLACKLIST_RATTLESNAKE);

        tag(NaturalistBiomeTags.BLACKLIST_RAY)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_RHINO)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_ROBIN);

        tag(NaturalistBiomeTags.BLACKLIST_SNAIL)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_SNAKE)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_SPARROW);

        tag(NaturalistBiomeTags.BLACKLIST_STARFISH)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"))
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_TIGER);

        tag(NaturalistBiomeTags.BLACKLIST_TORTOISE)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));

        tag(NaturalistBiomeTags.BLACKLIST_TURKEY);

        tag(NaturalistBiomeTags.BLACKLIST_VULTURE);

        tag(NaturalistBiomeTags.BLACKLIST_WHALE)
                .add(Biomes.FROZEN_OCEAN).add(Biomes.DEEP_FROZEN_OCEAN);

        tag(NaturalistBiomeTags.BLACKLIST_ZEBRA)
                .addOptionalTag(c("icy")).addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(c("snowy"));
    }
}
