package com.crispytwig.naturalist.fabric;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.fabric.config.FabricNaturalistConfig;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.tags.NaturalistBiomeTags;
import com.crispytwig.naturalist.world.level.NaturalistSpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.List;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricNaturalistConfig.load();
        Naturalist.bootstrap();

        Naturalist.createAttributes((type, builder) -> FabricDefaultAttributeRegistry.register(type, builder.build()));

        Naturalist.registerSpawnPlacements(SpawnPlacements::register);

        Naturalist.registerDispenserBehaviors();

        Naturalist.registerPotionMixes(PotionBrewing::addMix);

        CompostingChanceRegistry.INSTANCE.add(NaturalistRegistry.SNAIL_SHELL.get(), 0.65F);

        registerBiomeSpawns();
        registerBiomeFeatures();
        removeVanillaSpawns();
    }

    private static void registerBiomeSpawns() {
        NaturalistSpawns.forEachSpawn((hasTag, blacklistTag, category, type, weight, min, max) -> {
            BiomeModifications.addSpawn(ctx -> !NaturalistConfig.isRemoved(type)
                    && ctx.hasTag(hasTag) && (blacklistTag == null || !ctx.hasTag(blacklistTag)), category, type, weight, min, max);
        });
    }

    private static void registerBiomeFeatures() {
        for (String name : List.of("ant_hill_small", "ant_hill_big")) {
            BiomeModifications.addFeature(ctx -> ctx.hasTag(NaturalistBiomeTags.HAS_ANT_HILL), GenerationStep.Decoration.SURFACE_STRUCTURES,
                    ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Naturalist.MOD_ID, name)));
        }
    }

    private static void removeVanillaSpawns() {
        List<EntityType<?>> farmAnimals = List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW);
        BiomeModifications.create(Naturalist.location("remove_vanilla_spawns"))
                .add(ModificationPhase.REMOVALS, BiomeSelectors.tag(BiomeTags.IS_SAVANNA).or(BiomeSelectors.tag(ConventionalBiomeTags.SWAMP)),
                        ctx -> farmAnimals.forEach(type -> ctx.getSpawnSettings().removeSpawnsOfEntityType(type)))
                .add(ModificationPhase.REMOVALS, BiomeSelectors.tag(BiomeTags.IS_FOREST),
                        ctx -> ctx.getSpawnSettings().removeSpawnsOfEntityType(EntityType.PIG));
    }
}
