package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class HedgehogGlintLayer extends RenderLayer<Hedgehog, HierarchicalModel<Hedgehog>> {
    public HedgehogGlintLayer(RenderLayerParent<Hedgehog, HierarchicalModel<Hedgehog>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Hedgehog entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.hasThrowEnchantments() || entity.isInvisible()) {
            return;
        }
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityGlintDirect()), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
