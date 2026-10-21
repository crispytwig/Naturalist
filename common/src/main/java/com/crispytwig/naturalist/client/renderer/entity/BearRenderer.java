package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.bear.BearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BearModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BearRenderer extends NaturalistMobRenderer<Bear> {
    public BearRenderer(EntityRendererProvider.Context context) {
        super(context, new BearModel(context.bakeLayer(BearModel.LAYER_LOCATION)), new BearBabyModel(context.bakeLayer(BearBabyModel.LAYER_LOCATION)), 0.9F);
        this.addLayer(new BearHeldItemLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new DyeLayer<>(this, "bear"));
    }

    private static class BearHeldItemLayer extends RenderLayer<Bear, HierarchicalModel<Bear>> {
        private final ItemInHandRenderer itemInHandRenderer;

        BearHeldItemLayer(BearRenderer parent, ItemInHandRenderer itemInHandRenderer) {
            super(parent);
            this.itemInHandRenderer = itemInHandRenderer;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Bear entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getEatCounter() < 8) {
                return;
            }
            ItemStack stack = entity.getMainHandItem();
            if (stack.isEmpty() || !(this.getParentModel() instanceof BearModel bearModel)) {
                return;
            }
            poseStack.pushPose();
            bearModel.translateToRightHand(poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-22.5F));
            poseStack.translate(1 / 16F, -8 / 16F, 2 / 16F);
            this.itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }
}
