package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ParrotFlight;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ParrotModel.class)
public abstract class ParrotModelMixin {
    @ModifyArg(
            method = "renderOnShoulder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ParrotModel;prepare(Lnet/minecraft/client/model/ParrotModel$State;)V"),
            index = 0
    )
    private ParrotModel.State naturalist$flyingShoulderPose(ParrotModel.State state) {
        return ParrotFlight.shoulderShouldFlap ? ParrotModel.State.FLYING : state;
    }

    @ModifyArg(
            method = "renderOnShoulder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ParrotModel;setupAnim(Lnet/minecraft/client/model/ParrotModel$State;IFFFFF)V"),
            index = 0
    )
    private ParrotModel.State naturalist$flyingShoulderAnimState(ParrotModel.State state) {
        return ParrotFlight.shoulderShouldFlap ? ParrotModel.State.FLYING : state;
    }

    @ModifyArg(
            method = "renderOnShoulder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ParrotModel;setupAnim(Lnet/minecraft/client/model/ParrotModel$State;IFFFFF)V"),
            index = 4
    )
    private float naturalist$flyingShoulderAnimAge(ParrotModel.State state, int tickCount, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!ParrotFlight.shoulderShouldFlap) {
            return ageInTicks;
        }
        return Mth.sin((tickCount + ParrotFlight.shoulderPartialTick) * 1.5F) + 1.0F;
    }
}
