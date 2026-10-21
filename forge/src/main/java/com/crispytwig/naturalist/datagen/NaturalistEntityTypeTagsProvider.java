package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NaturalistEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public NaturalistEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(entityTypeTag("alexsmobs:anaconda_targets"))
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(NaturalistEntityTypes.PIRANHA.get());

        tag(entityTypeTag("alexsmobs:bald_eagle_targets"))
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.DUCK.get())
                .add(NaturalistEntityTypes.LIZARD.get())
                .add(NaturalistEntityTypes.SNAKE.get());

        tag(entityTypeTag("alexsmobs:caiman_targets"))
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.BIRD.get())
                .add(NaturalistEntityTypes.DUCK.get())
                .add(NaturalistEntityTypes.PIRANHA.get())
                .add(NaturalistEntityTypes.SNAIL.get());

        tag(entityTypeTag("alexsmobs:crocodile_targets"))
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(NaturalistEntityTypes.PIRANHA.get());

        tag(entityTypeTag("alexsmobs:giant_squid_targets"))
                .add(NaturalistEntityTypes.BLOBFISH.get());

        tag(entityTypeTag("alexsmobs:mantis_shrimp_targets"))
                .add(NaturalistEntityTypes.CLAM.get())
                .add(NaturalistEntityTypes.CRAB.get());

        tag(entityTypeTag("alexsmobs:neutral_land_animals"))
                .add(NaturalistEntityTypes.BEAR.get())
                .add(NaturalistEntityTypes.BOAR.get())
                .add(NaturalistEntityTypes.ELEPHANT.get())
                .add(NaturalistEntityTypes.HIPPO.get())
                .add(NaturalistEntityTypes.LION.get())
                .add(NaturalistEntityTypes.MAMMOTH.get())
                .add(NaturalistEntityTypes.RHINO.get());

        tag(entityTypeTag("alexsmobs:orca_targets"))
                .add(NaturalistEntityTypes.RAY.get());

        tag(entityTypeTag("alexsmobs:passive_land_animals"))
                .add(NaturalistEntityTypes.DEER.get())
                .add(NaturalistEntityTypes.DUCK.get())
                .add(NaturalistEntityTypes.GIRAFFE.get())
                .add(NaturalistEntityTypes.LIZARD.get())
                .add(NaturalistEntityTypes.TORTOISE.get())
                .add(NaturalistEntityTypes.ZEBRA.get());

        tag(entityTypeTag("alexsmobs:snow_leopard_targets"))
                .add(NaturalistEntityTypes.BOAR.get())
                .add(NaturalistEntityTypes.DEER.get());

        tag(entityTypeTag("fieldguide:blacklisted"))
                .add(NaturalistEntityTypes.LIZARD_TAIL.get());

        tag(entityTypeTag("minecraft:aquatic"))
                .add(NaturalistEntityTypes.ANGLERFISH.get())
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.BLOBFISH.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(NaturalistEntityTypes.CLAM.get())
                .add(NaturalistEntityTypes.GIANT_ISOPOD.get())
                .add(NaturalistEntityTypes.JELLYFISH.get())
                .add(NaturalistEntityTypes.PIRANHA.get())
                .add(NaturalistEntityTypes.RAY.get())
                .add(NaturalistEntityTypes.STARFISH.get());

        tag(entityTypeTag("minecraft:arthropod"))
                .add(NaturalistEntityTypes.ANT.get())
                .add(NaturalistEntityTypes.BUTTERFLY.get())
                .add(NaturalistEntityTypes.CATERPILLAR.get())
                .add(NaturalistEntityTypes.CRAB.get())
                .add(NaturalistEntityTypes.DESERT_SCORPION.get())
                .add(NaturalistEntityTypes.DRAGONFLY.get())
                .add(NaturalistEntityTypes.FIREFLY.get())
                .add(NaturalistEntityTypes.GIANT_ISOPOD.get())
                .add(NaturalistEntityTypes.JUNGLE_SCORPION.get());

        tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.PIRANHA.get());

        tag(entityTypeTag("minecraft:can_breathe_under_water"))
                .add(NaturalistEntityTypes.ALLIGATOR.get())
                .add(NaturalistEntityTypes.GIANT_ISOPOD.get())
                .add(NaturalistEntityTypes.CAPYBARA.get());

        tag(EntityTypeTags.FROG_FOOD)
                .add(NaturalistEntityTypes.FIREFLY.get())
                .add(NaturalistEntityTypes.DRAGONFLY.get());

        tag(NaturalistEntityTypeTags.ALLIGATOR_HOSTILES)
                .add(NaturalistEntityTypes.DEER.get())
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(NaturalistEntityTypes.DUCK.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(EntityType.FROG)
                .addOptional(alexsmobs("shoebill"))
                .addOptional(alexsmobs("terrapin"))
                .addOptional(alexsmobs("catfish"))
                .addOptional(alexsmobs("mudskipper"));

        tag(NaturalistEntityTypeTags.ANGLERFISH_HOSTILES)
                .add(EntityType.TROPICAL_FISH)
                .add(EntityType.COD)
                .addOptional(alexsmobs("blobfish"))
                .addOptional(alexsmobs("flying_fish"));

        tag(NaturalistEntityTypeTags.BEAR_HOSTILES)
                .add(EntityType.SALMON)
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.DEER.get())
                .addOptional(alexsmobs("moose"))
                .addOptional(alexsmobs("catfish"));

        tag(NaturalistEntityTypeTags.BOAR_HOSTILES)
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(NaturalistEntityTypes.SNAIL.get());

        tag(NaturalistEntityTypeTags.CATFISH_HOSTILES)
                .add(EntityType.TROPICAL_FISH)
                .add(EntityType.COD)
                .add(EntityType.TADPOLE)
                .add(NaturalistEntityTypes.BASS.get());

        tag(NaturalistEntityTypeTags.DEER_PREDATORS)
                .add(NaturalistEntityTypes.BEAR.get())
                .add(NaturalistEntityTypes.BLACK_BEAR.get())
                .add(NaturalistEntityTypes.TIGER.get())
                .addOptional(alexsmobs("grizzly_bear"))
                .addOptional(alexsmobs("crocodile"))
                .addOptional(alexsmobs("tiger"))
                .addOptional(alexsmobs("snow_leopard"))
                .addOptional(alexsmobs("anaconda"))
                .addOptional(alexsmobs("komodo_dragon"))
                .addOptional(alexsmobs("froststalker"));

        tag(NaturalistEntityTypeTags.GREAT_WHITE_SHARK_HOSTILES)
                .add(EntityType.COD)
                .add(EntityType.SALMON)
                .add(EntityType.TROPICAL_FISH)
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(NaturalistEntityTypes.PIRANHA.get())
                .add(NaturalistEntityTypes.BLOBFISH.get())
                .add(NaturalistEntityTypes.ANGLERFISH.get());

        tag(NaturalistEntityTypeTags.KOMODO_DRAGON_HOSTILES)
                .add(EntityType.CHICKEN)
                .add(EntityType.RABBIT)
                .add(NaturalistEntityTypes.LIZARD.get())
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(NaturalistEntityTypes.BOAR.get());

        tag(NaturalistEntityTypeTags.LION_HOSTILES)
                .add(NaturalistEntityTypes.RHINO.get())
                .add(NaturalistEntityTypes.ZEBRA.get())
                .add(NaturalistEntityTypes.BOAR.get())
                .add(EntityType.HORSE)
                .addOptional(alexsmobs("gazelle"));

        tag(NaturalistEntityTypeTags.PIRANHA_HOSTILES)
                .add(EntityType.COD)
                .add(EntityType.SALMON)
                .addOptional(alexsmobs("mudskipper"));

        tag(NaturalistEntityTypeTags.SAFE_EGG_WALKERS)
                .add(NaturalistEntityTypes.BUTTERFLY.get())
                .add(NaturalistEntityTypes.BIRD.get())
                .add(NaturalistEntityTypes.DRAGONFLY.get())
                .add(NaturalistEntityTypes.FIREFLY.get())
                .add(EntityType.BAT)
                .addOptional(alexsmobs("shoebill"));

        tag(NaturalistEntityTypeTags.SCORPION_HOSTILES)
                .add(NaturalistEntityTypes.LIZARD.get());

        tag(NaturalistEntityTypeTags.SNAKE_HOSTILES)
                .add(EntityType.RABBIT)
                .add(EntityType.CHICKEN)
                .add(EntityType.SILVERFISH)
                .add(NaturalistEntityTypes.SNAIL.get())
                .addOptional(alexsmobs("jerboa"))
                .addOptional(alexsmobs("rain_frog"));

        tag(NaturalistEntityTypeTags.TIGER_HOSTILES)
                .add(NaturalistEntityTypes.BOAR.get())
                .add(EntityType.PIG)
                .add(NaturalistEntityTypes.DEER.get())
                .add(NaturalistEntityTypes.ZEBRA.get())
                .add(NaturalistEntityTypes.SNAKE.get());

        tag(NaturalistEntityTypeTags.TURKEY_HOSTILES)
                .add(NaturalistEntityTypes.ANT.get());

        tag(NaturalistEntityTypeTags.VULTURE_HOSTILES)
                .add(EntityType.ZOMBIE)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.HUSK)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.ZOGLIN)
                .add(EntityType.ZOMBIE_HORSE);
    }

    private static ResourceLocation alexsmobs(String path) {
        return new ResourceLocation("alexsmobs", path);
    }

    private static TagKey<EntityType<?>> entityTypeTag(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(id));
    }
}
