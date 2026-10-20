package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.WolfMoleDigging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfModel.class)
public abstract class WolfModelMixin {
    @Shadow @Final private ModelPart head;
    @Shadow @Final private ModelPart body;
    @Shadow @Final private ModelPart upperBody;
    @Shadow @Final private ModelPart tail;
    @Shadow @Final private ModelPart leftFrontLeg;
    @Shadow @Final private ModelPart rightFrontLeg;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/Wolf;FFFFF)V", at = @At("TAIL"))
    @SuppressWarnings("unused")
    private void naturalist$digAnim(Wolf wolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!((WolfMoleDigging) wolf).naturalist$isDiggingOutMole()) {
            return;
        }
        float time = wolf.tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float scratch = Mth.cos(time * 1.1F);
        this.rightFrontLeg.xRot = scratch * 0.9F;
        this.leftFrontLeg.xRot = -scratch * 0.9F;
        this.head.xRot = Math.max(this.head.xRot, 0.85F);
        float crouch = 1.5F;
        this.body.y += crouch;
        this.upperBody.y += crouch;
        this.tail.y += crouch;
        this.head.y = this.head.getInitialPose().y + crouch;
        float tilt = 0.15F;
        this.body.xRot += tilt;
        this.upperBody.xRot += tilt;
        this.tail.xRot -= tilt;
    }
}
