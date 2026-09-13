package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.client.resources.sounds.DragonflySoundInstance;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(at = @At(value = "HEAD"), method = "postAddEntitySoundInstance")
    @SuppressWarnings("unused")
    private void handleAddMob(Entity entity, CallbackInfo ci) {
        if (entity instanceof Dragonfly dragonfly) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new DragonflySoundInstance(dragonfly));
        }
    }
}
