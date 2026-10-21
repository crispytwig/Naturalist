package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NaturalistItemTagsProvider extends ItemTagsProvider {
    private static final TagKey<Item> C_FOODS_COOKED_FISH = itemTag("c:foods/cooked_fish");
    private static final TagKey<Item> C_FOODS_COOKED_MEAT = itemTag("c:foods/cooked_meat");
    private static final TagKey<Item> C_FOODS_RAW_FISH = itemTag("c:foods/raw_fish");
    private static final TagKey<Item> C_FOODS_RAW_MEAT = itemTag("c:foods/raw_meat");

    public NaturalistItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(itemTag("c:animal_foods"))
                .addTag(NaturalistItemTags.ALLIGATOR_FOOD)
                .addTag(NaturalistItemTags.BEAR_TEMPT_ITEMS)
                .addTag(NaturalistItemTags.BIRD_FOOD)
                .addTag(NaturalistItemTags.BOAR_FOOD)
                .addTag(NaturalistItemTags.CAPYBARA_FOOD)
                .addTag(NaturalistItemTags.CRAB_FOOD)
                .addTag(NaturalistItemTags.DUCK_FOOD)
                .addTag(NaturalistItemTags.GIANT_ISOPOD_FOOD)
                .addTag(NaturalistItemTags.GIRAFFE_FOOD)
                .addTag(NaturalistItemTags.GREAT_WHITE_SHARK_FOOD)
                .addTag(NaturalistItemTags.HEDGEHOG_FOOD)
                .addTag(NaturalistItemTags.KOMODO_DRAGON_FOOD)
                .addTag(NaturalistItemTags.LION_FOOD)
                .addTag(NaturalistItemTags.LIZARD_TEMPT_ITEMS)
                .addTag(NaturalistItemTags.OSTRICH_FOOD)
                .addTag(NaturalistItemTags.RAT_FOOD)
                .addTag(NaturalistItemTags.SNAKE_TEMPT_ITEMS)
                .addTag(NaturalistItemTags.TIGER_FOOD)
                .addTag(NaturalistItemTags.TORTOISE_TEMPT_ITEMS)
                .addTag(NaturalistItemTags.TURKEY_FOOD);

        tag(itemTag("c:buckets"))
                .add(NaturalistRegistry.DUCK_BUCKET.get());

        tag(itemTag("c:buckets/entity_water"))
                .add(NaturalistRegistry.ANGLERFISH_BUCKET.get())
                .add(NaturalistRegistry.BASS_BUCKET.get())
                .add(NaturalistRegistry.BLOBFISH_BUCKET.get())
                .add(NaturalistRegistry.CATFISH_BUCKET.get())
                .add(NaturalistRegistry.GIANT_ISOPOD_BUCKET.get())
                .add(NaturalistRegistry.JELLYFISH_BUCKET.get())
                .add(NaturalistRegistry.PIRANHA_BUCKET.get())
                .add(NaturalistRegistry.RAY_BUCKET.get())
                .add(NaturalistRegistry.STARFISH_BUCKET.get());

        tag(itemTag("c:cooked_eggs"))
                .add(NaturalistRegistry.COOKED_EGG.get());

        tag(itemTag("c:cooked_fish"))
                .addTag(C_FOODS_COOKED_FISH);

        tag(itemTag("c:cooked_meat"))
                .addTag(C_FOODS_COOKED_MEAT);

        tag(itemTag("c:eggs"))
                .add(NaturalistRegistry.TORTOISE_EGG.get().asItem())
                .add(NaturalistRegistry.ALLIGATOR_EGG.get().asItem())
                .add(NaturalistRegistry.OSTRICH_EGG.get().asItem())
                .add(NaturalistRegistry.DUCK_EGG.get());

        tag(itemTag("c:entity_water_buckets"))
                .add(NaturalistRegistry.ANGLERFISH_BUCKET.get())
                .add(NaturalistRegistry.BASS_BUCKET.get())
                .add(NaturalistRegistry.BLOBFISH_BUCKET.get())
                .add(NaturalistRegistry.CATFISH_BUCKET.get())
                .add(NaturalistRegistry.GIANT_ISOPOD_BUCKET.get())
                .add(NaturalistRegistry.JELLYFISH_BUCKET.get())
                .add(NaturalistRegistry.PIRANHA_BUCKET.get())
                .add(NaturalistRegistry.RAY_BUCKET.get())
                .add(NaturalistRegistry.STARFISH_BUCKET.get());

        tag(itemTag("c:foods"))
                .add(NaturalistRegistry.COOKED_EGG.get());

        tag(C_FOODS_COOKED_FISH)
                .add(NaturalistRegistry.COOKED_ANGLERFISH.get())
                .add(NaturalistRegistry.COOKED_BASS.get())
                .add(NaturalistRegistry.COOKED_BLOBFISH.get())
                .add(NaturalistRegistry.COOKED_CATFISH.get())
                .add(NaturalistRegistry.COOKED_PIRANHA.get());

        tag(C_FOODS_COOKED_MEAT)
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_CLAM_MEAT.get())
                .add(NaturalistRegistry.COOKED_CRAB_MEAT.get())
                .add(NaturalistRegistry.COOKED_DRUMSTICK.get())
                .add(NaturalistRegistry.COOKED_DUCK.get())
                .add(NaturalistRegistry.COOKED_LIZARD_TAIL.get())
                .add(NaturalistRegistry.COOKED_MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.COOKED_VENISON.get());

        tag(itemTag("c:foods/food_poisoning"))
                .add(NaturalistRegistry.LIZARD_TAIL.get());

        tag(C_FOODS_RAW_FISH)
                .add(NaturalistRegistry.ANGLERFISH.get())
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.BLOBFISH.get())
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.PIRANHA.get());

        tag(C_FOODS_RAW_MEAT)
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.CLAM_MEAT.get())
                .add(NaturalistRegistry.CRAB_MEAT.get())
                .add(NaturalistRegistry.DRUMSTICK.get())
                .add(NaturalistRegistry.DUCK.get())
                .add(NaturalistRegistry.LIZARD_TAIL.get())
                .add(NaturalistRegistry.MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.MORSEL.get())
                .add(NaturalistRegistry.VENISON.get());

        tag(itemTag("c:glass_blocks"))
                .add(NaturalistRegistry.AZURE_FROGLASS.get().asItem())
                .add(NaturalistRegistry.CRIMSON_FROGLASS.get().asItem())
                .add(NaturalistRegistry.VERDANT_FROGLASS.get().asItem());

        tag(itemTag("c:glass_panes"))
                .add(NaturalistRegistry.AZURE_FROGLASS_PANE.get().asItem())
                .add(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get().asItem())
                .add(NaturalistRegistry.VERDANT_FROGLASS_PANE.get().asItem());

        tag(itemTag("c:music_discs"))
                .add(NaturalistRegistry.MUSIC_DISC_DEATH_BY_HOGS.get())
                .add(NaturalistRegistry.MUSIC_DISC_WILD_ONES.get());

        tag(itemTag("c:raw_fish"))
                .addTag(C_FOODS_RAW_FISH);

        tag(itemTag("c:raw_meat"))
                .addTag(C_FOODS_RAW_MEAT);

        tag(itemTag("c:tools"))
                .add(NaturalistRegistry.CAPTURE_NET.get());

        tag(itemTag("forge:cooked_eggs"))
                .add(NaturalistRegistry.COOKED_EGG.get());

        tag(itemTag("forge:cooked_fishes"))
                .addTag(C_FOODS_COOKED_FISH);

        tag(Tags.Items.EGGS)
                .add(NaturalistRegistry.TORTOISE_EGG.get().asItem())
                .add(NaturalistRegistry.ALLIGATOR_EGG.get().asItem())
                .add(NaturalistRegistry.OSTRICH_EGG.get().asItem())
                .add(NaturalistRegistry.DUCK_EGG.get());

        tag(Tags.Items.GLASS)
                .add(NaturalistRegistry.AZURE_FROGLASS.get().asItem())
                .add(NaturalistRegistry.CRIMSON_FROGLASS.get().asItem())
                .add(NaturalistRegistry.VERDANT_FROGLASS.get().asItem());

        tag(Tags.Items.GLASS_PANES)
                .add(NaturalistRegistry.AZURE_FROGLASS_PANE.get().asItem())
                .add(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get().asItem())
                .add(NaturalistRegistry.VERDANT_FROGLASS_PANE.get().asItem());

        tag(itemTag("forge:raw_fishes"))
                .addTag(C_FOODS_RAW_FISH);

        tag(itemTag("minecraft:axolotl_food"))
                .add(NaturalistRegistry.BASS_BUCKET.get())
                .add(NaturalistRegistry.CATFISH_BUCKET.get())
                .add(NaturalistRegistry.ANGLERFISH_BUCKET.get())
                .add(NaturalistRegistry.BLOBFISH_BUCKET.get())
                .add(NaturalistRegistry.PIRANHA_BUCKET.get());

        tag(itemTag("minecraft:cat_food"))
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.ANGLERFISH.get())
                .add(NaturalistRegistry.BLOBFISH.get())
                .add(NaturalistRegistry.PIRANHA.get());

        tag(ItemTags.FISHES)
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.COOKED_BASS.get())
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.COOKED_CATFISH.get())
                .add(NaturalistRegistry.ANGLERFISH.get())
                .add(NaturalistRegistry.COOKED_ANGLERFISH.get())
                .add(NaturalistRegistry.BLOBFISH.get())
                .add(NaturalistRegistry.COOKED_BLOBFISH.get())
                .add(NaturalistRegistry.PIRANHA.get())
                .add(NaturalistRegistry.COOKED_PIRANHA.get());

        tag(itemTag("minecraft:meat"))
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get())
                .add(NaturalistRegistry.VENISON.get())
                .add(NaturalistRegistry.COOKED_VENISON.get())
                .add(NaturalistRegistry.DRUMSTICK.get())
                .add(NaturalistRegistry.COOKED_DRUMSTICK.get())
                .add(NaturalistRegistry.DUCK.get())
                .add(NaturalistRegistry.COOKED_DUCK.get())
                .add(NaturalistRegistry.MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.COOKED_MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.LIZARD_TAIL.get())
                .add(NaturalistRegistry.COOKED_LIZARD_TAIL.get())
                .add(NaturalistRegistry.CRAB_MEAT.get())
                .add(NaturalistRegistry.COOKED_CRAB_MEAT.get())
                .add(NaturalistRegistry.CLAM_MEAT.get())
                .add(NaturalistRegistry.COOKED_CLAM_MEAT.get())
                .add(NaturalistRegistry.MORSEL.get());

        tag(ItemTags.MUSIC_DISCS)
                .add(NaturalistRegistry.MUSIC_DISC_WILD_ONES.get())
                .add(NaturalistRegistry.MUSIC_DISC_DEATH_BY_HOGS.get());

        tag(itemTag("minecraft:ocelot_food"))
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.ANGLERFISH.get())
                .add(NaturalistRegistry.BLOBFISH.get())
                .add(NaturalistRegistry.PIRANHA.get());

        tag(ItemTags.PIGLIN_FOOD)
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get());

        tag(ItemTags.SLABS)
                .add(NaturalistRegistry.SHELLSTONE_SLAB.get().asItem())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get().asItem())
                .add(NaturalistRegistry.CUT_SHELLSTONE_SLAB.get().asItem())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get().asItem());

        tag(ItemTags.STAIRS)
                .add(NaturalistRegistry.SHELLSTONE_STAIRS.get().asItem())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get().asItem())
                .add(NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get().asItem())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get().asItem());

        tag(ItemTags.WALLS)
                .add(NaturalistRegistry.SHELLSTONE_WALL.get().asItem())
                .add(NaturalistRegistry.SHELLSTONE_BRICK_WALL.get().asItem())
                .add(NaturalistRegistry.CUT_SHELLSTONE_WALL.get().asItem())
                .add(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get().asItem());

        tag(NaturalistItemTags.ALLIGATOR_FOOD)
                .add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT);

        tag(NaturalistItemTags.BEAR_FURS)
                .add(NaturalistRegistry.FUR.get())
                .addOptional(new ResourceLocation("alexsmobs", "bear_fur"));

        tag(NaturalistItemTags.BEAR_TEMPT_ITEMS)
                .add(Items.SALMON, Items.COOKED_SALMON, Items.HONEYCOMB, Items.SWEET_BERRIES)
                .add(NaturalistRegistry.VENISON.get())
                .addOptional(new ResourceLocation("alexsmobs", "raw_moose_ribs"))
                .addOptional(new ResourceLocation("alexsmobs", "cooked_moose_ribs"));

        tag(NaturalistItemTags.BIRD_FOOD)
                .add(Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS);

        tag(NaturalistItemTags.BOAR_FOOD)
                .add(Items.CARROT, Items.POTATO, Items.BEETROOT);

        tag(NaturalistItemTags.CAPYBARA_FOOD)
                .add(Items.MELON_SLICE);

        tag(NaturalistItemTags.CRAB_FOOD)
                .add(Items.TROPICAL_FISH);

        tag(NaturalistItemTags.DUCK_FOOD)
                .addTag(C_FOODS_RAW_FISH);

        tag(NaturalistItemTags.EGGS)
                .add(NaturalistRegistry.TORTOISE_EGG.get().asItem())
                .add(NaturalistRegistry.ALLIGATOR_EGG.get().asItem())
                .add(NaturalistRegistry.DUCK_EGG.get())
                .add(Items.EGG);

        tag(NaturalistItemTags.GIANT_ISOPOD_FOOD)
                .add(Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.TROPICAL_FISH)
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.COOKED_CATFISH.get())
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.COOKED_BASS.get());

        tag(NaturalistItemTags.GIRAFFE_FOOD)
                .add(Items.APPLE, Items.GOLDEN_APPLE, Items.HAY_BLOCK);

        tag(NaturalistItemTags.GREAT_WHITE_SHARK_FOOD)
                .add(Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.TROPICAL_FISH)
                .add(NaturalistRegistry.CATFISH.get())
                .add(NaturalistRegistry.COOKED_CATFISH.get())
                .add(NaturalistRegistry.BASS.get())
                .add(NaturalistRegistry.COOKED_BASS.get());

        tag(NaturalistItemTags.HEDGEHOG_FOOD)
                .add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);

        tag(NaturalistItemTags.KOMODO_DRAGON_FOOD)
                .addTag(C_FOODS_RAW_MEAT)
                .addTag(C_FOODS_COOKED_MEAT)
                .add(NaturalistRegistry.VENISON.get())
                .add(NaturalistRegistry.COOKED_VENISON.get())
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get())
                .add(NaturalistRegistry.DUCK.get())
                .add(NaturalistRegistry.COOKED_DUCK.get())
                .add(NaturalistRegistry.DRUMSTICK.get())
                .add(NaturalistRegistry.COOKED_DRUMSTICK.get())
                .add(NaturalistRegistry.MORSEL.get())
                .add(NaturalistRegistry.MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.COOKED_MAMMOTH_MEAT.get());

        tag(NaturalistItemTags.LION_FOOD)
                .addTag(C_FOODS_RAW_MEAT)
                .addTag(C_FOODS_COOKED_MEAT)
                .add(NaturalistRegistry.VENISON.get())
                .add(NaturalistRegistry.COOKED_VENISON.get())
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get())
                .add(NaturalistRegistry.DUCK.get())
                .add(NaturalistRegistry.COOKED_DUCK.get());

        tag(NaturalistItemTags.LIZARD_TEMPT_ITEMS)
                .add(Items.SPIDER_EYE);

        tag(NaturalistItemTags.OSTRICH_FOOD)
                .add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.PITCHER_POD, Items.TORCHFLOWER_SEEDS);

        tag(NaturalistItemTags.RAT_FOOD)
                .add(Items.BREAD);

        tag(NaturalistItemTags.SHEARS)
                .add(Items.SHEARS)
                .addOptionalTag(new ResourceLocation("c", "shears"))
                .addOptionalTag(Tags.Items.SHEARS);

        tag(NaturalistItemTags.SNAKE_TAME_ITEMS)
                .add(Items.SPIDER_EYE, Items.RABBIT, Items.COOKED_RABBIT);

        tag(NaturalistItemTags.SNAKE_TEMPT_ITEMS)
                .add(Items.CHICKEN, Items.RABBIT, Items.RABBIT_FOOT, Items.EGG);

        tag(NaturalistItemTags.TIGER_FOOD)
                .addTag(C_FOODS_RAW_MEAT)
                .addTag(C_FOODS_COOKED_MEAT)
                .add(NaturalistRegistry.VENISON.get())
                .add(NaturalistRegistry.COOKED_VENISON.get())
                .add(NaturalistRegistry.BUSHMEAT.get())
                .add(NaturalistRegistry.COOKED_BUSHMEAT.get())
                .add(NaturalistRegistry.DUCK.get())
                .add(NaturalistRegistry.COOKED_DUCK.get())
                .add(NaturalistRegistry.DRUMSTICK.get())
                .add(NaturalistRegistry.COOKED_DRUMSTICK.get())
                .add(NaturalistRegistry.MORSEL.get())
                .add(NaturalistRegistry.MAMMOTH_MEAT.get())
                .add(NaturalistRegistry.COOKED_MAMMOTH_MEAT.get());

        tag(NaturalistItemTags.TORTOISE_TEMPT_ITEMS)
                .add(Items.BAMBOO, Items.DANDELION, Items.BROWN_MUSHROOM, Items.CACTUS);

        tag(NaturalistItemTags.TURKEY_FOOD)
                .add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.PITCHER_POD, Items.TORCHFLOWER_SEEDS);
    }

    private static TagKey<Item> itemTag(String id) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(id));
    }
}
