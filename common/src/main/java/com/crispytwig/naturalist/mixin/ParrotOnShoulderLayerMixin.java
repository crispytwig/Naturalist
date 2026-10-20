package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ParrotFlight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParrotOnShoulderLayer.class)
public abstract class ParrotOnShoulderLayerMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void naturalist$markShoulderFlap(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Player player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ParrotFlight.shoulderShouldFlap = ParrotFlight.hasBirdOnHead(player) && !player.onGround();
        ParrotFlight.shoulderPartialTick = partialTick;
    }
}
