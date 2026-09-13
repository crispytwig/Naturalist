package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.lizard.LizardTailModel;
import com.crispytwig.naturalist.world.entity.animal.lizard.LizardTail;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class LizardTailRenderer extends NaturalistMobRenderer<LizardTail> {
    public LizardTailRenderer(EntityRendererProvider.Context context) {
        super(context, new LizardTailModel(context.bakeLayer(LizardTailModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void submit(NaturalistRenderState<LizardTail> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, -0.3F, 0.0F);
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.popPose();
    }
}
