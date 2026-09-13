package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.variant.LegacyVariantRemap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Inject(method = "by(Lnet/minecraft/world/level/storage/ValueInput;)Ljava/util/Optional;", at = @At("HEAD"))
    private static void naturalist$remapLegacyMobs(ValueInput input, CallbackInfoReturnable<Optional<EntityType<?>>> cir) {
        if (input instanceof TagValueInputAccessor accessor) {
            LegacyVariantRemap.apply(accessor.naturalist$getInput());
        }
    }
}
