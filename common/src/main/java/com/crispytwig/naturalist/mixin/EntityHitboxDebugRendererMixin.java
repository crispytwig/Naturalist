package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.MobPart;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {
    @Inject(method = "showHitboxes(Lnet/minecraft/world/entity/Entity;FZ)V", at = @At("TAIL"))
    private void naturalist$renderMobPartHitboxes(Entity entity, float partialTicks, boolean isServerEntity, CallbackInfo ci) {
        if (entity instanceof MultipartMob multipart) {
            for (MobPart part : multipart.getMobParts()) {
                Vec3 offset = part.getPosition(partialTicks).subtract(part.position());
                Gizmos.cuboid(part.getBoundingBox().move(offset), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
            }
        }
    }
}
