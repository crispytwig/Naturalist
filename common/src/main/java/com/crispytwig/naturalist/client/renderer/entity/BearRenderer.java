package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.bear.BearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BearModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

public class BearRenderer extends NaturalistMobRenderer<Bear> {
    public BearRenderer(EntityRendererProvider.Context context) {
        super(context, new BearModel(context.bakeLayer(BearModel.LAYER_LOCATION)), new BearBabyModel(context.bakeLayer(BearBabyModel.LAYER_LOCATION)), 0.9F);
        this.addLayer(new BearHeldItemLayer(this));
        this.addLayer(new DyeLayer<>(this, "bear"));
    }

    @Override
    public void extractRenderState(Bear entity, NaturalistRenderState<Bear> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        if (entity.getEatCounter() >= 8) {
            this.itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        } else {
            state.heldItem.clear();
        }
    }

    private static class BearHeldItemLayer extends RenderLayer<NaturalistRenderState<Bear>, NaturalistEntityModel<Bear>> {
        BearHeldItemLayer(RenderLayerParent<NaturalistRenderState<Bear>, NaturalistEntityModel<Bear>> parent) {
            super(parent);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Bear> state, float yRot, float xRot) {
            if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof BearModel bearModel)) {
                return;
            }
            poseStack.pushPose();
            bearModel.translateToRightHand(poseStack);
            poseStack.rotateDegrees(Axis.XP, -22.5F);
            poseStack.translate(1 / 16F, -8 / 16F, 2 / 16F);
            state.heldItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
