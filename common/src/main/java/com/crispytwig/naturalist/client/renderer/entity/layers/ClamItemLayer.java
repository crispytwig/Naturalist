package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.animal.clam.ClamModel;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class ClamItemLayer extends RenderLayer<Clam, HierarchicalModel<Clam>> {
    private final ItemInHandRenderer itemInHandRenderer;
    private final Quaternionf scratchRotation = new Quaternionf();

    public ClamItemLayer(RenderLayerParent<Clam, HierarchicalModel<Clam>> parent, ItemInHandRenderer itemInHandRenderer) {
        super(parent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Clam clam, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack held = clam.getMainHandItem();
        if (held.isEmpty() || !(this.getParentModel() instanceof ClamModel model)) {
            return;
        }

        poseStack.pushPose();
        model.translateToItem(poseStack);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(0.8F, 0.8F, 0.8F);

        poseStack.translate(0.0F, 0.6F + Mth.sin((clam.tickCount + partialTick) * 0.1F) * 0.2F, 0.0F);

        poseStack.mulPose(poseStack.last().pose().getNormalizedRotation(this.scratchRotation).conjugate());
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        this.itemInHandRenderer.renderItem(clam, held, ItemDisplayContext.FIXED, false, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
