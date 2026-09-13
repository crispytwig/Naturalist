package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.accessor.ExtendedLivingRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.animal.parrot.ParrotModel;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.ParrotRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParrotOnShoulderLayer.class)
public abstract class ParrotOnShoulderLayerMixin {
    @Inject(
            method = "submitOnShoulder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private void naturalist$flyingShoulderPose(CallbackInfo ci, @Local(argsOnly = true) AvatarRenderState playerState, @Local ParrotRenderState parrotState) {
        if (((ExtendedLivingRenderState) playerState).naturalist$shoulderFlap()) {
            parrotState.pose = ParrotModel.Pose.FLYING;
            parrotState.flapAngle = Mth.sin(playerState.ageInTicks * 1.5F) + 1.0F;
        }
    }
}
