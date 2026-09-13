package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.whale.WhaleBabyModel;
import com.crispytwig.naturalist.client.model.animal.whale.WhaleModel;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class WhaleRenderer extends NaturalistMobRenderer<Whale> {
    public WhaleRenderer(EntityRendererProvider.Context context) {
        super(context, new WhaleModel(context.bakeLayer(WhaleModel.LAYER_LOCATION)), new WhaleBabyModel(context.bakeLayer(WhaleBabyModel.LAYER_LOCATION)), 0.0F, 0.0F);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Whale> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, state.entity.getRenderYaw(state.partialTick), entityScale);
    }
}
