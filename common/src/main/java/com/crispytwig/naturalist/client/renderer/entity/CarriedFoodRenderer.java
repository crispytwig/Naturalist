package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.world.entity.animal.ant.CarriedFoodEntity;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CarriedFoodRenderer extends ItemEntityRenderer {
    public CarriedFoodRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(ItemEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof CarriedFoodEntity food) {
            Ant ant = food.resolveAnt();
            if (ant != null) {
                double dx = Mth.lerp(partialTick, ant.xOld, ant.getX()) - Mth.lerp(partialTick, entity.xOld, entity.getX());
                double dy = Mth.lerp(partialTick, ant.yOld, ant.getY()) + ant.getBbHeight() + CarriedFoodEntity.BACK_GAP - Mth.lerp(partialTick, entity.yOld, entity.getY());
                double dz = Mth.lerp(partialTick, ant.zOld, ant.getZ()) - Mth.lerp(partialTick, entity.zOld, entity.getZ());
                poseStack.pushPose();
                poseStack.translate(dx, dy, dz);
                super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                poseStack.popPose();
                return;
            }
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
