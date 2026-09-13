package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {
    @Inject(method = "isPreventingPlayerRest", at = @At(value = "HEAD"), cancellable = true)
    private void onIsPreventingPlayerRest(ServerLevel level, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isHolding(NaturalistRegistry.PLUSH_BEAR.get().asItem())) {
            cir.setReturnValue(false);
        }
    }
}
