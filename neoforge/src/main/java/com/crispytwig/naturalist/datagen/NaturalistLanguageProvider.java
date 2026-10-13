package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class NaturalistLanguageProvider extends LanguageProvider {
    public NaturalistLanguageProvider(PackOutput output) {
        super(output, Naturalist.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.naturalist.tab", "Naturalist");

        add("tooltip.naturalist.monarch", "Monarch");
        add("tooltip.naturalist.clouded_yellow", "Clouded Yellow");
        add("tooltip.naturalist.swallowtail", "Swallowtail");
        add("tooltip.naturalist.blue_morpho", "Blue Morpho");
        add("tooltip.naturalist.jade_green_swallowtail", "Jade Green Swallowtail");
        add("tooltip.naturalist.purple_emperor", "Purple Emperor");
        add("tooltip.naturalist.red_admiral", "Red Admiral");

        add("block.naturalist.alligator_egg", "Alligator Egg");
        add("block.naturalist.azure_froglass", "Azure Froglass");
        add("block.naturalist.azure_froglass_pane", "Azure Froglass Pane");
        add("block.naturalist.cattail", "Cattail");
        add("block.naturalist.chrysalis", "Chrysalis");
        add("block.naturalist.crimson_froglass", "Crimson Froglass");
        add("block.naturalist.crimson_froglass_pane", "Crimson Froglass Pane");
        add("block.naturalist.cut_shellstone", "Cut Shellstone");
        add("block.naturalist.cut_shellstone_slab", "Cut Shellstone Slab");
        add("block.naturalist.cut_shellstone_stairs", "Cut Shellstone Stairs");
        add("block.naturalist.cut_shellstone_wall", "Cut Shellstone Wall");
        add("block.naturalist.duckweed", "Duckweed");
        add("block.naturalist.ostrich_egg", "Ostrich Egg");
        add("block.naturalist.shellstone", "Shellstone");
        add("block.naturalist.shellstone_bricks", "Shellstone Bricks");
        add("block.naturalist.shellstone_brick_slab", "Shellstone Brick Slab");
        add("block.naturalist.shellstone_brick_stairs", "Shellstone Brick Stairs");
        add("block.naturalist.shellstone_brick_wall", "Shellstone Brick Wall");
        add("block.naturalist.shellstone_slab", "Shellstone Slab");
        add("block.naturalist.shellstone_stairs", "Shellstone Stairs");
        add("block.naturalist.shellstone_wall", "Shellstone Wall");
        add("block.naturalist.smooth_shellstone", "Smooth Shellstone");
        add("block.naturalist.smooth_shellstone_slab", "Smooth Shellstone Slab");
        add("block.naturalist.smooth_shellstone_stairs", "Smooth Shellstone Stairs");
        add("block.naturalist.smooth_shellstone_wall", "Smooth Shellstone Wall");
        add("block.naturalist.snail_eggs", "Snail Eggs");
        add("block.naturalist.plush_bear", "Bear Plush");
        add("block.naturalist.tortoise_egg", "Tortoise Egg");
        add("block.naturalist.verdant_froglass", "Verdant Froglass");
        add("block.naturalist.verdant_froglass_pane", "Verdant Froglass Pane");

        add("entity.naturalist.alligator", "Alligator");
        add("entity.naturalist.bass", "Bass");
        add("entity.naturalist.bear", "Bear");
        add("entity.naturalist.bluejay", "Bluejay");
        add("entity.naturalist.boar", "Boar");
        add("entity.naturalist.butterfly", "Butterfly");
        add("entity.naturalist.canary", "Canary");
        add("entity.naturalist.cardinal", "Cardinal");
        add("entity.naturalist.caterpillar", "Caterpillar");
        add("entity.naturalist.catfish", "Catfish");
        add("entity.naturalist.coral_snake", "Coral Snake");
        add("entity.naturalist.deer", "Deer");
        add("entity.naturalist.dragonfly", "Dragonfly");
        add("entity.naturalist.duck", "Duck");
        add("entity.naturalist.elephant", "Elephant");
        add("entity.naturalist.finch", "Finch");
        add("entity.naturalist.firefly", "Firefly");
        add("entity.naturalist.giraffe", "Giraffe");
        add("entity.naturalist.hippo", "Hippo");
        add("entity.naturalist.lion", "Lion");
        add("entity.naturalist.lizard", "Lizard");
        add("entity.naturalist.lizard_tail", "Lizard Tail");
        add("entity.naturalist.ostrich", "Ostrich");
        add("entity.naturalist.desert_scorpion", "Desert Scorpion");
        add("entity.naturalist.great_white_shark", "Great White Shark");
        add("entity.naturalist.capybara", "Capybara");
        add("entity.naturalist.hedgehog", "Hedgehog");
        add("entity.naturalist.jungle_scorpion", "Jungle Scorpion");
        add("entity.naturalist.rattlesnake", "Rattlesnake");
        add("entity.naturalist.rhino", "Rhino");
        add("entity.naturalist.robin", "Robin");
        add("entity.naturalist.snail", "Snail");
        add("entity.naturalist.snake", "Snake");
        add("entity.naturalist.sparrow", "Sparrow");
        add("entity.naturalist.tortoise", "Tortoise");
        add("entity.naturalist.turkey", "Turkey");
        add("entity.naturalist.vulture", "Vulture");
        add("entity.naturalist.zebra", "Zebra");

        add("item.naturalist.alligator_spawn_egg", "Alligator Spawn Egg");
        add("item.naturalist.antler", "Antler");
        add("item.naturalist.bass", "Raw Bass");
        add("item.naturalist.bass_bucket", "Bucket of Bass");
        add("item.naturalist.bass_spawn_egg", "Bass Spawn Egg");
        add("item.naturalist.bear_spawn_egg", "Brown Bear Spawn Egg");
        add("item.naturalist.bluejay_spawn_egg", "Bluejay Spawn Egg");
        add("item.naturalist.boar_spawn_egg", "Boar Spawn Egg");
        add("item.naturalist.capture_net", "Capture Net");
        add("item.naturalist.capybara_spawn_egg", "Capybara Spawn Egg");
        add("item.naturalist.hedgehog", "Hedgehog");
        add("item.naturalist.hedgehog_spawn_egg", "Hedgehog Spawn Egg");
        add("item.naturalist.bushmeat", "Bushmeat");
        add("item.naturalist.butterfly", "Butterfly");
        add("item.naturalist.butterfly_spawn_egg", "Butterfly Spawn Egg");
        add("item.naturalist.canary_spawn_egg", "Canary Spawn Egg");
        add("item.naturalist.cardinal_spawn_egg", "Cardinal Spawn Egg");
        add("item.naturalist.caterpillar", "Caterpillar");
        add("item.naturalist.dragonfly", "Dragonfly");
        add("item.naturalist.firefly", "Firefly");
        add("item.naturalist.bee", "Bee");
        add("item.naturalist.silverfish", "Silverfish");
        add("item.naturalist.endermite", "Endermite");
        add("item.naturalist.caterpillar_spawn_egg", "Caterpillar Spawn Egg");
        add("item.naturalist.catfish", "Raw Catfish");
        add("item.naturalist.catfish_bucket", "Bucket of Catfish");
        add("item.naturalist.catfish_spawn_egg", "Catfish Spawn Egg");
        add("item.naturalist.cattail_fluff", "Cattail Fluff");
        add("item.naturalist.cooked_bass", "Cooked Bass");
        add("item.naturalist.cooked_bushmeat", "Cooked Bushmeat");
        add("item.naturalist.cooked_catfish", "Cooked Catfish");
        add("item.naturalist.cooked_duck", "Cooked Duck");
        add("item.naturalist.cooked_egg", "Cooked Egg");
        add("item.naturalist.cooked_fish_fillet", "Cooked Fish Fillet");
        add("item.naturalist.cooked_lizard_tail", "Cooked Lizard Tail");
        add("item.naturalist.cooked_venison", "Cooked Venison");
        add("item.naturalist.coral_snake_spawn_egg", "Coral Snake Spawn Egg");
        add("item.naturalist.deer_spawn_egg", "Deer Spawn Egg");
        add("item.naturalist.duck", "Raw Duck");
        add("item.naturalist.duck_egg", "Duck Egg");
        add("item.naturalist.duck_spawn_egg", "Duck Spawn Egg");
        add("item.naturalist.dragonfly_spawn_egg", "Dragonfly Spawn Egg");
        add("item.naturalist.elephant_spawn_egg", "Elephant Spawn Egg");
        add("item.naturalist.finch_spawn_egg", "Finch Spawn Egg");
        add("item.naturalist.firefly_spawn_egg", "Firefly Spawn Egg");
        add("item.naturalist.fur", "Fur");
        add("item.naturalist.giraffe_spawn_egg", "Giraffe Spawn Egg");
        add("item.naturalist.glow_goop", "Glow Goop");
        add("item.naturalist.hippo_spawn_egg", "Hippo Spawn Egg");
        add("item.naturalist.lion_spawn_egg", "Lion Spawn Egg");
        add("item.naturalist.lizard_spawn_egg", "Lizard Spawn Egg");
        add("item.naturalist.lizard_tail", "Lizard Tail");
        add("item.naturalist.rattlesnake_spawn_egg", "Rattlesnake Spawn Egg");
        add("item.naturalist.rhino_spawn_egg", "Rhino Spawn Egg");
        add("item.naturalist.robin_spawn_egg", "Robin Spawn Egg");
        add("item.naturalist.snail", "Snail");
        add("item.naturalist.snail_shell", "Snail Shell");
        add("item.naturalist.snail_spawn_egg", "Snail Spawn Egg");
        add("item.naturalist.snake_spawn_egg", "Snake Spawn Egg");
        add("item.naturalist.sparrow_spawn_egg", "Sparrow Spawn Egg");
        add("item.naturalist.tortoise_spawn_egg", "Tortoise Spawn Egg");
        add("item.naturalist.venison", "Venison");
        add("item.naturalist.vulture_spawn_egg", "Vulture Spawn Egg");
        add("item.naturalist.zebra_spawn_egg", "Zebra Spawn Egg");

        add("item.minecraft.tipped_arrow.effect.forest_dasher", "Arrow of the Forest Dasher");
        add("item.minecraft.potion.effect.forest_dasher", "Potion of the Forest Dasher");
        add("item.minecraft.splash_potion.effect.forest_dasher", "Splash Potion of the Forest Dasher");
        add("item.minecraft.lingering_potion.effect.forest_dasher", "Lingering Potion of the Forest Dasher");
        add("effect.naturalist.antivenom", "Antivenom");
        add("item.minecraft.tipped_arrow.effect.antivenom", "Arrow of Antivenom");
        add("item.minecraft.potion.effect.antivenom", "Potion of Antivenom");
        add("item.minecraft.splash_potion.effect.antivenom", "Splash Potion of Antivenom");
        add("item.minecraft.lingering_potion.effect.antivenom", "Lingering Potion of Antivenom");
        add("item.minecraft.tipped_arrow.effect.glowing", "Arrow of Glowing");
        add("item.minecraft.potion.effect.glowing", "Potion of Glowing");
        add("item.minecraft.splash_potion.effect.glowing", "Splash Potion of Glowing");
        add("item.minecraft.lingering_potion.effect.glowing", "Lingering Potion of Glowing");

        add("advancements.husbandry.ride_giraffe_with_map.title", "A Whole New World");
        add("advancements.husbandry.ride_giraffe_with_map.description", "Use a Map while riding a Giraffe");
        add("advancements.husbandry.feed_hippo_melon.title", "Hungry Hungry Hippos");
        add("advancements.husbandry.feed_hippo_melon.description", "Feed a Melon to a Hippo");
        add("advancements.husbandry.feed_bear_honeycomb.title", "Oh Bother");
        add("advancements.husbandry.feed_bear_honeycomb.description", "Feed a Honeycomb to a Bear");

        add("naturalist.subtitles.entity.tortoise.ambient", "Tortoise grunts");
        add("naturalist.subtitles.entity.tortoise.hide", "Tortoise hides");
        add("naturalist.subtitles.entity.tortoise.thud", "Tortoise thuds");
        add("naturalist.subtitles.entity.tortoise.hurt", "Tortoise hurts");
        add("naturalist.subtitles.entity.tortoise.shell_block", "Tortoise shell blocks");
        add("naturalist.subtitles.entity.tortoise.death", "Tortoise dies");
        add("naturalist.subtitles.entity.tortoise.ambient_baby", "Baby Tortoise grunts");
        add("naturalist.subtitles.entity.tortoise.hurt_baby", "Baby Tortoise hurts");
        add("naturalist.subtitles.entity.tortoise.death_baby", "Baby Tortoise dies");

        add("naturalist.subtitles.entity.bird.hurt", "Bird hurts");
        add("naturalist.subtitles.entity.bird.death", "Bird dies");
        add("naturalist.subtitles.entity.bird.eat", "Bird eats");
        add("naturalist.subtitles.entity.bird.fly", "Bird flutters");

        add("naturalist.subtitles.entity.bird.ambient_bluejay", "Bluejay chirps");
        add("naturalist.subtitles.entity.bird.ambient_canary", "Canary chirps");
        add("naturalist.subtitles.entity.bird.ambient_cardinal", "Cardinal chirps");
        add("naturalist.subtitles.entity.bird.ambient_robin", "Robin chirps");
        add("naturalist.subtitles.entity.bird.ambient_finch", "Finch chirps");
        add("naturalist.subtitles.entity.bird.ambient_sparrow", "Sparrow chirps");

        add("naturalist.subtitles.entity.deer.hurt", "Deer hurts");
        add("naturalist.subtitles.entity.deer.death", "Deer dies");
        add("naturalist.subtitles.entity.deer.ambient", "Deer grunts");
        add("naturalist.subtitles.entity.deer.hurt_baby", "Fawn hurts");
        add("naturalist.subtitles.entity.deer.ambient_baby", "Fawn bleats");
        add("naturalist.subtitles.entity.deer.death_baby", "Fawn dies");

        add("naturalist.subtitles.entity.elephant.hurt", "Elephant hurts");
        add("naturalist.subtitles.entity.elephant.death", "Elephant dies");
        add("naturalist.subtitles.entity.elephant.ambient", "Elephant trumpets");
        add("naturalist.subtitles.entity.elephant.ambient_baby", "Baby Elephant squeaks");
        add("naturalist.subtitles.entity.elephant.hurt_baby", "Baby Elephant hurts");
        add("naturalist.subtitles.entity.elephant.death_baby", "Baby Elephant dies");

        add("naturalist.subtitles.entity.firefly.ambient", "Firefly buzzes");
        add("naturalist.subtitles.entity.firefly.hurt", "Firefly hurts");
        add("naturalist.subtitles.entity.firefly.death", "Firefly dies");
        add("naturalist.subtitles.entity.firefly.hide", "Firefly hides");
        add("naturalist.subtitles.entity.bear.hurt", "Brown Bear hurts");
        add("naturalist.subtitles.entity.bear.death", "Brown Bear dies");
        add("naturalist.subtitles.entity.bear.ambient", "Brown Bear groans");
        add("naturalist.subtitles.entity.bear.ambient_baby", "Brown Bear cub hums");
        add("naturalist.subtitles.entity.bear.hurt_baby", "Brown Bear cub hurts");
        add("naturalist.subtitles.entity.bear.sleep", "Brown Bear snores");
        add("naturalist.subtitles.entity.bear.sniff", "Brown Bear sniffs");
        add("naturalist.subtitles.entity.bear.spit", "Brown Bear spits");
        add("naturalist.subtitles.entity.bear.eat", "Brown Bear eats");
        add("naturalist.subtitles.entity.bear.death_baby", "Brown Bear cub dies");
        add("naturalist.subtitles.entity.lion.hurt", "Lion hurts");
        add("naturalist.subtitles.entity.lion.death", "Lion dies");
        add("naturalist.subtitles.entity.lion.ambient", "Lion growls");
        add("naturalist.subtitles.entity.lion.roar", "Lion roars");
        add("naturalist.subtitles.entity.lion.ambient_baby", "Lion cub mews");
        add("naturalist.subtitles.entity.lion.hurt_baby", "Lion cub hurts");
        add("naturalist.subtitles.entity.lion.death_baby", "Lion cub dies");

        add("naturalist.subtitles.entity.rhino.scrape", "Rhino scrapes foot");
        add("naturalist.subtitles.entity.rhino.ambient", "Rhino growls");
        add("naturalist.subtitles.entity.rhino.ambient_baby", "Rhino baby coos");
        add("naturalist.subtitles.entity.rhino.hurt", "Rhino hurts");
        add("naturalist.subtitles.entity.rhino.death", "Rhino dies");

        add("naturalist.subtitles.entity.snail.ambient", "Snail squelches");
        add("naturalist.subtitles.entity.snail.hurt", "Snail hurts");
        add("naturalist.subtitles.entity.snail.crush", "Snail crushes");
        add("naturalist.subtitles.entity.snail.move", "Snail inches");

        add("naturalist.subtitles.entity.snake.ambient", "Snake slithers");
        add("naturalist.subtitles.entity.snake.hiss", "Snake hisses");
        add("naturalist.subtitles.entity.snake.hurt", "Snake hurts");
        add("naturalist.subtitles.entity.snake.death", "Snake dies");
        add("naturalist.subtitles.entity.snake.rattle", "Snake rattles");

        add("naturalist.subtitles.entity.zebra.ambient", "Zebra brays");
        add("naturalist.subtitles.entity.zebra.hurt", "Zebra hurts");
        add("naturalist.subtitles.entity.zebra.death", "Zebra dies");
        add("naturalist.subtitles.entity.zebra.eat", "Zebra eats");
        add("naturalist.subtitles.entity.zebra.breathe", "Zebra breathes");
        add("naturalist.subtitles.entity.zebra.jump", "Zebra jumps");
        add("naturalist.subtitles.entity.zebra.angry", "Zebra whinnies");

        add("naturalist.subtitles.entity.vulture.ambient", "Vulture squawks");
        add("naturalist.subtitles.entity.vulture.hurt", "Vulture hurts");
        add("naturalist.subtitles.entity.vulture.death", "Vulture dies");

        add("naturalist.subtitles.entity.giraffe.ambient", "Giraffe snorts");
        add("naturalist.subtitles.entity.giraffe.hurt", "Giraffe hurts");
        add("naturalist.subtitles.entity.giraffe.death", "Giraffe dies");
        add("naturalist.subtitles.entity.giraffe.ambient_baby", "Baby Giraffe hums");
        add("naturalist.subtitles.entity.giraffe.hurt_baby", "Baby Giraffe hurts");
        add("naturalist.subtitles.entity.giraffe.death_baby", "Baby Giraffe dies");

        add("naturalist.subtitles.entity.hippo.ambient", "Hippo grumbles");
        add("naturalist.subtitles.entity.hippo.hurt", "Hippo hurts");
        add("naturalist.subtitles.entity.hippo.death", "Hippo dies");

        add("naturalist.subtitles.entity.alligator.hurt", "Alligator hurts");
        add("naturalist.subtitles.entity.alligator.death", "Alligator dies");
        add("naturalist.subtitles.entity.alligator.ambient", "Alligator bellows");
        add("naturalist.subtitles.entity.alligator.ambient_baby", "Alligator baby chirps");
        add("naturalist.subtitles.entity.alligator.hurt_baby", "Alligator baby hurts");
        add("naturalist.subtitles.entity.alligator.death_baby", "Alligator baby dies");

        add("naturalist.subtitles.entity.boar.ambient", "Boar grunts");
        add("naturalist.subtitles.entity.boar.hurt", "Boar hurts");
        add("naturalist.subtitles.entity.boar.death", "Boar dies");
        add("naturalist.subtitles.entity.boar.ambient_baby", "Piglet squeals");
        add("naturalist.subtitles.entity.boar.hurt_baby", "Piglet hurts");
        add("naturalist.subtitles.entity.boar.death_baby", "Piglet dies");

        add("naturalist.subtitles.entity.catfish.flop", "Catfish flops");
        add("naturalist.subtitles.entity.bass.flop", "Bass flops");

        add("naturalist.subtitles.entity.dragonfly.loop", "Dragonfly buzzes");
        add("naturalist.subtitles.entity.dragonfly.hurt", "Dragonfly hurts");
        add("naturalist.subtitles.entity.dragonfly.death", "Dragonfly dies");

        add("naturalist.subtitles.entity.duck.ambient", "Duck quacks");
        add("naturalist.subtitles.entity.duck.hurt", "Duck hurts");
        add("naturalist.subtitles.entity.duck.death", "Duck dies");
        add("naturalist.subtitles.entity.duck.ambient_baby", "Duckling quacks");
        add("naturalist.subtitles.entity.duck.hurt_baby", "Duckling hurts");
        add("naturalist.subtitles.entity.duck.death_baby", "Duckling dies");

        add("naturalist.subtitles.entity.tortoise.egg_break", "Tortoise egg breaks");
        add("naturalist.subtitles.entity.tortoise.egg_crack", "Tortoise egg cracks");
        add("naturalist.subtitles.entity.tortoise.egg_hatch", "Tortoise egg hatches");

        add("naturalist.subtitles.entity.alligator.egg_break", "Alligator egg breaks");
        add("naturalist.subtitles.entity.alligator.egg_crack", "Alligator egg cracks");
        add("naturalist.subtitles.entity.alligator.egg_hatch", "Alligator egg hatches");

        add("naturalist.subtitles.entity.lizard.ambient", "Lizard chirps");
        add("naturalist.subtitles.entity.lizard.hurt", "Lizard hurts");
        add("naturalist.subtitles.entity.lizard.death", "Lizard dies");

        add("naturalist.subtitles.entity.butterfly.ambient", "Butterfly flutters");
        add("naturalist.subtitles.entity.butterfly.hurt", "Butterfly hurts");

        add("naturalist.configuration.disable_mobs", "Disable Mobs");
        add("naturalist.configuration.aquatic_mobs", "Aquatic Mobs");
        add("naturalist.configuration.land_mobs", "Land Mobs");
        add("naturalist.configuration.bugs", "Bugs");
        add("naturalist.configuration.remove_all_bugs", "Remove All Bugs");
        add("naturalist.configuration.behavior", "Behavior");
        add("naturalist.configuration.snail_crushing", "Crushable Snails");
        add("naturalist.configuration.bird_head_slow_falling", "Bird Slow Falling");
        add("naturalist.configuration.parrot_flight", "Parrot Flight");
        add("naturalist.configuration.alligator_removed", "Remove Alligator");
        add("naturalist.configuration.anglerfish_removed", "Remove Anglerfish");
        add("naturalist.configuration.ant_removed", "Remove Ant");
        add("naturalist.configuration.bass_removed", "Remove Bass");
        add("naturalist.configuration.bear_removed", "Remove Bear");
        add("naturalist.configuration.bird_removed", "Remove Birds");
        add("naturalist.configuration.black_bear_removed", "Remove Black Bear");
        add("naturalist.configuration.blobfish_removed", "Remove Blobfish");
        add("naturalist.configuration.boar_removed", "Remove Boar");
        add("naturalist.configuration.butterfly_removed", "Remove Butterfly");
        add("naturalist.configuration.capybara_removed", "Remove Capybara");
        add("naturalist.configuration.catfish_removed", "Remove Catfish");
        add("naturalist.configuration.clam_removed", "Remove Clam");
        add("naturalist.configuration.crab_removed", "Remove Crab");
        add("naturalist.configuration.deer_removed", "Remove Deer");
        add("naturalist.configuration.desert_scorpion_removed", "Remove Desert Scorpion");
        add("naturalist.configuration.dragonfly_removed", "Remove Dragonfly");
        add("naturalist.configuration.duck_removed", "Remove Duck");
        add("naturalist.configuration.elephant_removed", "Remove Elephant");
        add("naturalist.configuration.firefly_removed", "Remove Firefly");
        add("naturalist.configuration.forest_fox_removed", "Remove Forest Fox");
        add("naturalist.configuration.forest_rabbit_removed", "Remove Forest Rabbit");
        add("naturalist.configuration.giant_isopod_removed", "Remove Giant Isopod");
        add("naturalist.configuration.giraffe_removed", "Remove Giraffe");
        add("naturalist.configuration.great_white_shark_removed", "Remove Great White Shark");
        add("naturalist.configuration.hedgehog_removed", "Remove Hedgehog");
        add("naturalist.configuration.hippo_removed", "Remove Hippo");
        add("naturalist.configuration.jellyfish_removed", "Remove Jellyfish");
        add("naturalist.configuration.jungle_scorpion_removed", "Remove Jungle Scorpion");
        add("naturalist.configuration.komodo_dragon_removed", "Remove Komodo Dragon");
        add("naturalist.configuration.lion_removed", "Remove Lion");
        add("naturalist.configuration.lizard_removed", "Remove Lizard");
        add("naturalist.configuration.mammoth_removed", "Remove Mammoth");
        add("naturalist.configuration.mole_removed", "Remove Mole");
        add("naturalist.configuration.ostrich_removed", "Remove Ostrich");
        add("naturalist.configuration.piranha_removed", "Remove Piranha");
        add("naturalist.configuration.rat_removed", "Remove Rat");
        add("naturalist.configuration.ray_removed", "Remove Ray");
        add("naturalist.configuration.rhino_removed", "Remove Rhino");
        add("naturalist.configuration.snail_removed", "Remove Snail");
        add("naturalist.configuration.snake_removed", "Remove Snakes");
        add("naturalist.configuration.starfish_removed", "Remove Starfish");
        add("naturalist.configuration.tiger_removed", "Remove Tiger");
        add("naturalist.configuration.tortoise_removed", "Remove Tortoise");
        add("naturalist.configuration.turkey_removed", "Remove Turkey");
        add("naturalist.configuration.vulture_removed", "Remove Vulture");
        add("naturalist.configuration.whale_removed", "Remove Whale");
        add("naturalist.configuration.zebra_removed", "Remove Zebra");
    }
}
