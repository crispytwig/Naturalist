package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.crispytwig.naturalist.client.model.animal.lizard.LizardTailModel;
import com.crispytwig.naturalist.world.entity.animal.lizard.LizardTail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class LizardTailRenderer extends MobRenderer<LizardTail, HierarchicalModel<LizardTail>> {
    public LizardTailRenderer(EntityRendererProvider.Context context) {
        super(context, new LizardTailModel(context.bakeLayer(LizardTailModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LizardTail entity) {
        return entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull LizardTail entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.translate(0, -0.3, 0);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
