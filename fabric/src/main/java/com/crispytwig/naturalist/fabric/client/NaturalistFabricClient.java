package com.crispytwig.naturalist.fabric.client;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistClient;
import com.crispytwig.naturalist.NaturalistClientConfig;
import com.crispytwig.naturalist.fabric.config.FabricNaturalistClientConfig;
import com.crispytwig.naturalist.client.model.item.VariantAwareItemModel;
import com.crispytwig.naturalist.client.model.item.VariantItemModels;
import com.crispytwig.naturalist.client.particle.CaptureNetSwingParticle;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.resources.model.BakedModel;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NaturalistFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricNaturalistClientConfig.load();
        NaturalistClientConfig.setGlowGoopTooltip(FabricNaturalistClientConfig::isGlowGoopTooltipEnabled);

        NaturalistClient.registerLayerDefinitions((location, definition) ->
                EntityModelLayerRegistry.registerModelLayer(location, definition::get));
        NaturalistClient.registerRenderers(EntityRendererRegistry::register);
        NaturalistClient.registerBlockEntityRenderers(BlockEntityRendererRegistry::register);

        NaturalistClient.registerItemProperties();
        NaturalistClient.registerMenuScreens(MenuScreens::register);

        registerVariantItemModels();
        registerCustomSpawnEggsPack();

        ParticleFactoryRegistry.getInstance().register(NaturalistParticleTypes.CAPTURE_NET_SWING.get(), CaptureNetSwingParticle.Provider::new);

        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.ORANGE_STARFISH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.PURPLE_STARFISH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.BLUE_STARFISH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.RED_STARFISH.get(), RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.SNAIL_EGGS.get(), RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.AZURE_FROGLASS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.VERDANT_FROGLASS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.CRIMSON_FROGLASS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.AZURE_FROGLASS_PANE.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.VERDANT_FROGLASS_PANE.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get(), RenderType.translucent());
    }

    private static void registerCustomSpawnEggsPack() {
        FabricLoader.getInstance().getModContainer(Naturalist.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(Naturalist.location("custom_spawn_eggs"), container,
                        Component.literal("Naturalist Spawn Eggs"), ResourcePackActivationType.NORMAL));
    }

    private static void registerVariantItemModels() {
        Map<ResourceLocation, NaturalistBucketItem> variantItems = VariantItemModels.collectVariantItems();
        PreparableModelLoadingPlugin.register(
                (resourceManager, executor) -> CompletableFuture.supplyAsync(() -> VariantItemModels.scanExtraModels(resourceManager), executor),
                (extraModels, context) -> {
                    context.addModels(extraModels);
                    context.modifyModelAfterBake().register((model, ctx) -> {
                        if (model != null && ctx.id() instanceof ModelResourceLocation topLevelId && "inventory".equals(topLevelId.getVariant())) {
                            NaturalistBucketItem item = variantItems.get(new ResourceLocation(topLevelId.getNamespace(), topLevelId.getPath()));
                            if (item != null) {
                                return new VariantAwareItemModel(model, item,
                                        id -> Minecraft.getInstance().getModelManager().getModel(id));
                            }
                        }
                        return model;
                    });
                });
    }
}
