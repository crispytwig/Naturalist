package com.crispytwig.naturalist.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    protected abstract void removeEntitiesOnShoulder();

    @Inject(method = "removeEntitiesOnShoulder", at = @At("HEAD"), cancellable = true)
    private void naturalist$keepShoulderParrotsWhileFalling(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.fallDistance > 0.5F && !self.isInWater() && !self.getAbilities().flying
                && !self.isSleeping() && !self.isInPowderSnow && !self.isCrouching()) {
            ci.cancel();
        }
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void naturalist$dropShoulderParrotsWhenCrouching(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (!self.level().isClientSide() && self.isCrouching()) {
            this.removeEntitiesOnShoulder();
        }
    }
}
