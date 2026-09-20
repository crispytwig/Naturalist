package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.CatfishModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Catfish;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class CatfishRenderer extends NaturalistMobRenderer<Catfish> {
    public CatfishRenderer(EntityRendererProvider.Context context) {
        super(context, new CatfishModel(context.bakeLayer(CatfishModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Catfish> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        poseStack.rotateDegrees(Axis.ZP, -state.entity.swimTilt.getTilt(state.partialTick));
    }
}
