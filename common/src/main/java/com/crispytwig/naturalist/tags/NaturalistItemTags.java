package com.crispytwig.naturalist.tags;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class NaturalistItemTags {
    public static final TagKey<Item> CRAB_FOOD = tag("crab_food");
    public static final TagKey<Item> RAT_FOOD = tag("rat_food");
    public static final TagKey<Item> TIGER_FOOD = tag("tiger_food_items");
    public static final TagKey<Item> KOMODO_DRAGON_FOOD = tag("komodo_dragon_food_items");
    public static final TagKey<Item> GREAT_WHITE_SHARK_FOOD = tag("great_white_shark_food_items");
    public static final TagKey<Item> GIANT_ISOPOD_FOOD = tag("giant_isopod_food");
    public static final TagKey<Item> MOOSE_FOOD = tag("moose_food");
    public static final TagKey<Item> PENGUIN_FOOD = tag("penguin_food");

    public static final TagKey<Item> HYENA_FOOD = tag("hyena_food_items");
    public static final TagKey<Item> OSTRICH_FOOD = tag("ostrich_food_items");

    public static final TagKey<Item> BEAR_TEMPT_ITEMS = tag("bear_tempt_items");
    public static final TagKey<Item> SNAKE_TEMPT_ITEMS = tag("snake_tempt_items");
    public static final TagKey<Item> SNAKE_TAME_ITEMS = tag("snake_tame_items");
    public static final TagKey<Item> BIRD_FOOD = tag("bird_food_items");
    public static final TagKey<Item> GIRAFFE_FOOD = tag("giraffe_food_items");
    public static final TagKey<Item> LION_FOOD = tag("lion_food_items");
    public static final TagKey<Item> BOAR_FOOD = tag("boar_food_items");
    public static final TagKey<Item> ALLIGATOR_FOOD = tag("alligator_food_items");
    public static final TagKey<Item> LIZARD_TEMPT_ITEMS = tag("lizard_tempt_items");
    public static final TagKey<Item> TORTOISE_TEMPT_ITEMS = tag("tortoise_tempt_items");
    public static final TagKey<Item> DUCK_FOOD = tag("duck_food_items");
    public static final TagKey<Item> TURKEY_FOOD = tag("turkey_food_items");
    public static final TagKey<Item> CAPYBARA_FOOD = tag("capybara_food_items");
    public static final TagKey<Item> HEDGEHOG_FOOD = tag("hedgehog_food_items");
    public static final TagKey<Item> SHEARS = tag("shears");
    public static final TagKey<Item> EGGS = tag("eggs");
    public static final TagKey<Item> BEAR_FURS = tag("bear_furs");

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, Naturalist.location(name));
    }
}
