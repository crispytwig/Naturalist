package com.crispytwig.naturalist.neoforge;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.neoforge.config.NeoForgeNaturalistConfig;
import com.crispytwig.naturalist.neoforge.platform.NeoForgeRegistrationProvider;
import com.crispytwig.naturalist.neoforge.registry.NaturalistBiomeModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import com.crispytwig.naturalist.datagen.NaturalistDataGenerators;

@Mod(Naturalist.MOD_ID)
public class NaturalistNeoForge {
    public NaturalistNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistrationProvider.EVENT_BUS = modEventBus;
        Naturalist.bootstrap();
        NaturalistBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, NeoForgeNaturalistConfig.SPEC, "naturalist-server.toml");

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::createAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);
        modEventBus.addListener(this::addPackFinders);
        modEventBus.addListener(NaturalistDataGenerators::gatherClientData);
        modEventBus.addListener(NaturalistDataGenerators::gatherServerData);

        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            NaturalistNeoForgeClient.init(modEventBus, modContainer);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Naturalist::registerDispenserBehaviors);
    }

    private void createAttributes(EntityAttributeCreationEvent event) {
        Naturalist.createAttributes((type, builder) -> event.put(type, builder.build()));
    }

    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        Naturalist.registerSpawnPlacements(new Naturalist.SpawnPlacementRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, SpawnPlacementType placementType, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate) {
                event.register(type, placementType, heightmap, predicate, RegisterSpawnPlacementsEvent.Operation.AND);
            }
        });
    }

    private void addPackFinders(AddPackFindersEvent event) {
        event.addPackFinders(
                Naturalist.location("resourcepacks/custom_spawn_eggs"),
                PackType.CLIENT_RESOURCES,
                Component.literal("Naturalist 1.21.5+ Spawn Eggs"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );
    }
}
