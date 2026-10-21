package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.crispytwig.naturalist.client.model.animal.giantisopod.GiantIsopodModel;
import com.crispytwig.naturalist.world.entity.animal.giantisopod.GiantIsopod;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GiantIsopodRenderer extends MobRenderer<GiantIsopod, HierarchicalModel<GiantIsopod>> {
    public GiantIsopodRenderer(EntityRendererProvider.Context context) {
        super(context, new GiantIsopodModel(context.bakeLayer(GiantIsopodModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GiantIsopod entity) {
        return entity.getVariantTexture();
    }

    @Override
    protected void scale(@NotNull GiantIsopod entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
