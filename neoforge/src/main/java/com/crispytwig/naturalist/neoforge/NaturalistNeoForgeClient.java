package com.crispytwig.naturalist.neoforge;

import com.crispytwig.naturalist.NaturalistClient;
import com.crispytwig.naturalist.NaturalistClientConfig;
import com.crispytwig.naturalist.neoforge.config.NeoForgeNaturalistClientConfig;
import com.crispytwig.naturalist.client.model.item.VariantAwareItemModel;
import com.crispytwig.naturalist.client.model.item.VariantItemModels;
import com.crispytwig.naturalist.client.particle.CaptureNetSwingParticle;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.Map;

public class NaturalistNeoForgeClient {
    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, NeoForgeNaturalistClientConfig.SPEC, "naturalist-client.toml");
        NaturalistClientConfig.setGlowGoopTooltip(NeoForgeNaturalistClientConfig::isGlowGoopTooltipEnabled);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(NaturalistNeoForgeClient::registerRenderers);
        modEventBus.addListener(NaturalistNeoForgeClient::registerLayerDefinitions);
        modEventBus.addListener(NaturalistNeoForgeClient::registerMenuScreens);
        modEventBus.addListener(NaturalistNeoForgeClient::registerExtraModels);
        modEventBus.addListener(NaturalistNeoForgeClient::wrapVariantItemModels);
        modEventBus.addListener(NaturalistNeoForgeClient::registerParticleProviders);
        modEventBus.addListener(NaturalistNeoForgeClient::clientSetup);
    }

    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(NaturalistParticleTypes.CAPTURE_NET_SWING.get(), CaptureNetSwingParticle.Provider::new);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        NaturalistClient.registerLayerDefinitions(event::registerLayerDefinition);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        NaturalistClient.registerRenderers(event::registerEntityRenderer);
        NaturalistClient.registerBlockEntityRenderers(event::registerBlockEntityRenderer);
    }

    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        NaturalistClient.registerMenuScreens(event::register);
    }

    private static void registerExtraModels(ModelEvent.RegisterAdditional event) {
        for (ResourceLocation model : VariantItemModels.scanExtraModels(Minecraft.getInstance().getResourceManager())) {
            event.register(ModelResourceLocation.standalone(model));
        }
    }

    private static void wrapVariantItemModels(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<ResourceLocation, NaturalistBucketItem> entry : VariantItemModels.collectVariantItems().entrySet()) {
            event.getModels().computeIfPresent(ModelResourceLocation.inventory(entry.getKey()), (id, model) -> new VariantAwareItemModel(model, entry.getValue(),
                    modelId -> Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(modelId))));
        }
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(NaturalistClient::registerItemProperties);
    }
}
