package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ParrotFlight;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Unique
    private static final double naturalist$ascendAccel = 0.15D;
    @Unique
    private static final double naturalist$ascendMax = 0.22D;

    @Shadow
    public ClientInput input;

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void naturalist$parrotAscend(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (!this.input.keyPresses.jump() || self.onGround() || self.getAbilities().flying || !ParrotFlight.canAscend(self)) {
            return;
        }
        Vec3 movement = self.getDeltaMovement();
        self.setDeltaMovement(movement.x, Math.min(movement.y + naturalist$ascendAccel, naturalist$ascendMax), movement.z);
        self.resetFallDistance();
    }
}
