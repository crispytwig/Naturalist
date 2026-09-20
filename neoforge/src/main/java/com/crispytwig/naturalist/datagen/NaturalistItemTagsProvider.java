package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class NaturalistItemTagsProvider extends ItemTagsProvider {
    public NaturalistItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Naturalist.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(itemTag("c:animal_foods"))
                .addTag(itemTag("naturalist:alligator_food_items"))
                .addTag(itemTag("naturalist:bear_tempt_items"))
                .addTag(itemTag("naturalist:bird_food_items"))
                .addTag(itemTag("naturalist:boar_food_items"))
                .addTag(itemTag("naturalist:capybara_food_items"))
                .addTag(itemTag("naturalist:crab_food"))
                .addTag(itemTag("naturalist:duck_food_items"))
                .addTag(itemTag("naturalist:giant_isopod_food"))
                .addTag(itemTag("naturalist:giraffe_food_items"))
                .addTag(itemTag("naturalist:great_white_shark_food_items"))
                .addTag(itemTag("naturalist:hedgehog_food_items"))
                .addTag(itemTag("naturalist:komodo_dragon_food_items"))
                .addTag(itemTag("naturalist:lion_food_items"))
                .addTag(itemTag("naturalist:lizard_tempt_items"))
                .addTag(itemTag("naturalist:ostrich_food_items"))
                .addTag(itemTag("naturalist:rat_food"))
                .addTag(itemTag("naturalist:snake_tempt_items"))
                .addTag(itemTag("naturalist:tiger_food_items"))
                .addTag(itemTag("naturalist:tortoise_tempt_items"))
                .addTag(itemTag("naturalist:turkey_food_items"));

        tag(itemTag("c:buckets/entity_water"))
                .add(item("naturalist:anglerfish_bucket"))
                .add(item("naturalist:bass_bucket"))
                .add(item("naturalist:blobfish_bucket"))
                .add(item("naturalist:catfish_bucket"))
                .add(item("naturalist:giant_isopod_bucket"))
                .add(item("naturalist:jellyfish_bucket"))
                .add(item("naturalist:piranha_bucket"))
                .add(item("naturalist:ray_bucket"))
                .add(item("naturalist:starfish_bucket"));

        tag(itemTag("c:buckets"))
                .add(item("naturalist:duck_bucket"));

        tag(itemTag("c:eggs"))
                .add(item("naturalist:tortoise_egg"))
                .add(item("naturalist:alligator_egg"))
                .add(item("naturalist:ostrich_egg"))
                .add(item("naturalist:duck_egg"));

        tag(itemTag("c:foods/cooked_fish"))
                .add(item("naturalist:cooked_anglerfish"))
                .add(item("naturalist:cooked_bass"))
                .add(item("naturalist:cooked_blobfish"))
                .add(item("naturalist:cooked_catfish"))
                .add(item("naturalist:cooked_piranha"));

        tag(itemTag("c:foods/cooked_meat"))
                .add(item("naturalist:cooked_bushmeat"))
                .add(item("naturalist:cooked_clam_meat"))
                .add(item("naturalist:cooked_crab_meat"))
                .add(item("naturalist:cooked_drumstick"))
                .add(item("naturalist:cooked_duck"))
                .add(item("naturalist:cooked_lizard_tail"))
                .add(item("naturalist:cooked_mammoth_meat"))
                .add(item("naturalist:cooked_venison"));

        tag(itemTag("c:foods/food_poisoning"))
                .add(item("naturalist:lizard_tail"));

        tag(itemTag("c:foods/raw_fish"))
                .add(item("naturalist:anglerfish"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:blobfish"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:piranha"));

        tag(itemTag("c:foods/raw_meat"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:clam_meat"))
                .add(item("naturalist:crab_meat"))
                .add(item("naturalist:drumstick"))
                .add(item("naturalist:duck"))
                .add(item("naturalist:lizard_tail"))
                .add(item("naturalist:mammoth_meat"))
                .add(item("naturalist:morsel"))
                .add(item("naturalist:venison"));

        tag(itemTag("c:foods"))
                .add(item("naturalist:cooked_egg"));

        tag(itemTag("c:glass_blocks"))
                .add(item("naturalist:azure_froglass"))
                .add(item("naturalist:crimson_froglass"))
                .add(item("naturalist:verdant_froglass"));

        tag(itemTag("c:glass_panes"))
                .add(item("naturalist:azure_froglass_pane"))
                .add(item("naturalist:crimson_froglass_pane"))
                .add(item("naturalist:verdant_froglass_pane"));

        tag(itemTag("c:music_discs"))
                .add(item("naturalist:music_disc_death_by_hogs"))
                .add(item("naturalist:music_disc_wild_ones"));

        tag(itemTag("c:tools"))
                .add(item("naturalist:capture_net"));

        tag(itemTag("minecraft:axolotl_food"))
                .add(item("naturalist:bass_bucket"))
                .add(item("naturalist:catfish_bucket"))
                .add(item("naturalist:anglerfish_bucket"))
                .add(item("naturalist:blobfish_bucket"))
                .add(item("naturalist:piranha_bucket"));

        tag(itemTag("minecraft:cat_food"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:anglerfish"))
                .add(item("naturalist:blobfish"))
                .add(item("naturalist:piranha"));

        tag(itemTag("minecraft:fishes"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:cooked_bass"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:cooked_catfish"))
                .add(item("naturalist:anglerfish"))
                .add(item("naturalist:cooked_anglerfish"))
                .add(item("naturalist:blobfish"))
                .add(item("naturalist:cooked_blobfish"))
                .add(item("naturalist:piranha"))
                .add(item("naturalist:cooked_piranha"));

        tag(itemTag("minecraft:meat"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:cooked_bushmeat"))
                .add(item("naturalist:venison"))
                .add(item("naturalist:cooked_venison"))
                .add(item("naturalist:drumstick"))
                .add(item("naturalist:cooked_drumstick"))
                .add(item("naturalist:duck"))
                .add(item("naturalist:cooked_duck"))
                .add(item("naturalist:mammoth_meat"))
                .add(item("naturalist:cooked_mammoth_meat"))
                .add(item("naturalist:lizard_tail"))
                .add(item("naturalist:cooked_lizard_tail"))
                .add(item("naturalist:crab_meat"))
                .add(item("naturalist:cooked_crab_meat"))
                .add(item("naturalist:clam_meat"))
                .add(item("naturalist:cooked_clam_meat"))
                .add(item("naturalist:morsel"));

        tag(itemTag("minecraft:ocelot_food"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:anglerfish"))
                .add(item("naturalist:blobfish"))
                .add(item("naturalist:piranha"));

        tag(itemTag("minecraft:piglin_food"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:cooked_bushmeat"));

        tag(itemTag("minecraft:slabs"))
                .add(item("naturalist:shellstone_slab"))
                .add(item("naturalist:shellstone_brick_slab"))
                .add(item("naturalist:cut_shellstone_slab"))
                .add(item("naturalist:smooth_shellstone_slab"));

        tag(itemTag("minecraft:stairs"))
                .add(item("naturalist:shellstone_stairs"))
                .add(item("naturalist:shellstone_brick_stairs"))
                .add(item("naturalist:cut_shellstone_stairs"))
                .add(item("naturalist:smooth_shellstone_stairs"));

        tag(itemTag("minecraft:walls"))
                .add(item("naturalist:shellstone_wall"))
                .add(item("naturalist:shellstone_brick_wall"))
                .add(item("naturalist:cut_shellstone_wall"))
                .add(item("naturalist:smooth_shellstone_wall"));

        tag(NaturalistItemTags.CAPYBARA_FOOD_ITEMS)
                .add(item("minecraft:melon_slice"));

        tag(NaturalistItemTags.GIANT_ISOPOD_FOOD)
                .add(item("minecraft:cod"))
                .add(item("minecraft:cooked_cod"))
                .add(item("minecraft:salmon"))
                .add(item("minecraft:cooked_salmon"))
                .add(item("minecraft:tropical_fish"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:cooked_catfish"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:cooked_bass"));

        tag(NaturalistItemTags.GREAT_WHITE_SHARK_FOOD_ITEMS)
                .add(item("minecraft:cod"))
                .add(item("minecraft:cooked_cod"))
                .add(item("minecraft:salmon"))
                .add(item("minecraft:cooked_salmon"))
                .add(item("minecraft:tropical_fish"))
                .add(item("naturalist:catfish"))
                .add(item("naturalist:cooked_catfish"))
                .add(item("naturalist:bass"))
                .add(item("naturalist:cooked_bass"));

        tag(NaturalistItemTags.HEDGEHOG_FOOD_ITEMS)
                .add(item("minecraft:sweet_berries"))
                .add(item("minecraft:glow_berries"));

        tag(NaturalistItemTags.KOMODO_DRAGON_FOOD_ITEMS)
                .addTag(itemTag("c:foods/raw_meat"))
                .addTag(itemTag("c:foods/cooked_meat"))
                .add(item("naturalist:venison"))
                .add(item("naturalist:cooked_venison"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:cooked_bushmeat"))
                .add(item("naturalist:duck"))
                .add(item("naturalist:cooked_duck"))
                .add(item("naturalist:drumstick"))
                .add(item("naturalist:cooked_drumstick"))
                .add(item("naturalist:morsel"))
                .add(item("naturalist:mammoth_meat"))
                .add(item("naturalist:cooked_mammoth_meat"));

        tag(NaturalistItemTags.LION_FOOD_ITEMS)
                .addTag(itemTag("c:foods/raw_meat"))
                .addTag(itemTag("c:foods/cooked_meat"))
                .add(item("naturalist:venison"))
                .add(item("naturalist:cooked_venison"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:cooked_bushmeat"))
                .add(item("naturalist:duck"))
                .add(item("naturalist:cooked_duck"));

        tag(NaturalistItemTags.OSTRICH_FOOD_ITEMS)
                .add(item("minecraft:wheat_seeds"))
                .add(item("minecraft:beetroot_seeds"))
                .add(item("minecraft:melon_seeds"))
                .add(item("minecraft:pumpkin_seeds"))
                .add(item("minecraft:pitcher_pod"))
                .add(item("minecraft:torchflower_seeds"));

        tag(NaturalistItemTags.SNAKE_TAME_ITEMS)
                .add(item("minecraft:spider_eye"))
                .add(item("minecraft:rabbit"))
                .add(item("minecraft:cooked_rabbit"));

        tag(NaturalistItemTags.TIGER_FOOD_ITEMS)
                .addTag(itemTag("c:foods/raw_meat"))
                .addTag(itemTag("c:foods/cooked_meat"))
                .add(item("naturalist:venison"))
                .add(item("naturalist:cooked_venison"))
                .add(item("naturalist:bushmeat"))
                .add(item("naturalist:cooked_bushmeat"))
                .add(item("naturalist:duck"))
                .add(item("naturalist:cooked_duck"))
                .add(item("naturalist:drumstick"))
                .add(item("naturalist:cooked_drumstick"))
                .add(item("naturalist:morsel"))
                .add(item("naturalist:mammoth_meat"))
                .add(item("naturalist:cooked_mammoth_meat"));

        tag(NaturalistItemTags.TURKEY_FOOD_ITEMS)
                .add(item("minecraft:wheat_seeds"))
                .add(item("minecraft:beetroot_seeds"))
                .add(item("minecraft:melon_seeds"))
                .add(item("minecraft:pumpkin_seeds"))
                .add(item("minecraft:pitcher_pod"))
                .add(item("minecraft:torchflower_seeds"));

        tag(NaturalistItemTags.ALLIGATOR_FOOD_ITEMS)
                .add(Items.BEEF.builtInRegistryHolder().key(), Items.PORKCHOP.builtInRegistryHolder().key(), Items.CHICKEN.builtInRegistryHolder().key(), Items.RABBIT.builtInRegistryHolder().key());

        tag(NaturalistItemTags.BEAR_FURS)
                .add(NaturalistRegistry.FUR.get().builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("alexsmobs", "bear_fur")));

        tag(NaturalistItemTags.BEAR_TEMPT_ITEMS)
                .add(Items.SALMON.builtInRegistryHolder().key(), Items.COOKED_SALMON.builtInRegistryHolder().key(), Items.HONEYCOMB.builtInRegistryHolder().key(), Items.SWEET_BERRIES.builtInRegistryHolder().key())
                .add(NaturalistRegistry.VENISON.get().builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("alexsmobs", "raw_moose_ribs")))
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("alexsmobs", "cooked_moose_ribs")));

        tag(NaturalistItemTags.BIRD_FOOD_ITEMS)
                .add(Items.WHEAT_SEEDS.builtInRegistryHolder().key(), Items.PUMPKIN_SEEDS.builtInRegistryHolder().key(), Items.MELON_SEEDS.builtInRegistryHolder().key(), Items.BEETROOT_SEEDS.builtInRegistryHolder().key());

        tag(NaturalistItemTags.BOAR_FOOD_ITEMS)
                .add(Items.CARROT.builtInRegistryHolder().key(), Items.POTATO.builtInRegistryHolder().key(), Items.BEETROOT.builtInRegistryHolder().key());

        tag(NaturalistItemTags.CRAB_FOOD)
                .add(Items.TROPICAL_FISH.builtInRegistryHolder().key());

        tag(NaturalistItemTags.RAT_FOOD)
                .add(Items.BREAD.builtInRegistryHolder().key());

        tag(NaturalistItemTags.DUCK_FOOD_ITEMS)
                .addTag(Tags.Items.FOODS_RAW_FISH);

        tag(NaturalistItemTags.EGGS)
                .add(NaturalistRegistry.TORTOISE_EGG.get().asItem().builtInRegistryHolder().key())
                .add(NaturalistRegistry.ALLIGATOR_EGG.get().asItem().builtInRegistryHolder().key())
                .add(NaturalistRegistry.DUCK_EGG.get().builtInRegistryHolder().key())
                .add(Items.EGG.builtInRegistryHolder().key());

        tag(NaturalistItemTags.GIRAFFE_FOOD_ITEMS)
                .add(Items.APPLE.builtInRegistryHolder().key(), Items.GOLDEN_APPLE.builtInRegistryHolder().key(), Items.HAY_BLOCK.builtInRegistryHolder().key());

        tag(NaturalistItemTags.LIZARD_TEMPT_ITEMS)
                .add(Items.SPIDER_EYE.builtInRegistryHolder().key());

        tag(NaturalistItemTags.SHEARS)
                .add(Items.SHEARS.builtInRegistryHolder().key())
                .addOptionalTag(Tags.Items.TOOLS_SHEAR);

        tag(NaturalistItemTags.SNAKE_TEMPT_ITEMS)
                .add(Items.CHICKEN.builtInRegistryHolder().key(), Items.RABBIT.builtInRegistryHolder().key(), Items.RABBIT_FOOT.builtInRegistryHolder().key(), Items.EGG.builtInRegistryHolder().key());

        tag(NaturalistItemTags.TORTOISE_TEMPT_ITEMS)
                .add(Items.BAMBOO.builtInRegistryHolder().key(), Items.DANDELION.builtInRegistryHolder().key(), Items.BROWN_MUSHROOM.builtInRegistryHolder().key(), Items.CACTUS.builtInRegistryHolder().key());
    }

    private static ResourceKey<Item> item(String id) {
        return ResourceKey.create(Registries.ITEM, Identifier.parse(id));
    }

    private static TagKey<Item> itemTag(String id) {
        return TagKey.create(Registries.ITEM, Identifier.parse(id));
    }
}
