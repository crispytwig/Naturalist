package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.BassModel;
import com.crispytwig.naturalist.client.model.animal.fish.LargeBassModel;
import com.crispytwig.naturalist.client.model.animal.fish.MediumBassModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class BassRenderer extends NaturalistMobRenderer<Bass> {
    private final NaturalistEntityModel<Bass> normalModel;
    private final NaturalistEntityModel<Bass> mediumModel;
    private final NaturalistEntityModel<Bass> largeModel;

    public BassRenderer(EntityRendererProvider.Context context) {
        super(context, new BassModel(context.bakeLayer(BassModel.LAYER_LOCATION)), 0.0F);
        this.normalModel = this.model;
        this.mediumModel = new MediumBassModel(context.bakeLayer(MediumBassModel.LAYER_LOCATION));
        this.largeModel = new LargeBassModel(context.bakeLayer(LargeBassModel.LAYER_LOCATION));
    }

    @Override
    public void submit(NaturalistRenderState<Bass> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.entity.isLargeVariant()) {
            this.model = this.largeModel;
        } else if (state.entity.isMediumVariant()) {
            this.model = this.mediumModel;
        } else {
            this.model = this.normalModel;
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Bass> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        poseStack.rotateDegrees(Axis.ZP, -state.entity.swimTilt.getTilt(state.partialTick));
    }
}
