package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.RayModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class RayRenderer extends MobRenderer<Ray, HierarchicalModel<Ray>> {
    public RayRenderer(EntityRendererProvider.Context context) {
        super(context, new RayModel(context.bakeLayer(RayModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Ray entity) {
        return entity.getVariantTexture();
    }

    @Override
    protected void setupRotations(@NotNull Ray entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float nativeScale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, nativeScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.swimTilt.getTilt(partialTick)));
    }
}
