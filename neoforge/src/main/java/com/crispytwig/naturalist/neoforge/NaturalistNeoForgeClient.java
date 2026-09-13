package com.crispytwig.naturalist.neoforge;

import com.crispytwig.naturalist.NaturalistClient;
import com.crispytwig.naturalist.NaturalistClientConfig;
import com.crispytwig.naturalist.neoforge.config.NeoForgeNaturalistClientConfig;
import com.crispytwig.naturalist.client.particle.CaptureNetSwingParticle;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NaturalistNeoForgeClient {
    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, NeoForgeNaturalistClientConfig.SPEC, "naturalist-client.toml");
        NaturalistClientConfig.setGlowGoopTooltip(NeoForgeNaturalistClientConfig::isGlowGoopTooltipEnabled);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(NaturalistNeoForgeClient::registerRenderers);
        modEventBus.addListener(NaturalistNeoForgeClient::registerLayerDefinitions);
        modEventBus.addListener(NaturalistNeoForgeClient::registerMenuScreens);
        modEventBus.addListener(NaturalistNeoForgeClient::registerConditionalItemModelProperties);
        modEventBus.addListener(NaturalistNeoForgeClient::registerParticleProviders);
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

    private static void registerConditionalItemModelProperties(RegisterConditionalItemModelPropertyEvent event) {
        NaturalistClient.registerConditionalItemModelProperties(event::register);
    }
}
