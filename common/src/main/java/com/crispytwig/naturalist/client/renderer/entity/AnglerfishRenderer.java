package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.crispytwig.naturalist.client.model.animal.fish.AnglerfishModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class AnglerfishRenderer extends MobRenderer<Anglerfish, HierarchicalModel<Anglerfish>> {
    public AnglerfishRenderer(EntityRendererProvider.Context context) {
        super(context, new AnglerfishModel(context.bakeLayer(AnglerfishModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Anglerfish entity) {
        return entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull Anglerfish entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isGlowing()) {
            packedLight = LightTexture.FULL_BRIGHT;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void setupRotations(@NotNull Anglerfish entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float nativeScale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, nativeScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.swimTilt.getTilt(partialTick)));
    }
}
