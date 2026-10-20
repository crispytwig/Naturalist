package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ParrotFlight;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @ModifyArgs(
            method = "renderOnShoulder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ParrotModel;setupAnim(Lnet/minecraft/client/model/ParrotModel$State;IFFFFF)V")
    )
    private void naturalist$flyingShoulderAnim(Args args) {
        if (!ParrotFlight.shoulderShouldFlap) {
            return;
        }
        int tickCount = args.get(1);
        args.set(0, ParrotModel.State.FLYING);
        args.set(4, Mth.sin((tickCount + ParrotFlight.shoulderPartialTick) * 1.5F) + 1.0F);
    }
}
