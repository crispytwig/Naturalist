package com.crispytwig.naturalist.forge;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.datagen.NaturalistDataGenerators;
import com.crispytwig.naturalist.forge.config.ForgeNaturalistConfig;
import com.crispytwig.naturalist.forge.platform.ForgeRegistrationProvider;
import com.crispytwig.naturalist.forge.registry.NaturalistBiomeModifiers;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.LegacyIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Mod(Naturalist.MOD_ID)
public class NaturalistForge {
    public NaturalistForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeRegistrationProvider.EVENT_BUS = modEventBus;
        Naturalist.bootstrap();
        NaturalistBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeNaturalistConfig.SPEC, "naturalist-server.toml");

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::createAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);
        modEventBus.addListener(this::addPackFinders);
        modEventBus.addListener(NaturalistDataGenerators::gatherData);

        MinecraftForge.EVENT_BUS.addListener(NaturalistForge::remapLegacyIds);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NaturalistForgeClient.init(modEventBus);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Naturalist.registerDispenserBehaviors();
            Naturalist.registerPotionMixes(PotionBrewing::addMix);
            ComposterBlock.COMPOSTABLES.put(NaturalistRegistry.SNAIL_SHELL.get(), 0.65F);
        });
    }

    private void createAttributes(EntityAttributeCreationEvent event) {
        Naturalist.createAttributes((type, builder) -> event.put(type, builder.build()));
    }

    private void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        Naturalist.registerSpawnPlacements(new Naturalist.SpawnPlacementRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, SpawnPlacements.Type placementType, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate) {
                event.register(type, placementType, heightmap, predicate, SpawnPlacementRegisterEvent.Operation.AND);
            }
        });
    }

    private static void remapLegacyIds(MissingMappingsEvent event) {
        remap(event, ForgeRegistries.Keys.BLOCKS, LegacyIds.BLOCKS, ForgeRegistries.BLOCKS);
        remap(event, ForgeRegistries.Keys.ITEMS, LegacyIds.ITEMS, ForgeRegistries.ITEMS);
    }

    private static <T> void remap(MissingMappingsEvent event, net.minecraft.resources.ResourceKey<? extends net.minecraft.core.Registry<T>> key,
                                  Map<String, String> renames, net.minecraftforge.registries.IForgeRegistry<T> registry) {
        for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(key, Naturalist.MOD_ID)) {
            String target = renames.get(mapping.getKey().toString());
            if (target != null) {
                T value = registry.getValue(new ResourceLocation(target));
                if (value != null) {
                    mapping.remap(value);
                }
            }
        }
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) {
            return;
        }
        Path resourcePath = ModList.get().getModFileById(Naturalist.MOD_ID).getFile().findResource("resourcepacks/custom_spawn_eggs");
        if (!Files.exists(resourcePath)) {
            return;
        }
        event.addRepositorySource(consumer -> {
            Pack pack = Pack.readMetaAndCreate(
                    "naturalist:custom_spawn_eggs",
                    Component.literal("Naturalist Spawn Eggs"),
                    false,
                    id -> new PathPackResources(id, resourcePath, true),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );
            if (pack != null) {
                consumer.accept(pack);
            }
        });
    }
}
