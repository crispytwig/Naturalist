package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.RayModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class RayRenderer extends NaturalistMobRenderer<Ray> {
    public RayRenderer(EntityRendererProvider.Context context) {
        super(context, new RayModel(context.bakeLayer(RayModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Ray> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        poseStack.rotateDegrees(Axis.ZP, -state.entity.swimTilt.getTilt(state.partialTick));
    }
}
