package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

public class NaturalistEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public NaturalistEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(entityTypeTag("alexsmobs:anaconda_targets"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:catfish"))
                .add(entityType("naturalist:piranha"));

        tag(entityTypeTag("alexsmobs:bald_eagle_targets"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:duck"))
                .add(entityType("naturalist:lizard"))
                .add(entityType("naturalist:snake"));

        tag(entityTypeTag("alexsmobs:caiman_targets"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:bird"))
                .add(entityType("naturalist:duck"))
                .add(entityType("naturalist:piranha"))
                .add(entityType("naturalist:snail"));

        tag(entityTypeTag("alexsmobs:crocodile_targets"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:catfish"))
                .add(entityType("naturalist:piranha"));

        tag(entityTypeTag("alexsmobs:giant_squid_targets"))
                .add(entityType("naturalist:blobfish"));

        tag(entityTypeTag("alexsmobs:mantis_shrimp_targets"))
                .add(entityType("naturalist:clam"))
                .add(entityType("naturalist:crab"));

        tag(entityTypeTag("alexsmobs:neutral_land_animals"))
                .add(entityType("naturalist:bear"))
                .add(entityType("naturalist:boar"))
                .add(entityType("naturalist:elephant"))
                .add(entityType("naturalist:hippo"))
                .add(entityType("naturalist:lion"))
                .add(entityType("naturalist:mammoth"))
                .add(entityType("naturalist:rhino"));

        tag(entityTypeTag("alexsmobs:orca_targets"))
                .add(entityType("naturalist:ray"));

        tag(entityTypeTag("alexsmobs:passive_land_animals"))
                .add(entityType("naturalist:deer"))
                .add(entityType("naturalist:duck"))
                .add(entityType("naturalist:giraffe"))
                .add(entityType("naturalist:lizard"))
                .add(entityType("naturalist:tortoise"))
                .add(entityType("naturalist:zebra"));

        tag(entityTypeTag("alexsmobs:snow_leopard_targets"))
                .add(entityType("naturalist:boar"))
                .add(entityType("naturalist:deer"));

        tag(entityTypeTag("fieldguide:blacklisted"))
                .add(entityType("naturalist:lizard_tail"));

        tag(entityTypeTag("minecraft:aquatic"))
                .add(entityType("naturalist:anglerfish"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:blobfish"))
                .add(entityType("naturalist:catfish"))
                .add(entityType("naturalist:clam"))
                .add(entityType("naturalist:giant_isopod"))
                .add(entityType("naturalist:jellyfish"))
                .add(entityType("naturalist:piranha"))
                .add(entityType("naturalist:ray"))
                .add(entityType("naturalist:starfish"));

        tag(entityTypeTag("minecraft:arthropod"))
                .add(entityType("naturalist:ant"))
                .add(entityType("naturalist:butterfly"))
                .add(entityType("naturalist:caterpillar"))
                .add(entityType("naturalist:crab"))
                .add(entityType("naturalist:desert_scorpion"))
                .add(entityType("naturalist:dragonfly"))
                .add(entityType("naturalist:firefly"))
                .add(entityType("naturalist:giant_isopod"))
                .add(entityType("naturalist:jungle_scorpion"));

        tag(entityTypeTag("minecraft:axolotl_hunt_targets"))
                .add(entityType("naturalist:bass"))
                .add(entityType("naturalist:piranha"));

        tag(entityTypeTag("minecraft:can_breathe_under_water"))
                .add(entityType("naturalist:alligator"))
                .add(entityType("naturalist:giant_isopod"))
                .add(entityType("naturalist:capybara"));

        tag(entityTypeTag("minecraft:frog_food"))
                .add(entityType("naturalist:firefly"))
                .add(entityType("naturalist:dragonfly"));

        tag(entityTypeTag("naturalist:piranha_hostiles"))
                .add(entityType("minecraft:cod"))
                .add(entityType("minecraft:salmon"))
                .addOptional(entityType("alexsmobs:mudskipper"));

        tag(NaturalistEntityTypeTags.ALLIGATOR_HOSTILES)
                .add(NaturalistEntityTypes.DEER.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.SNAKE.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.DUCK.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.CATFISH.get().builtInRegistryHolder().key())
                .add(EntityTypes.FROG.builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "shoebill")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "terrapin")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "catfish")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "mudskipper")));

        tag(NaturalistEntityTypeTags.BEAR_HOSTILES)
                .add(EntityTypes.SALMON.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BASS.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.DEER.get().builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "moose")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "catfish")));

        tag(NaturalistEntityTypeTags.BOAR_HOSTILES)
                .add(NaturalistEntityTypes.SNAKE.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.SNAIL.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.CATFISH_HOSTILES)
                .add(EntityTypes.TROPICAL_FISH.builtInRegistryHolder().key())
                .add(EntityTypes.COD.builtInRegistryHolder().key())
                .add(EntityTypes.TADPOLE.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BASS.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.ANGLERFISH_HOSTILES)
                .add(EntityTypes.TROPICAL_FISH.builtInRegistryHolder().key())
                .add(EntityTypes.COD.builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "blobfish")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "flying_fish")));

        tag(NaturalistEntityTypeTags.DEER_PREDATORS)
                .add(NaturalistEntityTypes.BEAR.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BLACK_BEAR.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.TIGER.get().builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "grizzly_bear")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "crocodile")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "tiger")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "snow_leopard")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "anaconda")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "komodo_dragon")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "froststalker")));

        tag(NaturalistEntityTypeTags.LION_HOSTILES)
                .add(NaturalistEntityTypes.RHINO.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.ZEBRA.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BOAR.get().builtInRegistryHolder().key())
                .add(EntityTypes.HORSE.builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "gazelle")));

        tag(NaturalistEntityTypeTags.TIGER_HOSTILES)
                .add(NaturalistEntityTypes.BOAR.get().builtInRegistryHolder().key())
                .add(EntityTypes.PIG.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.DEER.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.ZEBRA.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.SNAKE.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.KOMODO_DRAGON_HOSTILES)
                .add(EntityTypes.CHICKEN.builtInRegistryHolder().key())
                .add(EntityTypes.RABBIT.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.LIZARD.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.SNAKE.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BOAR.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.SCORPION_HOSTILES)
                .add(NaturalistEntityTypes.LIZARD.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.GREAT_WHITE_SHARK_HOSTILES)
                .add(EntityTypes.COD.builtInRegistryHolder().key())
                .add(EntityTypes.SALMON.builtInRegistryHolder().key())
                .add(EntityTypes.TROPICAL_FISH.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BASS.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.CATFISH.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.PIRANHA.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BLOBFISH.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.ANGLERFISH.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.TURKEY_HOSTILES)
                .add(NaturalistEntityTypes.ANT.get().builtInRegistryHolder().key());

        tag(NaturalistEntityTypeTags.SAFE_EGG_WALKERS)
                .add(NaturalistEntityTypes.BUTTERFLY.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.BIRD.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.DRAGONFLY.get().builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.FIREFLY.get().builtInRegistryHolder().key())
                .add(EntityTypes.BAT.builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "shoebill")));

        tag(NaturalistEntityTypeTags.SNAKE_HOSTILES)
                .add(EntityTypes.RABBIT.builtInRegistryHolder().key())
                .add(EntityTypes.CHICKEN.builtInRegistryHolder().key())
                .add(EntityTypes.SILVERFISH.builtInRegistryHolder().key())
                .add(NaturalistEntityTypes.SNAIL.get().builtInRegistryHolder().key())
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "jerboa")))
                .addOptional(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("alexsmobs", "rain_frog")));

        tag(NaturalistEntityTypeTags.VULTURE_HOSTILES)
                .add(EntityTypes.ZOMBIE.builtInRegistryHolder().key())
                .add(EntityTypes.ZOMBIE_VILLAGER.builtInRegistryHolder().key())
                .add(EntityTypes.HUSK.builtInRegistryHolder().key())
                .add(EntityTypes.ZOMBIFIED_PIGLIN.builtInRegistryHolder().key())
                .add(EntityTypes.ZOGLIN.builtInRegistryHolder().key())
                .add(EntityTypes.ZOMBIE_HORSE.builtInRegistryHolder().key());
    }

    private static ResourceKey<EntityType<?>> entityType(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.parse(id));
    }

    private static TagKey<EntityType<?>> entityTypeTag(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.parse(id));
    }
}
