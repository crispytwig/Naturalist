package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.CatfishModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Catfish;
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
public class CatfishRenderer extends MobRenderer<Catfish, HierarchicalModel<Catfish>> {
    public CatfishRenderer(EntityRendererProvider.Context context) {
        super(context, new CatfishModel(context.bakeLayer(CatfishModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Catfish entity) {
        return entity.getVariantTexture();
    }

    @Override
    protected void setupRotations(@NotNull Catfish entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float nativeScale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, nativeScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.swimTilt.getTilt(partialTick)));
    }
}
