package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.crispytwig.naturalist.client.model.animal.hippo.HippoBabyModel;
import com.crispytwig.naturalist.client.model.animal.hippo.HippoModel;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.BlockItem;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class HippoRenderer extends NaturalistMobRenderer<Hippo> {
    public HippoRenderer(EntityRendererProvider.Context context) {
        super(context, new HippoModel(context.bakeLayer(HippoModel.LAYER_LOCATION)), new HippoBabyModel(context.bakeLayer(HippoBabyModel.LAYER_LOCATION)), 1.1F);
        this.addLayer(new HippoJawBlockLayer(this));
    }

    private static class HippoJawBlockLayer extends RenderLayer<Hippo, HierarchicalModel<Hippo>> {
        HippoJawBlockLayer(HippoRenderer parent) {
            super(parent);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Hippo entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(entity.getMainHandItem().getItem() instanceof BlockItem blockItem)) {
                return;
            }
            if (!(this.getParentModel() instanceof HippoModel hippoModel)) {
                return;
            }
            poseStack.pushPose();
            hippoModel.translateToBotJaw(poseStack);
            poseStack.translate(-0.4D, 0.76D, -1.8D);
            poseStack.scale(0.675F, 0.675F, 0.675F);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockItem.getBlock().defaultBlockState(), poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
