package com.crispytwig.naturalist.forge;

import com.crispytwig.naturalist.NaturalistClient;
import com.crispytwig.naturalist.NaturalistClientConfig;
import com.crispytwig.naturalist.client.model.item.VariantAwareItemModel;
import com.crispytwig.naturalist.client.model.item.VariantItemModels;
import com.crispytwig.naturalist.client.particle.CaptureNetSwingParticle;
import com.crispytwig.naturalist.forge.config.ForgeNaturalistClientConfig;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

public class NaturalistForgeClient {
    public static void init(IEventBus modEventBus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeNaturalistClientConfig.SPEC, "naturalist-client.toml");
        NaturalistClientConfig.setGlowGoopTooltip(ForgeNaturalistClientConfig::isGlowGoopTooltipEnabled);

        modEventBus.addListener(NaturalistForgeClient::registerRenderers);
        modEventBus.addListener(NaturalistForgeClient::registerLayerDefinitions);
        modEventBus.addListener(NaturalistForgeClient::registerExtraModels);
        modEventBus.addListener(NaturalistForgeClient::wrapVariantItemModels);
        modEventBus.addListener(NaturalistForgeClient::registerParticleProviders);
        modEventBus.addListener(NaturalistForgeClient::clientSetup);
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

    private static void registerExtraModels(ModelEvent.RegisterAdditional event) {
        for (ResourceLocation model : VariantItemModels.scanExtraModels(Minecraft.getInstance().getResourceManager())) {
            event.register(model);
        }
    }

    private static void wrapVariantItemModels(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<ResourceLocation, NaturalistBucketItem> entry : VariantItemModels.collectVariantItems().entrySet()) {
            event.getModels().computeIfPresent(new ModelResourceLocation(entry.getKey(), "inventory"), (id, model) -> new VariantAwareItemModel(model, entry.getValue(),
                    modelId -> Minecraft.getInstance().getModelManager().getModel(modelId)));
        }
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            NaturalistClient.registerItemProperties();
            NaturalistClient.registerMenuScreens(MenuScreens::register);
        });
    }
}
