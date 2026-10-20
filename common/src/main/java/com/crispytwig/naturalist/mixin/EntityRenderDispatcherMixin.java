package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.IKMount;
import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.MobPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void naturalist$skipBakedRider(Entity entity, double x, double y, double z, float rotationYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (entity instanceof Player && entity.getVehicle() instanceof IKMount) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHitbox", at = @At("TAIL"))
    private static void naturalist$renderMobPartHitboxes(PoseStack poseStack, VertexConsumer buffer, Entity entity, float partialTick, float red, float green, float blue, CallbackInfo ci) {
        if (entity instanceof MultipartMob multipart) {
            double x = -Mth.lerp(partialTick, entity.xOld, entity.getX());
            double y = -Mth.lerp(partialTick, entity.yOld, entity.getY());
            double z = -Mth.lerp(partialTick, entity.zOld, entity.getZ());
            for (MobPart part : multipart.getParts()) {
                poseStack.pushPose();
                double px = x + Mth.lerp(partialTick, part.xOld, part.getX());
                double py = y + Mth.lerp(partialTick, part.yOld, part.getY());
                double pz = z + Mth.lerp(partialTick, part.zOld, part.getZ());
                poseStack.translate(px, py, pz);
                LevelRenderer.renderLineBox(poseStack, buffer, part.getBoundingBox().move(-part.getX(), -part.getY(), -part.getZ()), 0.25F, 1.0F, 0.0F, 1.0F);
                poseStack.popPose();
            }
        }
    }
}
