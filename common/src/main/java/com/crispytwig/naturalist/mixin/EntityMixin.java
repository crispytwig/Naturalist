package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.MobPart;
import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.level.MultipartLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // Chunk unloads and dimension changes call setRemoved without remove(), so unregister the parts here.
    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void naturalist$unregisterMobParts(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof MultipartMob multipart && self.level() instanceof MultipartLevel level) {
            MobPart.unregisterAll(level, multipart.getParts());
        }
    }
}
