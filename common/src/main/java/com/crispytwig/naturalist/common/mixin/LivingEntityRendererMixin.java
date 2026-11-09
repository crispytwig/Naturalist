package com.crispytwig.naturalist.common.mixin;

import com.crispytwig.naturalist.client.renderer.entity.state.ExtendedLivingRenderState;
import com.crispytwig.naturalist.common.entity.IKMount;
import com.crispytwig.naturalist.common.entity.WolfMoleDigging;
import com.crispytwig.naturalist.common.entity.ParrotFlight;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void naturalist$extractExtendedState(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        ExtendedLivingRenderState extended = (ExtendedLivingRenderState) state;
        Player player = entity instanceof Player p ? p : null;
        IKMount mount = player != null && player.getVehicle() instanceof IKMount m ? m : null;
        extended.naturalist$setShoulderFlap(player != null && ParrotFlight.hasBirdOnHead(player) && !player.onGround());
        extended.naturalist$setLean(mount != null ? mount.getRenderPitch() : 0.0F, mount != null ? mount.getRenderRoll() : 0.0F);
        extended.naturalist$setDiggingOutMole(entity instanceof WolfMoleDigging digging && digging.naturalist$isDiggingOutMole());
    }
}
