package com.crispytwig.naturalist.fabric.client;

import com.crispytwig.naturalist.NaturalistClient;
import com.crispytwig.naturalist.NaturalistClientConfig;
import com.crispytwig.naturalist.fabric.config.FabricNaturalistClientConfig;
import com.crispytwig.naturalist.client.particle.CaptureNetSwingParticle;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;

public class NaturalistFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricNaturalistClientConfig.load();
        NaturalistClientConfig.setGlowGoopTooltip(FabricNaturalistClientConfig::isGlowGoopTooltipEnabled);

        NaturalistClient.registerLayerDefinitions((location, definition) ->
                ModelLayerRegistry.registerModelLayer(location, definition::get));
        NaturalistClient.registerRenderers(EntityRendererRegistry::register);
        NaturalistClient.registerBlockEntityRenderers(BlockEntityRenderers::register);

        NaturalistClient.registerMenuScreens(MenuScreens::register);

        NaturalistClient.registerConditionalItemModelProperties(ConditionalItemModelProperties.ID_MAPPER::put);

        ParticleProviderRegistry.getInstance().register(NaturalistParticleTypes.CAPTURE_NET_SWING.get(), CaptureNetSwingParticle.Provider::new);

    }
}
