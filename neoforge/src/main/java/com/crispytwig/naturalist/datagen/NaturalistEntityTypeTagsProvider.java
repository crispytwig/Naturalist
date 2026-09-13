package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NaturalistEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public NaturalistEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(NaturalistEntityTypeTags.ALLIGATOR_HOSTILES)
                .add(NaturalistEntityTypes.DEER.get())
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(EntityType.FROG)
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "shoebill"));

        tag(NaturalistEntityTypeTags.BEAR_HOSTILES)
                .add(EntityType.SALMON)
                .add(NaturalistEntityTypes.DEER.get())
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "moose"));

        tag(NaturalistEntityTypeTags.BOAR_HOSTILES)
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(NaturalistEntityTypes.SNAIL.get());

        tag(NaturalistEntityTypeTags.CATFISH_HOSTILES)
                .add(EntityType.TROPICAL_FISH)
                .add(EntityType.COD)
                .add(EntityType.TADPOLE)
                .add(NaturalistEntityTypes.BASS.get());

        tag(NaturalistEntityTypeTags.ANGLERFISH_HOSTILES)
                .add(EntityType.TROPICAL_FISH)
                .add(EntityType.COD);

        tag(NaturalistEntityTypeTags.DEER_PREDATORS)
                .add(NaturalistEntityTypes.BEAR.get())
                .add(NaturalistEntityTypes.BLACK_BEAR.get())
                .add(NaturalistEntityTypes.TIGER.get())
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "grizzly_bear"))
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "crocodile"));

        tag(NaturalistEntityTypeTags.LION_HOSTILES)
                .add(NaturalistEntityTypes.RHINO.get())
                .add(NaturalistEntityTypes.ZEBRA.get())
                .add(NaturalistEntityTypes.BOAR.get())
                .add(EntityType.HORSE);

        tag(NaturalistEntityTypeTags.TIGER_HOSTILES)
                .add(NaturalistEntityTypes.BOAR.get())
                .add(EntityType.PIG)
                .add(NaturalistEntityTypes.DEER.get())
                .add(NaturalistEntityTypes.ZEBRA.get())
                .add(NaturalistEntityTypes.SNAKE.get());

        tag(NaturalistEntityTypeTags.KOMODO_DRAGON_HOSTILES)
                .add(EntityType.CHICKEN)
                .add(EntityType.RABBIT)
                .add(NaturalistEntityTypes.LIZARD.get())
                .add(NaturalistEntityTypes.SNAKE.get())
                .add(NaturalistEntityTypes.BOAR.get());

        tag(NaturalistEntityTypeTags.SCORPION_HOSTILES)
                .add(NaturalistEntityTypes.LIZARD.get());

        tag(NaturalistEntityTypeTags.GREAT_WHITE_SHARK_HOSTILES)
                .add(EntityType.COD)
                .add(EntityType.SALMON)
                .add(EntityType.TROPICAL_FISH)
                .add(NaturalistEntityTypes.BASS.get())
                .add(NaturalistEntityTypes.CATFISH.get())
                .add(NaturalistEntityTypes.PIRANHA.get())
                .add(NaturalistEntityTypes.BLOBFISH.get())
                .add(NaturalistEntityTypes.ANGLERFISH.get());

        tag(NaturalistEntityTypeTags.TURKEY_HOSTILES)
                .add(NaturalistEntityTypes.ANT.get());

        tag(NaturalistEntityTypeTags.SAFE_EGG_WALKERS)
                .add(NaturalistEntityTypes.BUTTERFLY.get())
                .add(NaturalistEntityTypes.BIRD.get())
                .add(NaturalistEntityTypes.DRAGONFLY.get())
                .add(NaturalistEntityTypes.FIREFLY.get())
                .add(EntityType.BAT)
                .addOptional(Identifier.fromNamespaceAndPath("alexsmobs", "shoebill"));

        tag(NaturalistEntityTypeTags.SNAKE_HOSTILES)
                .add(EntityType.RABBIT)
                .add(EntityType.CHICKEN)
                .add(EntityType.SILVERFISH)
                .add(NaturalistEntityTypes.SNAIL.get());

        tag(NaturalistEntityTypeTags.VULTURE_HOSTILES)
                .add(EntityType.ZOMBIE)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.HUSK)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.ZOGLIN)
                .add(EntityType.ZOMBIE_HORSE);
    }
}
