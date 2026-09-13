package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishGrayModel;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishPinkModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class BlobfishRenderer extends NaturalistMobRenderer<Blobfish> {
    private static final Identifier GRAY = Naturalist.location("textures/entity/blobfish/gray.png");
    private static final Identifier PINK = Naturalist.location("textures/entity/blobfish/pink.png");

    private final NaturalistEntityModel<Blobfish> pinkModel;
    private final NaturalistEntityModel<Blobfish> grayModel;

    public BlobfishRenderer(EntityRendererProvider.Context context) {
        super(context, new BlobfishPinkModel(context.bakeLayer(BlobfishPinkModel.LAYER_LOCATION)), 0.0F);
        this.pinkModel = this.model;
        this.grayModel = new BlobfishGrayModel(context.bakeLayer(BlobfishGrayModel.LAYER_LOCATION));
    }

    @Override
    protected Identifier getTexture(Blobfish entity) {
        if (entity.hasNonDefaultVariant()) {
            return entity.getVariantTexture();
        }
        return entity.isGray() ? GRAY : PINK;
    }

    @Override
    public void submit(NaturalistRenderState<Blobfish> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.entity.isGray() ? this.grayModel : this.pinkModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Blobfish> state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.entity.swimTilt.getTilt(state.partialTick)));
    }
}
