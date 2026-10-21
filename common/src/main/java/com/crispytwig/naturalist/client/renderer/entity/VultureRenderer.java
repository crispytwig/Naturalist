package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureBabyModel;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureModel;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VultureRenderer extends NaturalistMobRenderer<Vulture> {
    public VultureRenderer(EntityRendererProvider.Context context) {
        super(context, new VultureModel(context.bakeLayer(VultureModel.LAYER_LOCATION)), new VultureBabyModel(context.bakeLayer(VultureBabyModel.LAYER_LOCATION)), 0.65F, 0.3F);
        this.addLayer(new VultureHeldItemLayer(this, context.getItemInHandRenderer()));
    }

    private static class VultureHeldItemLayer extends RenderLayer<Vulture, HierarchicalModel<Vulture>> {
        private final ItemInHandRenderer itemInHandRenderer;

        VultureHeldItemLayer(VultureRenderer parent, ItemInHandRenderer itemInHandRenderer) {
            super(parent);
            this.itemInHandRenderer = itemInHandRenderer;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Vulture entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack stack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack.isEmpty() || !(this.getParentModel() instanceof VultureModel vultureModel)) {
                return;
            }
            poseStack.pushPose();
            vultureModel.translateToHeldItem(poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            this.itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }
}
