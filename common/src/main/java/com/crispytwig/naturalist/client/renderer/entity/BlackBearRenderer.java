package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.bear.BlackBearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BlackBearModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

public class BlackBearRenderer extends NaturalistMobRenderer<BlackBear> {
    public BlackBearRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackBearModel(context.bakeLayer(BlackBearModel.LAYER_LOCATION)), new BlackBearBabyModel(context.bakeLayer(BlackBearBabyModel.LAYER_LOCATION)), 0.9F);
        this.addLayer(new BlackBearHeldItemLayer(this));
        this.addLayer(new DyeLayer<>(this, "black_bear"));
    }

    @Override
    public void extractRenderState(BlackBear entity, NaturalistRenderState<BlackBear> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        ItemStack stack = entity.getMainHandItem();
        if (entity.getEatCounter() >= 8 && !stack.is(Items.SWEET_BERRIES) && !stack.is(Items.HONEYCOMB)) {
            this.itemModelResolver.updateForLiving(state.heldItem, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        } else {
            state.heldItem.clear();
        }
    }

    private static class BlackBearHeldItemLayer extends RenderLayer<NaturalistRenderState<BlackBear>, NaturalistEntityModel<BlackBear>> {
        BlackBearHeldItemLayer(RenderLayerParent<NaturalistRenderState<BlackBear>, NaturalistEntityModel<BlackBear>> parent) {
            super(parent);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<BlackBear> state, float yRot, float xRot) {
            if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof BlackBearModel bearModel)) {
                return;
            }
            poseStack.pushPose();
            bearModel.translateToRightArm(poseStack);
            poseStack.rotateDegrees(Axis.XP, -22.5F);
            poseStack.translate(0.0F, -7 / 16F, 0.0F);
            state.heldItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
