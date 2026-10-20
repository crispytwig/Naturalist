package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.bear.BlackBearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BlackBearModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BlackBearRenderer extends NaturalistMobRenderer<BlackBear> {
    public BlackBearRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackBearModel(context.bakeLayer(BlackBearModel.LAYER_LOCATION)), new BlackBearBabyModel(context.bakeLayer(BlackBearBabyModel.LAYER_LOCATION)), 0.9F);
        this.addLayer(new BlackBearHeldItemLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new DyeLayer<>(this, "black_bear"));
    }

    private static class BlackBearHeldItemLayer extends RenderLayer<BlackBear, HierarchicalModel<BlackBear>> {
        private final ItemInHandRenderer itemInHandRenderer;

        BlackBearHeldItemLayer(BlackBearRenderer parent, ItemInHandRenderer itemInHandRenderer) {
            super(parent);
            this.itemInHandRenderer = itemInHandRenderer;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull BlackBear entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getEatCounter() < 8) {
                return;
            }
            ItemStack stack = entity.getMainHandItem();
            if (stack.isEmpty() || stack.is(Items.SWEET_BERRIES) || stack.is(Items.HONEYCOMB)) {
                return;
            }
            if (!(this.getParentModel() instanceof BlackBearModel bearModel)) {
                return;
            }
            poseStack.pushPose();
            bearModel.translateToRightArm(poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-22.5F));
            poseStack.translate(0.0F, -7 / 16F, 0.0F);
            this.itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }
}
