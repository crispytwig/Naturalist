package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.rhino.RhinoModel;
import com.crispytwig.naturalist.world.entity.animal.rhino.Rhino;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class RhinoRenderer extends NaturalistMobRenderer<Rhino> {
    public RhinoRenderer(EntityRendererProvider.Context context) {
        super(context, new RhinoModel(context.bakeLayer(RhinoModel.LAYER_LOCATION)), 1.1F);
    }

    @Override
    protected void scale(NaturalistRenderState<Rhino> state, PoseStack poseStack) {
        float scale = state.isBaby ? 0.5F : 0.9F;
        poseStack.scale(scale, scale, scale);
    }
}
