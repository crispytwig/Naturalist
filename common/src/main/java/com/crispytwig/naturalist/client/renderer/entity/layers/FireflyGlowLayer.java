package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class FireflyGlowLayer extends RenderLayer<NaturalistRenderState<Firefly>, NaturalistEntityModel<Firefly>> {
    private static final Identifier GLOW = Naturalist.location("textures/entity/firefly_glow.png");
    private static final Identifier GLOW_E = Naturalist.location("textures/entity/firefly_glow_e.png");
    private static final int TOTAL_FRAMES = 30;
    private static final int TICKS_PER_FRAME = 1;

    public FireflyGlowLayer(RenderLayerParent<NaturalistRenderState<Firefly>, NaturalistEntityModel<Firefly>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Firefly> state, float yRot, float xRot) {
        Firefly entity = state.entity;
        int frame;
        if (entity.isGlowing()) {
            frame = Math.min((entity.tickCount - entity.getGlowStartTick()) / TICKS_PER_FRAME, TOTAL_FRAMES - 1);
        } else {
            frame = 0;
        }

        NaturalistEntityModel<Firefly> model = this.getParentModel();
        submitAnimated(submitNodeCollector, poseStack, model, state, RenderTypes.entityCutout(GLOW), lightCoords, frame);
        if (entity.isGlowing()) {
            submitAnimated(submitNodeCollector, poseStack, model, state, RenderTypes.entityTranslucentEmissive(GLOW_E), lightCoords, frame);
        }
    }

    private static void submitAnimated(SubmitNodeCollector submitNodeCollector, PoseStack poseStack, NaturalistEntityModel<Firefly> model,
                                       NaturalistRenderState<Firefly> state, RenderType renderType, int lightCoords, int frame) {
        submitNodeCollector.order(1).submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            PoseStack modelPoseStack = new PoseStack();
            modelPoseStack.last().set(pose);
            model.setupAnim(state);
            model.renderToBuffer(modelPoseStack, new AnimatedUVVertexConsumer(buffer, TOTAL_FRAMES, frame), lightCoords, OverlayTexture.NO_OVERLAY, -1);
        });
    }
}
