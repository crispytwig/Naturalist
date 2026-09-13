package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.animal.crab.CrabModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class CrabItemLayer extends RenderLayer<NaturalistRenderState<Crab>, NaturalistEntityModel<Crab>> {
    public CrabItemLayer(RenderLayerParent<NaturalistRenderState<Crab>, NaturalistEntityModel<Crab>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Crab> state, float yRot, float xRot) {
        if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof CrabModel model)) {
            return;
        }

        poseStack.pushPose();
        model.translateToItem(poseStack);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        state.heldItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
