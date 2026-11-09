package com.crispytwig.naturalist.common.mixin;

import com.crispytwig.naturalist.common.entity.fish.Blobfish;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFish.class)
public abstract class AbstractFishMixin extends WaterAnimal {
    protected AbstractFishMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void naturalist$blobfishNoFlop(CallbackInfo ci) {
        if ((Object) this instanceof Blobfish) {
            super.aiStep();
            ci.cancel();
        }
    }
}
