package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.accessor.ExtendedLivingRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.animal.wolf.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfModel.class)
public abstract class WolfModelMixin {
    @Shadow @Final protected ModelPart head;
    @Shadow @Final protected ModelPart body;
    @Shadow @Final protected ModelPart tail;
    @Shadow @Final protected ModelPart leftFrontLeg;
    @Shadow @Final protected ModelPart rightFrontLeg;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/WolfRenderState;)V", at = @At("TAIL"))
    @SuppressWarnings("unused")
    private void naturalist$digAnim(WolfRenderState state, CallbackInfo ci) {
        if (!((ExtendedLivingRenderState) state).naturalist$isDiggingOutMole()) {
            return;
        }
        float time = state.ageInTicks;
        float scratch = Mth.cos(time * 1.1F);
        this.rightFrontLeg.xRot = scratch * 0.9F;
        this.leftFrontLeg.xRot = -scratch * 0.9F;
        this.head.xRot = Math.max(this.head.xRot, 0.85F);
        float crouch = 1.5F;
        float tilt = 0.15F;
        this.body.y += crouch;
        this.tail.y += crouch;
        this.head.y = this.head.getInitialPose().y() + crouch;
        this.body.xRot += tilt;
        this.tail.xRot -= tilt;
        ModelPart root = ((Model<?>) (Object) this).root();
        if (root.hasChild("upper_body")) {
            ModelPart upperBody = root.getChild("upper_body");
            upperBody.y += crouch;
            upperBody.xRot += tilt;
        }
    }
}
