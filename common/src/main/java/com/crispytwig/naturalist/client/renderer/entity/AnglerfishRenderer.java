package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.AnglerfishModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.LightCoordsUtil;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class AnglerfishRenderer extends NaturalistMobRenderer<Anglerfish> {
    public AnglerfishRenderer(EntityRendererProvider.Context context) {
        super(context, new AnglerfishModel(context.bakeLayer(AnglerfishModel.LAYER_LOCATION)), 0.0F);
    }

    @Override
    public void extractRenderState(Anglerfish entity, NaturalistRenderState<Anglerfish> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        if (entity.isGlowing()) {
            state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
        }
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Anglerfish> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        poseStack.rotateDegrees(Axis.ZP, -state.entity.swimTilt.getTilt(state.partialTick));
    }
}
