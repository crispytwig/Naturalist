package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureBabyModel;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureModel;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class VultureRenderer extends NaturalistMobRenderer<Vulture> {
    public VultureRenderer(EntityRendererProvider.Context context) {
        super(context, new VultureModel(context.bakeLayer(VultureModel.LAYER_LOCATION)), new VultureBabyModel(context.bakeLayer(VultureBabyModel.LAYER_LOCATION)), 0.65F, 0.3F);
        this.addLayer(new VultureHeldItemLayer(this));
    }

    @Override
    public void extractRenderState(Vulture entity, NaturalistRenderState<Vulture> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        this.itemModelResolver.updateForLiving(state.heldItem, entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.GROUND, entity);
    }

    private static class VultureHeldItemLayer extends RenderLayer<NaturalistRenderState<Vulture>, NaturalistEntityModel<Vulture>> {
        VultureHeldItemLayer(RenderLayerParent<NaturalistRenderState<Vulture>, NaturalistEntityModel<Vulture>> parent) {
            super(parent);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Vulture> state, float yRot, float xRot) {
            if (state.heldItem.isEmpty() || !(this.getParentModel() instanceof VultureModel vultureModel)) {
                return;
            }
            poseStack.pushPose();
            vultureModel.translateToHeldItem(poseStack);
            poseStack.rotateDegrees(Axis.XP, -90.0F);
            state.heldItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
