package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobEffects;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class NaturalistLanguageProvider extends LanguageProvider {
    public NaturalistLanguageProvider(PackOutput output) {
        super(output, Naturalist.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.naturalist.tab", "Naturalist");

        add("_comment.advancements", " ADVANCEMENTS ");
        add("advancements.husbandry.feed_bear_honeycomb.description", "Feed a Honeycomb to a Bear");
        add("advancements.husbandry.feed_bear_honeycomb.title", "Oh Bother");
        add("advancements.husbandry.feed_hippo_melon.description", "Feed a Melon to a Hippo");
        add("advancements.husbandry.feed_hippo_melon.title", "Hungry Hungry Hippos");
        add("advancements.husbandry.ride_giraffe_with_map.description", "Use a Map while riding a Giraffe");
        add("advancements.husbandry.ride_giraffe_with_map.title", "A Whole New World");

        add("_comment.blocks", " BLOCKS ");
        addBlock(NaturalistRegistry.ALLIGATOR_EGG, "Alligator Egg");
        addBlock(NaturalistRegistry.ANT_HILL, "Ant Hill");
        addBlock(NaturalistRegistry.AZURE_FROGLASS, "Azure Froglass");
        addBlock(NaturalistRegistry.AZURE_FROGLASS_PANE, "Azure Froglass Pane");
        addBlock(NaturalistRegistry.BLUE_STARFISH, "Blue Starfish");
        add("block.naturalist.cattail", "Cattail");
        addBlock(NaturalistRegistry.CHRYSALIS_BLOCK, "Chrysalis");
        addBlock(NaturalistRegistry.CRIMSON_FROGLASS, "Crimson Froglass");
        addBlock(NaturalistRegistry.CRIMSON_FROGLASS_PANE, "Crimson Froglass Pane");
        addBlock(NaturalistRegistry.CUT_SHELLSTONE, "Cut Shellstone");
        addBlock(NaturalistRegistry.CUT_SHELLSTONE_SLAB, "Cut Shellstone Slab");
        addBlock(NaturalistRegistry.CUT_SHELLSTONE_STAIRS, "Cut Shellstone Stairs");
        addBlock(NaturalistRegistry.CUT_SHELLSTONE_WALL, "Cut Shellstone Wall");
        add("block.naturalist.duckweed", "Duckweed");
        addBlock(NaturalistRegistry.ORANGE_STARFISH, "Orange Starfish");
        addBlock(NaturalistRegistry.OSTRICH_EGG, "Ostrich Egg");
        addBlock(NaturalistRegistry.PLUSH_BEAR, "Bear Plush");
        addBlock(NaturalistRegistry.PURPLE_STARFISH, "Purple Starfish");
        addBlock(NaturalistRegistry.RED_STARFISH, "Red Starfish");
        addBlock(NaturalistRegistry.SHELLSTONE, "Shellstone");
        addBlock(NaturalistRegistry.SHELLSTONE_BRICK_SLAB, "Shellstone Brick Slab");
        addBlock(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS, "Shellstone Brick Stairs");
        addBlock(NaturalistRegistry.SHELLSTONE_BRICK_WALL, "Shellstone Brick Wall");
        addBlock(NaturalistRegistry.SHELLSTONE_BRICKS, "Shellstone Bricks");
        addBlock(NaturalistRegistry.SHELLSTONE_SLAB, "Shellstone Slab");
        addBlock(NaturalistRegistry.SHELLSTONE_STAIRS, "Shellstone Stairs");
        addBlock(NaturalistRegistry.SHELLSTONE_WALL, "Shellstone Wall");
        addBlock(NaturalistRegistry.SMOOTH_SHELLSTONE, "Smooth Shellstone");
        addBlock(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB, "Smooth Shellstone Slab");
        addBlock(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS, "Smooth Shellstone Stairs");
        addBlock(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL, "Smooth Shellstone Wall");
        addBlock(NaturalistRegistry.SNAIL_EGGS, "Snail Eggs");
        addBlock(NaturalistRegistry.SNAIL_SHELL_BLOCK, "Snail Shell");
        addBlock(NaturalistRegistry.TORTOISE_EGG, "Tortoise Egg");
        addBlock(NaturalistRegistry.VERDANT_FROGLASS, "Verdant Froglass");
        addBlock(NaturalistRegistry.VERDANT_FROGLASS_PANE, "Verdant Froglass Pane");

        add("_comment.entities", " ENTITY NAMES ");
        add("death.attack.naturalist.hedgehog_throw", "%1$s died being thrown at %2$s");
        addEntityType(NaturalistEntityTypes.ALLIGATOR, "Alligator");
        addEntityType(NaturalistEntityTypes.ANGLERFISH, "Anglerfish");
        addEntityType(NaturalistEntityTypes.ANT, "Ant");
        addEntityType(NaturalistEntityTypes.BASS, "Bass");
        addEntityType(NaturalistEntityTypes.BEAR, "Bear");
        addEntityType(NaturalistEntityTypes.BIRD, "Bird");
        addEntityType(NaturalistEntityTypes.BLACK_BEAR, "Black Bear");
        addEntityType(NaturalistEntityTypes.BLOBFISH, "Blobfish");
        addEntityType(NaturalistEntityTypes.BOAR, "Boar");
        addEntityType(NaturalistEntityTypes.BUTTERFLY, "Butterfly");
        addEntityType(NaturalistEntityTypes.CARRIED_FOOD, "Carried Food");
        addEntityType(NaturalistEntityTypes.CAPYBARA, "Capybara");
        addEntityType(NaturalistEntityTypes.CATERPILLAR, "Caterpillar");
        addEntityType(NaturalistEntityTypes.CATFISH, "Catfish");
        addEntityType(NaturalistEntityTypes.CLAM, "Clam");
        addEntityType(NaturalistEntityTypes.CRAB, "Crab");
        addEntityType(NaturalistEntityTypes.DEER, "Deer");
        addEntityType(NaturalistEntityTypes.DESERT_SCORPION, "Desert Scorpion");
        addEntityType(NaturalistEntityTypes.DIRT_TRAIL, "Dirt Trail");
        addEntityType(NaturalistEntityTypes.DRAGONFLY, "Dragonfly");
        addEntityType(NaturalistEntityTypes.DUCK, "Duck");
        addEntityType(NaturalistEntityTypes.ELEPHANT, "Elephant");
        addEntityType(NaturalistEntityTypes.FIREFLY, "Firefly");
        addEntityType(NaturalistEntityTypes.GIANT_ISOPOD, "Giant Isopod");
        addEntityType(NaturalistEntityTypes.GIRAFFE, "Giraffe");
        addEntityType(NaturalistEntityTypes.GREAT_WHITE_SHARK, "Great White Shark");
        addEntityType(NaturalistEntityTypes.HEDGEHOG, "Hedgehog");
        addEntityType(NaturalistEntityTypes.HIPPO, "Hippo");
        addEntityType(NaturalistEntityTypes.JELLYFISH, "Jellyfish");
        addEntityType(NaturalistEntityTypes.JUNGLE_SCORPION, "Jungle Scorpion");
        addEntityType(NaturalistEntityTypes.KOMODO_DRAGON, "Komodo Dragon");
        addEntityType(NaturalistEntityTypes.LION, "Lion");
        addEntityType(NaturalistEntityTypes.LIZARD, "Lizard");
        addEntityType(NaturalistEntityTypes.LIZARD_TAIL, "Lizard Tail");
        addEntityType(NaturalistEntityTypes.MAMMOTH, "Mammoth");
        addEntityType(NaturalistEntityTypes.MOLE, "Mole");
        addEntityType(NaturalistEntityTypes.OSTRICH, "Ostrich");
        addEntityType(NaturalistEntityTypes.PIRANHA, "Piranha");
        addEntityType(NaturalistEntityTypes.RAT, "Rat");
        addEntityType(NaturalistEntityTypes.RAY, "Ray");
        addEntityType(NaturalistEntityTypes.RHINO, "Rhino");
        addEntityType(NaturalistEntityTypes.SNAIL, "Snail");
        addEntityType(NaturalistEntityTypes.SNAKE, "Snake");
        addEntityType(NaturalistEntityTypes.STARFISH, "Starfish");
        addEntityType(NaturalistEntityTypes.TIGER, "Tiger");
        addEntityType(NaturalistEntityTypes.TORTOISE, "Tortoise");
        addEntityType(NaturalistEntityTypes.TURKEY, "Turkey");
        addEntityType(NaturalistEntityTypes.VULTURE, "Vulture");
        addEntityType(NaturalistEntityTypes.WHALE, "Whale");
        addEntityType(NaturalistEntityTypes.ZEBRA, "Zebra");

        add("_comment.effects", " POTIONS / EFFECTS ");
        addEffect(NaturalistMobEffects.ANTIVENOM, "Antivenom");
        add("item.minecraft.lingering_potion.effect.antivenom", "Lingering Potion of Antivenom");
        add("item.minecraft.lingering_potion.effect.forest_dasher", "Lingering Potion of the Forest Dasher");
        add("item.minecraft.lingering_potion.effect.glowing", "Lingering Potion of Glowing");
        add("item.minecraft.potion.effect.antivenom", "Potion of Antivenom");
        add("item.minecraft.potion.effect.forest_dasher", "Potion of the Forest Dasher");
        add("item.minecraft.potion.effect.glowing", "Potion of Glowing");
        add("item.minecraft.splash_potion.effect.antivenom", "Splash Potion of Antivenom");
        add("item.minecraft.splash_potion.effect.forest_dasher", "Splash Potion of the Forest Dasher");
        add("item.minecraft.splash_potion.effect.glowing", "Splash Potion of Glowing");
        add("item.minecraft.tipped_arrow.effect.antivenom", "Arrow of Antivenom");
        add("item.minecraft.tipped_arrow.effect.forest_dasher", "Arrow of the Forest Dasher");
        add("item.minecraft.tipped_arrow.effect.glowing", "Arrow of Glowing");

        add("_comment.items", " ITEMS ");
        addItem(NaturalistRegistry.ALLIGATOR_SPAWN_EGG, "Alligator Spawn Egg");
        addItem(NaturalistRegistry.ANGLERFISH, "Anglerfish");
        addItem(NaturalistRegistry.ANGLERFISH_BUCKET, "Bucket of Anglerfish");
        addItem(NaturalistRegistry.ANGLERFISH_SPAWN_EGG, "Anglerfish Spawn Egg");
        addItem(NaturalistRegistry.ANT, "Ant");
        addItem(NaturalistRegistry.ANT_SPAWN_EGG, "Ant Spawn Egg");
        addItem(NaturalistRegistry.ANTLER, "Antler");
        addItem(NaturalistRegistry.BASS, "Raw Bass");
        addItem(NaturalistRegistry.BASS_BUCKET, "Bucket of Bass");
        addItem(NaturalistRegistry.BASS_SPAWN_EGG, "Bass Spawn Egg");
        addItem(NaturalistRegistry.BEAR_SPAWN_EGG, "Brown Bear Spawn Egg");
        addItem(NaturalistRegistry.BIRD_SPAWN_EGG, "Bird Spawn Egg");
        addItem(NaturalistRegistry.BLACK_BEAR_SPAWN_EGG, "Black Bear Spawn Egg");
        addItem(NaturalistRegistry.BLOBFISH, "Blobfish");
        addItem(NaturalistRegistry.BLOBFISH_BUCKET, "Bucket of Blobfish");
        addItem(NaturalistRegistry.BLOBFISH_SPAWN_EGG, "Blobfish Spawn Egg");
        addItem(NaturalistRegistry.BOAR_SPAWN_EGG, "Boar Spawn Egg");
        addItem(NaturalistRegistry.BUSHMEAT, "Bushmeat");
        addItem(NaturalistRegistry.BUTTERFLY, "Butterfly");
        addItem(NaturalistRegistry.BUTTERFLY_SPAWN_EGG, "Butterfly Spawn Egg");
        addItem(NaturalistRegistry.CAPYBARA_SPAWN_EGG, "Capybara Spawn Egg");
        addItem(NaturalistRegistry.CAPTURE_NET, "Capture Net");
        addItem(NaturalistRegistry.CATERPILLAR, "Caterpillar");
        addItem(NaturalistRegistry.DRAGONFLY, "Dragonfly");
        addItem(NaturalistRegistry.FIREFLY, "Firefly");
        addItem(NaturalistRegistry.BEE, "Bee");
        addItem(NaturalistRegistry.SILVERFISH, "Silverfish");
        addItem(NaturalistRegistry.ENDERMITE, "Endermite");
        addItem(NaturalistRegistry.CATERPILLAR_SPAWN_EGG, "Caterpillar Spawn Egg");
        addItem(NaturalistRegistry.CATFISH, "Raw Catfish");
        addItem(NaturalistRegistry.CATFISH_BUCKET, "Bucket of Catfish");
        addItem(NaturalistRegistry.CATFISH_SPAWN_EGG, "Catfish Spawn Egg");
        add("item.naturalist.cattail_fluff", "Cattail Fluff");
        addItem(NaturalistRegistry.CLAM_MEAT, "Clam Meat");
        addItem(NaturalistRegistry.CLAM_SPAWN_EGG, "Clam Spawn Egg");
        addItem(NaturalistRegistry.COOKED_ANGLERFISH, "Cooked Anglerfish");
        addItem(NaturalistRegistry.COOKED_BASS, "Cooked Bass");
        addItem(NaturalistRegistry.COOKED_BLOBFISH, "Cooked Blobfish");
        addItem(NaturalistRegistry.COOKED_BUSHMEAT, "Cooked Bushmeat");
        addItem(NaturalistRegistry.COOKED_CATFISH, "Cooked Catfish");
        addItem(NaturalistRegistry.COOKED_CLAM_MEAT, "Cooked Clam Meat");
        addItem(NaturalistRegistry.COOKED_CRAB_MEAT, "Cooked Crab Meat");
        addItem(NaturalistRegistry.COOKED_DRUMSTICK, "Cooked Drumstick");
        addItem(NaturalistRegistry.COOKED_DUCK, "Cooked Duck");
        addItem(NaturalistRegistry.COOKED_EGG, "Cooked Egg");
        add("item.naturalist.cooked_fish_fillet", "Cooked Fish Fillet");
        addItem(NaturalistRegistry.COOKED_LIZARD_TAIL, "Cooked Lizard Tail");
        addItem(NaturalistRegistry.COOKED_MAMMOTH_MEAT, "Cooked Mammoth Meat");
        addItem(NaturalistRegistry.COOKED_PIRANHA, "Cooked Piranha");
        addItem(NaturalistRegistry.COOKED_VENISON, "Cooked Venison");
        addItem(NaturalistRegistry.CRAB, "Crab");
        addItem(NaturalistRegistry.CRAB_MEAT, "Crab Meat");
        addItem(NaturalistRegistry.CRAB_SPAWN_EGG, "Crab Spawn Egg");
        addItem(NaturalistRegistry.DEER_SPAWN_EGG, "Deer Spawn Egg");
        addItem(NaturalistRegistry.DESERT_SCORPION_SPAWN_EGG, "Desert Scorpion Spawn Egg");
        addItem(NaturalistRegistry.DRAGONFLY_SPAWN_EGG, "Dragonfly Spawn Egg");
        addItem(NaturalistRegistry.DRUMSTICK, "Drumstick");
        addItem(NaturalistRegistry.DUCK, "Raw Duck");
        addItem(NaturalistRegistry.DUCK_BUCKET, "Bucket of Duckling");
        addItem(NaturalistRegistry.DUCK_EGG, "Duck Egg");
        addItem(NaturalistRegistry.DUCK_SPAWN_EGG, "Duck Spawn Egg");
        addItem(NaturalistRegistry.ELEPHANT_SPAWN_EGG, "Elephant Spawn Egg");
        addItem(NaturalistRegistry.FAT, "Fat");
        addItem(NaturalistRegistry.FIREFLY_SPAWN_EGG, "Firefly Spawn Egg");
        addItem(NaturalistRegistry.FUR, "Fur");
        addItem(NaturalistRegistry.GIANT_ISOPOD_BUCKET, "Bucket of Giant Isopod");
        addItem(NaturalistRegistry.GIANT_ISOPOD_SPAWN_EGG, "Giant Isopod Spawn Egg");
        addItem(NaturalistRegistry.GIRAFFE_SPAWN_EGG, "Giraffe Spawn Egg");
        addItem(NaturalistRegistry.GLOW_GOOP, "Glow Goop");
        addItem(NaturalistRegistry.GREAT_WHITE_SHARK_SPAWN_EGG, "Great White Shark Spawn Egg");
        addItem(NaturalistRegistry.HIDE, "Hide");
        addItem(NaturalistRegistry.HEDGEHOG, "Hedgehog");
        addItem(NaturalistRegistry.HEDGEHOG_SPAWN_EGG, "Hedgehog Spawn Egg");
        addItem(NaturalistRegistry.HIPPO_SPAWN_EGG, "Hippo Spawn Egg");
        addItem(NaturalistRegistry.JELLYFISH_BUCKET, "Bucket of Jellyfish");
        addItem(NaturalistRegistry.JELLYFISH_SPAWN_EGG, "Jellyfish Spawn Egg");
        addItem(NaturalistRegistry.JUNGLE_SCORPION_SPAWN_EGG, "Jungle Scorpion Spawn Egg");
        addItem(NaturalistRegistry.KNAPSACK, "Knapsack");
        addItem(NaturalistRegistry.KOMODO_DRAGON_SPAWN_EGG, "Komodo Dragon Spawn Egg");
        addItem(NaturalistRegistry.LION_SPAWN_EGG, "Lion Spawn Egg");
        addItem(NaturalistRegistry.LIZARD_SPAWN_EGG, "Lizard Spawn Egg");
        addItem(NaturalistRegistry.LIZARD_TAIL, "Lizard Tail");
        addItem(NaturalistRegistry.MAMMOTH_MEAT, "Raw Mammoth Meat");
        addItem(NaturalistRegistry.MAMMOTH_SPAWN_EGG, "Mammoth Spawn Egg");
        addItem(NaturalistRegistry.MOLE_SPAWN_EGG, "Mole Spawn Egg");
        addItem(NaturalistRegistry.MORSEL, "Morsel");
        addItem(NaturalistRegistry.MUSIC_DISC_DEATH_BY_HOGS, "Music Disc");
        addItem(NaturalistRegistry.MUSIC_DISC_WILD_ONES, "Music Disc");
        add("item.naturalist.music_disc_death_by_hogs.desc", "Ninni - Death by Hogs");
        add("jukebox_song.naturalist.death_by_hogs", "Ninni - Death by Hogs");
        add("item.naturalist.music_disc_wild_ones.desc", "Chips the Cat - Wild Ones");
        add("jukebox_song.naturalist.wild_ones", "Chips the Cat - Wild Ones");
        add("painting.naturalist.bear_necessities.author", ".inzaenity");
        add("painting.naturalist.bear_necessities.title", "Bear Necessities");
        addItem(NaturalistRegistry.OSTRICH_SPAWN_EGG, "Ostrich Spawn Egg");
        addItem(NaturalistRegistry.PIRANHA, "Piranha");
        addItem(NaturalistRegistry.PIRANHA_BUCKET, "Bucket of Piranha");
        addItem(NaturalistRegistry.PIRANHA_SPAWN_EGG, "Piranha Spawn Egg");
        addItem(NaturalistRegistry.QUEEN_ANT, "Queen Ant");
        addItem(NaturalistRegistry.RAT, "Rat");
        addItem(NaturalistRegistry.RAT_SPAWN_EGG, "Rat Spawn Egg");
        addItem(NaturalistRegistry.RAY_BUCKET, "Bucket of Ray");
        addItem(NaturalistRegistry.RAY_SPAWN_EGG, "Ray Spawn Egg");
        addItem(NaturalistRegistry.RHINO_SPAWN_EGG, "Rhino Spawn Egg");
        addItem(NaturalistRegistry.SCORPION, "Scorpion");
        addItem(NaturalistRegistry.SCORPION_POISON_GLAND, "Scorpion Poison Gland");
        addItem(NaturalistRegistry.SNAIL, "Snail");
        add("item.naturalist.snail_shell", "Snail Shell");
        addItem(NaturalistRegistry.SNAIL_SPAWN_EGG, "Snail Spawn Egg");
        addItem(NaturalistRegistry.SNAKE_SPAWN_EGG, "Snake Spawn Egg");
        addItem(NaturalistRegistry.STARFISH_BUCKET, "Bucket of Starfish");
        addItem(NaturalistRegistry.STARFISH_SPAWN_EGG, "Starfish Spawn Egg");
        addItem(NaturalistRegistry.TIGER_SPAWN_EGG, "Tiger Spawn Egg");
        addItem(NaturalistRegistry.TOOTH, "Tooth");
        addItem(NaturalistRegistry.TORTOISE_SPAWN_EGG, "Tortoise Spawn Egg");
        addItem(NaturalistRegistry.TURKEY_SPAWN_EGG, "Turkey Spawn Egg");
        addItem(NaturalistRegistry.VENISON, "Venison");
        addItem(NaturalistRegistry.VULTURE_SPAWN_EGG, "Vulture Spawn Egg");
        addItem(NaturalistRegistry.WHALE_SPAWN_EGG, "Whale Spawn Egg");
        addItem(NaturalistRegistry.WHISTLE, "Whistle");
        addItem(NaturalistRegistry.ZEBRA_SPAWN_EGG, "Zebra Spawn Egg");

        add("_comment.tooltips", " TOOLTIPS ");
        add("tooltip.naturalist.anglerfish_glow", "Glow");
        add("tooltip.naturalist.anglerfish_red", "Red");
        add("tooltip.naturalist.bass_small", "Small");
        add("tooltip.naturalist.bass_medium", "Medium");
        add("tooltip.naturalist.bass_large", "Large");
        add("tooltip.naturalist.blue_morpho", "Blue Morpho");
        add("tooltip.naturalist.clouded_yellow", "Clouded Yellow");
        add("tooltip.naturalist.crab_blue", "Blue");
        add("tooltip.naturalist.crab_brown", "Brown");
        add("tooltip.naturalist.crab_orange", "Orange");
        add("tooltip.naturalist.crab_red", "Red");
        add("tooltip.naturalist.crab_yellow", "Yellow");
        add("tooltip.naturalist.giant_isopod_blue", "Blue");
        add("tooltip.naturalist.giant_isopod_brown", "Brown");
        add("tooltip.naturalist.jade_green_swallowtail", "Jade Green Swallowtail");
        add("tooltip.naturalist.monarch", "Monarch");
        add("tooltip.naturalist.purple_emperor", "Purple Emperor");
        add("tooltip.naturalist.hedgehog_brown", "Brown");
        add("tooltip.naturalist.hedgehog_dark", "Dark");
        add("tooltip.naturalist.hedgehog_white", "White");
        add("tooltip.naturalist.rat_black", "Black");
        add("tooltip.naturalist.rat_brown", "Brown");
        add("tooltip.naturalist.rat_white", "White");
        add("tooltip.naturalist.ray_eagle_ray", "Eagle Ray");
        add("tooltip.naturalist.ray_mobula_ray", "Mobula Ray");
        add("tooltip.naturalist.ray_stingray", "Stingray");
        add("tooltip.naturalist.red_admiral", "Red Admiral");
        add("tooltip.naturalist.swallowtail", "Swallowtail");

        add("_comment.configuration", " CONFIGS ");
        add("naturalist.configuration.alligator_removed", "Remove Alligator");
        add("naturalist.configuration.anglerfish_removed", "Remove Anglerfish");
        add("naturalist.configuration.aquatic_mobs", "Aquatic Mobs");
        add("naturalist.configuration.ant_removed", "Remove Ant");
        add("naturalist.configuration.bass_removed", "Remove Bass");
        add("naturalist.configuration.bear_removed", "Remove Bear");
        add("naturalist.configuration.behavior", "Behavior");
        add("naturalist.configuration.bird_head_slow_falling", "Bird Slow Falling");
        add("naturalist.configuration.bird_removed", "Remove Birds");
        add("naturalist.configuration.black_bear_removed", "Remove Black Bear");
        add("naturalist.configuration.blobfish_removed", "Remove Blobfish");
        add("naturalist.configuration.boar_removed", "Remove Boar");
        add("naturalist.configuration.bugs", "Bugs");
        add("naturalist.configuration.butterfly_removed", "Remove Butterfly");
        add("naturalist.configuration.capybara_removed", "Remove Capybara");
        add("naturalist.configuration.catfish_removed", "Remove Catfish");
        add("naturalist.configuration.clam_removed", "Remove Clam");
        add("naturalist.configuration.crab_removed", "Remove Crab");
        add("naturalist.configuration.deer_removed", "Remove Deer");
        add("naturalist.configuration.desert_scorpion_removed", "Remove Desert Scorpion");
        add("naturalist.configuration.disable_mobs", "Disable Mobs");
        add("naturalist.configuration.dragonfly_removed", "Remove Dragonfly");
        add("naturalist.configuration.duck_removed", "Remove Duck");
        add("naturalist.configuration.elephant_removed", "Remove Elephant");
        add("naturalist.configuration.firefly_removed", "Remove Firefly");
        add("naturalist.configuration.forest_fox_removed", "Remove Forest Fox");
        add("naturalist.configuration.forest_rabbit_removed", "Remove Forest Rabbit");
        add("naturalist.configuration.giant_isopod_removed", "Remove Giant Isopod");
        add("naturalist.configuration.giraffe_removed", "Remove Giraffe");
        add("naturalist.configuration.glow_goop_tooltip", "Glow Goop Tooltip");
        add("naturalist.configuration.great_white_shark_removed", "Remove Great White Shark");
        add("naturalist.configuration.hedgehog_removed", "Remove Hedgehog");
        add("naturalist.configuration.hippo_removed", "Remove Hippo");
        add("naturalist.configuration.jellyfish_removed", "Remove Jellyfish");
        add("naturalist.configuration.jungle_scorpion_removed", "Remove Jungle Scorpion");
        add("naturalist.configuration.komodo_dragon_removed", "Remove Komodo Dragon");
        add("naturalist.configuration.land_mobs", "Land Mobs");
        add("naturalist.configuration.lion_removed", "Remove Lion");
        add("naturalist.configuration.lizard_removed", "Remove Lizard");
        add("naturalist.configuration.mammoth_removed", "Remove Mammoth");
        add("naturalist.configuration.mole_removed", "Remove Mole");
        add("naturalist.configuration.ostrich_removed", "Remove Ostrich");
        add("naturalist.configuration.parrot_flight", "Parrot Flight");
        add("naturalist.configuration.piranha_removed", "Remove Piranha");
        add("naturalist.configuration.rat_removed", "Remove Rat");
        add("naturalist.configuration.ray_removed", "Remove Ray");
        add("naturalist.configuration.remove_all_bugs", "Remove All Bugs");
        add("naturalist.configuration.rhino_removed", "Remove Rhino");
        add("naturalist.configuration.snail_crushing", "Crushable Snails");
        add("naturalist.configuration.snail_removed", "Remove Snail");
        add("naturalist.configuration.snake_removed", "Remove Snakes");
        add("naturalist.configuration.starfish_removed", "Remove Starfish");
        add("naturalist.configuration.tiger_removed", "Remove Tiger");
        add("naturalist.configuration.tortoise_removed", "Remove Tortoise");
        add("naturalist.configuration.turkey_removed", "Remove Turkey");
        add("naturalist.configuration.vulture_removed", "Remove Vulture");
        add("naturalist.configuration.whale_removed", "Remove Whale");
        add("naturalist.configuration.zebra_removed", "Remove Zebra");

        add("_comment.whistle_knapsack", " WHISTLE / KNAPSACK ");
        add("naturalist.whistle.message", "%1$s is now %2$s.");
        add("naturalist.whistle.following", "Following");
        add("naturalist.whistle.wandering", "Wandering");
        add("naturalist.subtitles.item.whistle", "Whistle blows");
        add("naturalist.subtitles.item.knapsack.pickup", "Knapsack captures");
        add("naturalist.subtitles.item.knapsack.place", "Knapsack releases");

        add("_comment.subtitles", " ENTITY SUBTITLES ");
        add("naturalist.subtitles.entity.alligator.ambient", "Alligator bellows");
        add("naturalist.subtitles.entity.alligator.death", "Alligator dies");
        add("naturalist.subtitles.entity.alligator.egg_break", "Alligator egg breaks");
        add("naturalist.subtitles.entity.alligator.egg_crack", "Alligator egg cracks");
        add("naturalist.subtitles.entity.alligator.egg_hatch", "Alligator egg hatches");
        add("naturalist.subtitles.entity.alligator.hurt", "Alligator hurts");

        add("naturalist.subtitles.entity.anglerfish.idle", "Anglerfish gurgles");
        add("naturalist.subtitles.entity.anglerfish.swim", "Anglerfish swims");
        add("naturalist.subtitles.entity.anglerfish.hurt", "Anglerfish hurts");
        add("naturalist.subtitles.entity.anglerfish.death", "Anglerfish dies");

        add("naturalist.subtitles.entity.ant.ambient", "Ant chitters");
        add("naturalist.subtitles.entity.ant.hurt", "Ant hurts");
        add("naturalist.subtitles.entity.ant.death", "Ant dies");

        add("naturalist.subtitles.entity.bass.flop", "Bass flops");

        add("naturalist.subtitles.entity.bear.ambient", "Brown Bear groans");
        add("naturalist.subtitles.entity.bear.death", "Brown Bear dies");
        add("naturalist.subtitles.entity.bear.eat", "Brown Bear eats");
        add("naturalist.subtitles.entity.bear.hurt", "Brown Bear hurts");
        add("naturalist.subtitles.entity.bear.sleep", "Brown Bear snores");
        add("naturalist.subtitles.entity.bear.sniff", "Brown Bear sniffs");
        add("naturalist.subtitles.entity.bear.spit", "Brown Bear spits");

        add("naturalist.subtitles.entity.bird.ambient_bluejay", "Bluejay chirps");
        add("naturalist.subtitles.entity.bird.ambient_canary", "Canary chirps");
        add("naturalist.subtitles.entity.bird.ambient_cardinal", "Cardinal chirps");
        add("naturalist.subtitles.entity.bird.ambient_finch", "Finch chirps");
        add("naturalist.subtitles.entity.bird.ambient_robin", "Robin chirps");
        add("naturalist.subtitles.entity.bird.ambient_sparrow", "Sparrow chirps");
        add("naturalist.subtitles.entity.bird.death", "Bird dies");
        add("naturalist.subtitles.entity.bird.eat", "Bird eats");
        add("naturalist.subtitles.entity.bird.fly", "Bird flutters");
        add("naturalist.subtitles.entity.bird.hurt", "Bird hurts");

        add("naturalist.subtitles.entity.black_bear.ambient", "Black Bear groans");
        add("naturalist.subtitles.entity.black_bear.hurt", "Black Bear hurts");
        add("naturalist.subtitles.entity.black_bear.death", "Black Bear dies");
        add("naturalist.subtitles.entity.black_bear.sleep", "Black Bear snores");
        add("naturalist.subtitles.entity.black_bear.eat", "Black Bear eats");
        add("naturalist.subtitles.entity.black_bear.sniff", "Black Bear sniffs");
        add("naturalist.subtitles.entity.black_bear.attack", "Black Bear attacks");

        add("naturalist.subtitles.entity.blobfish.flop", "Blobfish flops");
        add("naturalist.subtitles.entity.blobfish.swim", "Blobfish swims");
        add("naturalist.subtitles.entity.blobfish.hurt", "Blobfish hurts");
        add("naturalist.subtitles.entity.blobfish.death", "Blobfish dies");

        add("naturalist.subtitles.entity.boar.ambient", "Boar grunts");
        add("naturalist.subtitles.entity.boar.death", "Boar dies");
        add("naturalist.subtitles.entity.boar.hurt", "Boar hurts");

        add("naturalist.subtitles.entity.butterfly.ambient", "Butterfly flutters");
        add("naturalist.subtitles.entity.butterfly.hurt", "Butterfly hurts");

        add("naturalist.subtitles.entity.catfish.flop", "Catfish flops");

        add("naturalist.subtitles.entity.clam.ambient", "Clam burbles");
        add("naturalist.subtitles.entity.clam.hurt", "Clam hurts");
        add("naturalist.subtitles.entity.clam.death", "Clam dies");
        add("naturalist.subtitles.entity.clam.launch", "Clam snaps");

        add("naturalist.subtitles.entity.crab.ambient", "Crab clatters");
        add("naturalist.subtitles.entity.crab.hurt", "Crab hurts");
        add("naturalist.subtitles.entity.crab.death", "Crab dies");
        add("naturalist.subtitles.entity.crab.pincer", "Crab snips");

        add("naturalist.subtitles.entity.deer.ambient", "Deer grunts");
        add("naturalist.subtitles.entity.deer.death", "Deer dies");
        add("naturalist.subtitles.entity.deer.hurt", "Deer hurts");

        add("naturalist.subtitles.entity.dragonfly.death", "Dragonfly dies");
        add("naturalist.subtitles.entity.dragonfly.hurt", "Dragonfly hurts");
        add("naturalist.subtitles.entity.dragonfly.loop", "Dragonfly buzzes");

        add("naturalist.subtitles.entity.capybara.ambient", "Capybara squeaks");
        add("naturalist.subtitles.entity.capybara.death", "Capybara dies");
        add("naturalist.subtitles.entity.capybara.hurt", "Capybara hurts");
        add("naturalist.subtitles.entity.capybara.sleep", "Capybara snores");

        add("naturalist.subtitles.entity.hedgehog.ambient", "Hedgehog grunts");
        add("naturalist.subtitles.entity.hedgehog.death", "Hedgehog dies");
        add("naturalist.subtitles.entity.hedgehog.hide", "Hedgehog rolls up");
        add("naturalist.subtitles.entity.hedgehog.hurt", "Hedgehog hurts");
        add("naturalist.subtitles.entity.hedgehog.unhide", "Hedgehog unrolls");
        add("naturalist.subtitles.entity.duck.ambient", "Duck quacks");
        add("naturalist.subtitles.entity.duck.death", "Duck dies");
        add("naturalist.subtitles.entity.duck.hurt", "Duck hurts");

        add("naturalist.subtitles.entity.elephant.ambient", "Elephant trumpets");
        add("naturalist.subtitles.entity.elephant.death", "Elephant dies");
        add("naturalist.subtitles.entity.elephant.hurt", "Elephant hurts");

        add("naturalist.subtitles.entity.firefly.ambient", "Firefly buzzes");
        add("naturalist.subtitles.entity.firefly.death", "Firefly dies");
        add("naturalist.subtitles.entity.firefly.hide", "Firefly hides");
        add("naturalist.subtitles.entity.firefly.hurt", "Firefly hurts");

        add("naturalist.subtitles.entity.giant_isopod.roll", "Giant Isopod rolls up");

        add("naturalist.subtitles.entity.giraffe.ambient", "Giraffe snorts");
        add("naturalist.subtitles.entity.giraffe.death", "Giraffe dies");
        add("naturalist.subtitles.entity.giraffe.hurt", "Giraffe hurts");

        add("naturalist.subtitles.entity.great_white_shark.ambient", "Great White Shark growls");
        add("naturalist.subtitles.entity.great_white_shark.hurt", "Great White Shark hurts");
        add("naturalist.subtitles.entity.great_white_shark.death", "Great White Shark dies");
        add("naturalist.subtitles.entity.great_white_shark.attack", "Great White Shark bites");
        add("naturalist.subtitles.entity.great_white_shark.flop", "Great White Shark flops");
        add("naturalist.subtitles.entity.great_white_shark.swim", "Great White Shark swims");

        add("naturalist.subtitles.entity.hippo.ambient", "Hippo grumbles");
        add("naturalist.subtitles.entity.hippo.death", "Hippo dies");
        add("naturalist.subtitles.entity.hippo.hurt", "Hippo hurts");

        add("naturalist.subtitles.entity.jellyfish.idle", "Jellyfish pulses");
        add("naturalist.subtitles.entity.jellyfish.swim", "Jellyfish swims");
        add("naturalist.subtitles.entity.jellyfish.hurt", "Jellyfish hurts");
        add("naturalist.subtitles.entity.jellyfish.death", "Jellyfish dies");

        add("naturalist.subtitles.entity.komodo_dragon.ambient", "Komodo Dragon growls");
        add("naturalist.subtitles.entity.komodo_dragon.hurt", "Komodo Dragon hurts");
        add("naturalist.subtitles.entity.komodo_dragon.death", "Komodo Dragon dies");
        add("naturalist.subtitles.entity.komodo_dragon.attack", "Komodo Dragon bites");

        add("naturalist.subtitles.entity.lion.ambient", "Lion growls");
        add("naturalist.subtitles.entity.lion.death", "Lion dies");
        add("naturalist.subtitles.entity.lion.hurt", "Lion hurts");
        add("naturalist.subtitles.entity.lion.roar", "Lion roars");

        add("naturalist.subtitles.entity.lizard.ambient", "Lizard chirps");
        add("naturalist.subtitles.entity.lizard.death", "Lizard dies");
        add("naturalist.subtitles.entity.lizard.hurt", "Lizard hurts");

        add("naturalist.subtitles.entity.mammoth.ambient", "Mammoth trumpets");
        add("naturalist.subtitles.entity.mammoth.death", "Mammoth dies");
        add("naturalist.subtitles.entity.mammoth.hurt", "Mammoth hurts");

        add("naturalist.subtitles.entity.mole.ambient", "Mole squeaks");
        add("naturalist.subtitles.entity.mole.hurt", "Mole hurts");
        add("naturalist.subtitles.entity.mole.death", "Mole dies");
        add("naturalist.subtitles.entity.mole.dig_down", "Mole burrows");
        add("naturalist.subtitles.entity.mole.dig_up", "Mole surfaces");
        add("naturalist.subtitles.entity.mole.peek", "Mole peeks");

        add("naturalist.subtitles.entity.ostrich.ambient", "Ostrich chirps");
        add("naturalist.subtitles.entity.ostrich.hurt", "Ostrich hurts");
        add("naturalist.subtitles.entity.ostrich.death", "Ostrich dies");
        add("naturalist.subtitles.entity.ostrich.attack", "Ostrich kicks");
        add("naturalist.subtitles.entity.ostrich.bury", "Ostrich buries its head");
        add("naturalist.subtitles.entity.ostrich.egg_break", "Ostrich egg breaks");
        add("naturalist.subtitles.entity.ostrich.egg_crack", "Ostrich egg cracks");
        add("naturalist.subtitles.entity.ostrich.egg_hatch", "Ostrich egg hatches");

        add("naturalist.subtitles.entity.piranha.flop", "Piranha flops");
        add("naturalist.subtitles.entity.piranha.idle", "Piranha gurgles");
        add("naturalist.subtitles.entity.piranha.hurt", "Piranha hurts");
        add("naturalist.subtitles.entity.piranha.death", "Piranha dies");

        add("naturalist.subtitles.entity.rat.ambient", "Rat squeaks");
        add("naturalist.subtitles.entity.rat.hurt", "Rat hurts");
        add("naturalist.subtitles.entity.rat.death", "Rat dies");
        add("naturalist.subtitles.entity.rat.swim", "Rat paddles");

        add("naturalist.subtitles.entity.ray.idle", "Ray gurgles");
        add("naturalist.subtitles.entity.ray.swim", "Ray swims");
        add("naturalist.subtitles.entity.ray.hurt", "Ray hurts");
        add("naturalist.subtitles.entity.ray.death", "Ray dies");

        add("naturalist.subtitles.entity.rhino.ambient", "Rhino growls");
        add("naturalist.subtitles.entity.rhino.death", "Rhino dies");
        add("naturalist.subtitles.entity.rhino.hurt", "Rhino hurts");
        add("naturalist.subtitles.entity.rhino.scrape", "Rhino scrapes foot");

        add("naturalist.subtitles.entity.scorpion.ambient", "Scorpion chitters");
        add("naturalist.subtitles.entity.scorpion.hurt", "Scorpion hurts");
        add("naturalist.subtitles.entity.scorpion.death", "Scorpion dies");
        add("naturalist.subtitles.entity.scorpion.attack", "Scorpion stings");

        add("naturalist.subtitles.entity.snail.ambient", "Snail squelches");
        add("naturalist.subtitles.entity.snail.crush", "Snail crushes");
        add("naturalist.subtitles.entity.snail.hurt", "Snail hurts");
        add("naturalist.subtitles.entity.snail.move", "Snail inches");

        add("naturalist.subtitles.entity.snake.ambient", "Snake slithers");
        add("naturalist.subtitles.entity.snake.death", "Snake dies");
        add("naturalist.subtitles.entity.snake.hiss", "Snake hisses");
        add("naturalist.subtitles.entity.snake.hurt", "Snake hurts");
        add("naturalist.subtitles.entity.snake.rattle", "Snake rattles");

        add("naturalist.subtitles.entity.tiger.ambient", "Tiger growls");
        add("naturalist.subtitles.entity.tiger.hurt", "Tiger hurts");
        add("naturalist.subtitles.entity.tiger.death", "Tiger dies");
        add("naturalist.subtitles.entity.tiger.attack", "Tiger roars");
        add("naturalist.subtitles.entity.tiger.sleep", "Tiger snores");
        add("naturalist.subtitles.entity.tiger.prey", "Tiger chuffs");

        add("naturalist.subtitles.entity.tortoise.ambient", "Tortoise grunts");
        add("naturalist.subtitles.entity.tortoise.death", "Tortoise dies");
        add("naturalist.subtitles.entity.tortoise.egg_break", "Tortoise egg breaks");
        add("naturalist.subtitles.entity.tortoise.egg_crack", "Tortoise egg cracks");
        add("naturalist.subtitles.entity.tortoise.egg_hatch", "Tortoise egg hatches");
        add("naturalist.subtitles.entity.tortoise.hide", "Tortoise hides");
        add("naturalist.subtitles.entity.tortoise.hurt", "Tortoise hurts");
        add("naturalist.subtitles.entity.tortoise.shell_block", "Tortoise shell blocks");
        add("naturalist.subtitles.entity.tortoise.thud", "Tortoise thuds");

        add("naturalist.subtitles.entity.turkey.ambient", "Turkey gobbles");
        add("naturalist.subtitles.entity.turkey.hurt", "Turkey hurts");
        add("naturalist.subtitles.entity.turkey.death", "Turkey dies");
        add("naturalist.subtitles.entity.turkey.peck", "Turkey pecks");

        add("naturalist.subtitles.entity.vulture.ambient", "Vulture squawks");
        add("naturalist.subtitles.entity.vulture.death", "Vulture dies");
        add("naturalist.subtitles.entity.vulture.hurt", "Vulture hurts");

        add("naturalist.subtitles.entity.whale.ambient", "Whale sings");
        add("naturalist.subtitles.entity.whale.hurt", "Whale hurts");
        add("naturalist.subtitles.entity.whale.death", "Whale dies");
        add("naturalist.subtitles.entity.whale.blowhole", "Whale spouts");

        add("naturalist.subtitles.entity.zebra.ambient", "Zebra brays");
        add("naturalist.subtitles.entity.zebra.angry", "Zebra whinnies");
        add("naturalist.subtitles.entity.zebra.breathe", "Zebra breathes");
        add("naturalist.subtitles.entity.zebra.death", "Zebra dies");
        add("naturalist.subtitles.entity.zebra.eat", "Zebra eats");
        add("naturalist.subtitles.entity.zebra.hurt", "Zebra hurts");
        add("naturalist.subtitles.entity.zebra.jump", "Zebra jumps");

        add("tag.item.minecraft.meat", "Meat");
        add("tag.item.naturalist.alligator_food_items", "Alligator Food");
        add("tag.item.naturalist.bear_furs", "Bear Furs");
        add("tag.item.naturalist.bear_tempt_items", "Bear Tempt Items");
        add("tag.item.naturalist.bird_food_items", "Bird Food");
        add("tag.item.naturalist.boar_food_items", "Boar Food");
        add("tag.item.naturalist.capybara_food_items", "Capybara Food");
        add("tag.item.naturalist.crab_food", "Crab Food");
        add("tag.item.naturalist.duck_food_items", "Duck Food");
        add("tag.item.naturalist.eggs", "Eggs");
        add("tag.item.naturalist.giant_isopod_food", "Giant Isopod Food");
        add("tag.item.naturalist.giraffe_food_items", "Giraffe Food");
        add("tag.item.naturalist.great_white_shark_food_items", "Great White Shark Food");
        add("tag.item.naturalist.hedgehog_food_items", "Hedgehog Food");
        add("tag.item.naturalist.komodo_dragon_food_items", "Komodo Dragon Food");
        add("tag.item.naturalist.lion_food_items", "Lion Food");
        add("tag.item.naturalist.lizard_tempt_items", "Lizard Tempt Items");
        add("tag.item.naturalist.ostrich_food_items", "Ostrich Food");
        add("tag.item.naturalist.rat_food", "Rat Food");
        add("tag.item.naturalist.shears", "Shears");
        add("tag.item.naturalist.snake_tame_items", "Snake Tame Items");
        add("tag.item.naturalist.snake_tempt_items", "Snake Tempt Items");
        add("tag.item.naturalist.tiger_food_items", "Tiger Food");
        add("tag.item.naturalist.tortoise_tempt_items", "Tortoise Tempt Items");
        add("tag.item.naturalist.turkey_food_items", "Turkey Food");
    }
}
