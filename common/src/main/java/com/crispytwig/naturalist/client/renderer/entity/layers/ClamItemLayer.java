package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.animal.clam.ClamModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class ClamItemLayer extends RenderLayer<NaturalistRenderState<Clam>, NaturalistEntityModel<Clam>> {
    private final Quaternionf scratchRotation = new Quaternionf();

    public ClamItemLayer(RenderLayerParent<NaturalistRenderState<Clam>, NaturalistEntityModel<Clam>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Clam> state, float yRot, float xRot) {
        if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof ClamModel model)) {
            return;
        }

        poseStack.pushPose();
        model.translateToItem(poseStack);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(0.8F, 0.8F, 0.8F);

        poseStack.translate(0.0F, 0.6F + Mth.sin(state.ageInTicks * 0.1F) * 0.2F, 0.0F);

        poseStack.rotate(poseStack.last().pose().getNormalizedRotation(this.scratchRotation).conjugate());
        poseStack.rotate(Minecraft.getInstance().gameRenderer.gameRenderState().levelRenderState.cameraRenderState.orientation);
        poseStack.rotateDegrees(Axis.YP, 180.0F);

        state.heldItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
