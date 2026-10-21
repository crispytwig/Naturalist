package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.variant.LegacyVariantRemap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Inject(method = "by(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/Optional;", at = @At("HEAD"))
    private static void naturalist$remapLegacyMobs(CompoundTag tag, CallbackInfoReturnable<Optional<EntityType<?>>> cir) {
        LegacyVariantRemap.apply(tag);
    }
}
