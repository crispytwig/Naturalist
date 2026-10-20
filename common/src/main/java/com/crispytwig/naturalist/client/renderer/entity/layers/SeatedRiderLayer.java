package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.SeatedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SeatedRiderLayer<T extends Mob> extends RenderLayer<T, HierarchicalModel<T>> {
    public SeatedRiderLayer(RenderLayerParent<T, HierarchicalModel<T>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity.getFirstPassenger() instanceof Player player) || !(this.getParentModel() instanceof SeatedModel seatedModel)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (player == minecraft.player && minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        EntityRenderer<? super Player> renderer = minecraft.getEntityRenderDispatcher().getRenderer(player);

        poseStack.pushPose();
        seatedModel.translateToSeat(poseStack);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-seatedModel.seatZRot() * 0.1F * Mth.RAD_TO_DEG));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot) - 180.0F));
        poseStack.translate(0.0F, -seatedModel.seatHeight(), 0.0F);
        renderer.render(player, 0.0F, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
