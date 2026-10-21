package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.crispytwig.naturalist.client.model.animal.rhino.RhinoModel;
import com.crispytwig.naturalist.world.entity.animal.rhino.Rhino;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RhinoRenderer extends MobRenderer<Rhino, HierarchicalModel<Rhino>> {
    public RhinoRenderer(EntityRendererProvider.Context context) {
        super(context, new RhinoModel(context.bakeLayer(RhinoModel.LAYER_LOCATION)), 1.1F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Rhino entity) {
        return entity.getVariantTexture();
    }

    @Override
    protected void scale(@NotNull Rhino entity, @NotNull PoseStack poseStack, float partialTick) {
        float scale = entity.isBaby() ? 0.5F : 0.9F;
        poseStack.scale(scale, scale, scale);
    }
}
