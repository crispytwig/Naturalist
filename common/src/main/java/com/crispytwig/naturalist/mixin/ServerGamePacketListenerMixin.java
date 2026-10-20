package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ParrotFlight;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {
    @Shadow
    public ServerPlayer player;

    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private boolean clientIsFloating;

    @Inject(method = "tick", at = @At("HEAD"))
    private void naturalist$allowParrotFlight(CallbackInfo ci) {
        if (this.player != null && ParrotFlight.canAscend(this.player)) {
            this.aboveGroundTickCount = 0;
            this.clientIsFloating = false;
        }
    }
}
