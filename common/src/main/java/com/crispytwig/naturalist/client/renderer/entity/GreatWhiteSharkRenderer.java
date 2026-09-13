package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.greatwhiteshark.GreatWhiteSharkModel;
import com.crispytwig.naturalist.world.entity.animal.greatwhiteshark.GreatWhiteShark;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class GreatWhiteSharkRenderer extends NaturalistMobRenderer<GreatWhiteShark> {
    public GreatWhiteSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new GreatWhiteSharkModel(context.bakeLayer(GreatWhiteSharkModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<GreatWhiteShark> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, state.entity.getRenderYaw(state.partialTick), entityScale);
    }
}
