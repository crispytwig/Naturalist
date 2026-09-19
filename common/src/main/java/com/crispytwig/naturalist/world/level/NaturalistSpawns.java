package com.crispytwig.naturalist.world.level;

import com.crispytwig.naturalist.tags.NaturalistBiomeTags;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public final class NaturalistSpawns {
    private NaturalistSpawns() {
    }

    @FunctionalInterface
    public interface SpawnConsumer {
        void accept(TagKey<Biome> hasTag, @Nullable TagKey<Biome> blacklistTag, MobCategory category, EntityType<?> type, int weight, int min, int max);
    }

    public static void forEachSpawn(SpawnConsumer c) {
        c.accept(NaturalistBiomeTags.HAS_ALLIGATOR, NaturalistBiomeTags.BLACKLIST_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR.get(), 10, 2, 3);
        c.accept(NaturalistBiomeTags.HAS_ANGLERFISH, NaturalistBiomeTags.BLACKLIST_ANGLERFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.ANGLERFISH.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_RAY, NaturalistBiomeTags.BLACKLIST_RAY, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.RAY.get(), 8, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_BLOBFISH, NaturalistBiomeTags.BLACKLIST_BLOBFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BLOBFISH.get(), 6, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_WHALE, NaturalistBiomeTags.BLACKLIST_WHALE, MobCategory.WATER_CREATURE, NaturalistEntityTypes.WHALE.get(), 5, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_GREAT_WHITE_SHARK, NaturalistBiomeTags.BLACKLIST_GREAT_WHITE_SHARK, MobCategory.WATER_CREATURE, NaturalistEntityTypes.GREAT_WHITE_SHARK.get(), 4, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_PIRANHA, NaturalistBiomeTags.BLACKLIST_PIRANHA, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.PIRANHA.get(), 8, 2, 4);
        c.accept(NaturalistBiomeTags.HAS_BASS, NaturalistBiomeTags.BLACKLIST_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS.get(), 10, 3, 6);
        c.accept(NaturalistBiomeTags.HAS_BEAR, NaturalistBiomeTags.BLACKLIST_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), 10, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_BLACK_BEAR, NaturalistBiomeTags.BLACKLIST_BLACK_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BLACK_BEAR.get(), 10, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_BLUEJAY, NaturalistBiomeTags.BLACKLIST_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_BOAR, NaturalistBiomeTags.BLACKLIST_BOAR, MobCategory.CREATURE, NaturalistEntityTypes.BOAR.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_BUTTERFLY, NaturalistBiomeTags.BLACKLIST_BUTTERFLY, MobCategory.AMBIENT, NaturalistEntityTypes.BUTTERFLY.get(), 10, 3, 6);
        c.accept(NaturalistBiomeTags.HAS_CANARY, NaturalistBiomeTags.BLACKLIST_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_CARDINAL, NaturalistBiomeTags.BLACKLIST_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_CAPYBARA, NaturalistBiomeTags.BLACKLIST_CAPYBARA, MobCategory.CREATURE, NaturalistEntityTypes.CAPYBARA.get(), 10, 2, 4);
        c.accept(NaturalistBiomeTags.HAS_CATFISH, NaturalistBiomeTags.BLACKLIST_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH.get(), 10, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_CLAM, NaturalistBiomeTags.BLACKLIST_CLAM, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CLAM.get(), 8, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_CORAL_SNAKE, NaturalistBiomeTags.BLACKLIST_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_CRAB, NaturalistBiomeTags.BLACKLIST_CRAB, MobCategory.CREATURE, NaturalistEntityTypes.CRAB.get(), 10, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_DEER, NaturalistBiomeTags.BLACKLIST_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), 10, 3, 5);
        c.accept(NaturalistBiomeTags.HAS_DESERT_SCORPION, NaturalistBiomeTags.BLACKLIST_DESERT_SCORPION, MobCategory.CREATURE, NaturalistEntityTypes.DESERT_SCORPION.get(), 20, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_JUNGLE_SCORPION, NaturalistBiomeTags.BLACKLIST_JUNGLE_SCORPION, MobCategory.CREATURE, NaturalistEntityTypes.JUNGLE_SCORPION.get(), 30, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_DRAGONFLY, NaturalistBiomeTags.BLACKLIST_DRAGONFLY, MobCategory.AMBIENT, NaturalistEntityTypes.DRAGONFLY.get(), 10, 2, 4);
        c.accept(NaturalistBiomeTags.HAS_DUCK, NaturalistBiomeTags.BLACKLIST_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_ELEPHANT, NaturalistBiomeTags.BLACKLIST_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), 5, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_FINCH, NaturalistBiomeTags.BLACKLIST_FINCH, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_FIREFLY, NaturalistBiomeTags.BLACKLIST_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY.get(), 10, 2, 4);
        c.accept(BiomeTags.IS_FOREST, NaturalistBiomeTags.BLACKLIST_FOREST_FOXES, MobCategory.CREATURE, EntityType.FOX, 10, 1, 2);
        c.accept(BiomeTags.IS_FOREST, NaturalistBiomeTags.BLACKLIST_FOREST_RABBITS, MobCategory.CREATURE, EntityType.RABBIT, 10, 2, 3);
        c.accept(NaturalistBiomeTags.HAS_GIANT_ISOPOD, NaturalistBiomeTags.BLACKLIST_GIANT_ISOPOD, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.GIANT_ISOPOD.get(), 8, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_HEDGEHOG, NaturalistBiomeTags.BLACKLIST_HEDGEHOG, MobCategory.CREATURE, NaturalistEntityTypes.HEDGEHOG.get(), 10, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_JELLYFISH, NaturalistBiomeTags.BLACKLIST_JELLYFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.JELLYFISH.get(), 10, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_GIRAFFE, NaturalistBiomeTags.BLACKLIST_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), 5, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_HIPPO, NaturalistBiomeTags.BLACKLIST_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO.get(), 10, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_KOMODO_DRAGON, NaturalistBiomeTags.BLACKLIST_KOMODO_DRAGON, MobCategory.CREATURE, NaturalistEntityTypes.KOMODO_DRAGON.get(), 20, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_LION, NaturalistBiomeTags.BLACKLIST_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION.get(), 5, 3, 5);
        c.accept(NaturalistBiomeTags.HAS_LIZARD, NaturalistBiomeTags.BLACKLIST_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_MAMMOTH, NaturalistBiomeTags.BLACKLIST_MAMMOTH, MobCategory.CREATURE, NaturalistEntityTypes.MAMMOTH.get(), 5, 2, 3);
        c.accept(NaturalistBiomeTags.HAS_MOLE, NaturalistBiomeTags.BLACKLIST_MOLE, MobCategory.CREATURE, NaturalistEntityTypes.MOLE.get(), 10, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_OSTRICH, NaturalistBiomeTags.BLACKLIST_OSTRICH, MobCategory.CREATURE, NaturalistEntityTypes.OSTRICH.get(), 28, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_RAT, NaturalistBiomeTags.BLACKLIST_RAT, MobCategory.CREATURE, NaturalistEntityTypes.RAT.get(), 10, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_RHINO, NaturalistBiomeTags.BLACKLIST_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO.get(), 1, 1, 3);
        c.accept(NaturalistBiomeTags.HAS_ROBIN, NaturalistBiomeTags.BLACKLIST_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_SNAIL, NaturalistBiomeTags.BLACKLIST_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), 10, 2, 3);
        c.accept(NaturalistBiomeTags.HAS_SNAKE, NaturalistBiomeTags.BLACKLIST_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_RATTLESNAKE, NaturalistBiomeTags.BLACKLIST_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_SPARROW, NaturalistBiomeTags.BLACKLIST_SPARROW, MobCategory.CREATURE, NaturalistEntityTypes.BIRD.get(), 10, 3, 4);
        c.accept(NaturalistBiomeTags.HAS_STARFISH, NaturalistBiomeTags.BLACKLIST_STARFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.STARFISH.get(), 8, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_TIGER, NaturalistBiomeTags.BLACKLIST_TIGER, MobCategory.CREATURE, NaturalistEntityTypes.TIGER.get(), 8, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_TORTOISE, NaturalistBiomeTags.BLACKLIST_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE.get(), 10, 1, 1);
        c.accept(NaturalistBiomeTags.HAS_TURKEY, NaturalistBiomeTags.BLACKLIST_TURKEY, MobCategory.CREATURE, NaturalistEntityTypes.TURKEY.get(), 17, 1, 2);
        c.accept(NaturalistBiomeTags.HAS_VULTURE, NaturalistBiomeTags.BLACKLIST_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE.get(), 3, 3, 5);
        c.accept(NaturalistBiomeTags.HAS_ZEBRA, NaturalistBiomeTags.BLACKLIST_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA.get(), 1, 2, 6);
    }
}
