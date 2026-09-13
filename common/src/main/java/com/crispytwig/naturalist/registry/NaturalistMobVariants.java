package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class NaturalistMobVariants {
    private static final Set<String> NON_MOB_ENTITIES = Set.of("duck_egg", "dirt_trail", "carried_food");
    private static final Set<String> REGISTERED_MOBS = new HashSet<>();

    public static final ResourceKey<Registry<MobVariant>> HEDGEHOG_VARIANT = registryFor("hedgehog");
    public static final ResourceKey<Registry<MobVariant>> RAT_VARIANT = registryFor("rat");
    public static final ResourceKey<Registry<MobVariant>> TIGER_VARIANT = registryFor("tiger");
    public static final ResourceKey<Registry<MobVariant>> TORTOISE_VARIANT = registryFor("tortoise");
    public static final ResourceKey<Registry<MobVariant>> ANGLERFISH_VARIANT = registryFor("anglerfish");
    public static final ResourceKey<Registry<MobVariant>> BUTTERFLY_VARIANT = registryFor("butterfly");
    public static final ResourceKey<Registry<MobVariant>> BIRD_VARIANT = registryFor("bird");

    public static final ResourceKey<MobVariant> HEDGEHOG_BROWN = createKey(HEDGEHOG_VARIANT, "brown");
    public static final ResourceKey<MobVariant> RAT_BLACK = createKey(RAT_VARIANT, "black");
    public static final ResourceKey<MobVariant> TIGER_BLACK_PANTHER = createKey(TIGER_VARIANT, "black_panther");
    public static final ResourceKey<MobVariant> TORTOISE_BROWN = createKey(TORTOISE_VARIANT, "brown");
    public static final ResourceKey<MobVariant> ANGLERFISH_RED = createKey(ANGLERFISH_VARIANT, "red");
    public static final ResourceKey<MobVariant> ANGLERFISH_GLOW = createKey(ANGLERFISH_VARIANT, "glow");
    public static final ResourceKey<MobVariant> BUTTERFLY_MONARCH = createKey(BUTTERFLY_VARIANT, "monarch");
    public static final ResourceKey<MobVariant> BIRD_AMERICAN_ROBIN = createKey(BIRD_VARIANT, "american_robin");

    private NaturalistMobVariants() {
    }

    public static void bootstrap() {
        for (String name : NaturalistEntityTypes.ENTITY_TYPES.getRegisteredNames()) {
            if (NON_MOB_ENTITIES.contains(name)) {
                continue;
            }
            REGISTERED_MOBS.add(name);
            Services.REGISTRY.registerSyncedDataPackRegistry(registryFor(name), MobVariant.DIRECT_CODEC);
        }
    }

    public static ResourceKey<Registry<MobVariant>> registryFor(String mobName) {
        return ResourceKey.createRegistryKey(Naturalist.location(mobName + "_variant"));
    }

    public static ResourceKey<Registry<MobVariant>> registryFor(EntityType<?> type) {
        return registryFor(EntityType.getKey(type).getPath());
    }

    public static Optional<ResourceKey<Registry<MobVariant>>> findRegistry(EntityType<?> type) {
        Identifier id = EntityType.getKey(type);
        if (!id.getNamespace().equals(Naturalist.MOD_ID) || !REGISTERED_MOBS.contains(id.getPath())) {
            return Optional.empty();
        }
        return Optional.of(registryFor(id.getPath()));
    }

    public static ResourceKey<MobVariant> defaultFor(EntityType<?> type) {
        String name = EntityType.getKey(type).getPath();
        return createKey(registryFor(name), name);
    }

    public static ResourceKey<MobVariant> createKey(ResourceKey<Registry<MobVariant>> registry, String name) {
        return ResourceKey.create(registry, Naturalist.location(name));
    }
}
