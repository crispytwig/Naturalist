package com.crispytwig.naturalist.tags;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class NaturalistEntityTypeTags {
    public static final TagKey<EntityType<?>> SAFE_EGG_WALKERS = tag("safe_egg_walkers");
    public static final TagKey<EntityType<?>> OSTRICH_PREDATORS = tag("ostrich_predators");
    public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");
    public static final TagKey<EntityType<?>> SNAKE_HOSTILES = tag("snake_hostiles");
    public static final TagKey<EntityType<?>> DEER_PREDATORS = tag("deer_predators");
    public static final TagKey<EntityType<?>> LION_HOSTILES = tag("lion_hostiles");
    public static final TagKey<EntityType<?>> TIGER_HOSTILES = tag("tiger_hostiles");
    public static final TagKey<EntityType<?>> KOMODO_DRAGON_HOSTILES = tag("komodo_dragon_hostiles");
    public static final TagKey<EntityType<?>> SCORPION_HOSTILES = tag("scorpion_hostiles");
    public static final TagKey<EntityType<?>> GREAT_WHITE_SHARK_HOSTILES = tag("great_white_shark_hostiles");
    public static final TagKey<EntityType<?>> TURKEY_HOSTILES = tag("turkey_hostiles");
    public static final TagKey<EntityType<?>> VULTURE_HOSTILES = tag("vulture_hostiles");
    public static final TagKey<EntityType<?>> CATFISH_HOSTILES = tag("catfish_hostiles");
    public static final TagKey<EntityType<?>> ANGLERFISH_HOSTILES = tag("anglerfish_hostiles");
    public static final TagKey<EntityType<?>> PIRANHA_HOSTILES = tag("piranha_hostiles");
    public static final TagKey<EntityType<?>> ALLIGATOR_HOSTILES = tag("alligator_hostiles");
    public static final TagKey<EntityType<?>> BOAR_HOSTILES = tag("boar_hostiles");
    public static final TagKey<EntityType<?>> ANIMAL_CRATE_BLACKLISTED = tag("animal_crate_blacklist");

    private static @NotNull TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Naturalist.location(name));
    }
}
