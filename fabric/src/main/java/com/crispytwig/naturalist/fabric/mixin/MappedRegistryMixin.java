package com.crispytwig.naturalist.fabric.mixin;

import com.crispytwig.naturalist.world.level.LegacyIds;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
    @Shadow
    public abstract ResourceKey<? extends Registry<T>> key();

    @Shadow
    public abstract @Nullable T get(@Nullable ResourceLocation name);

    @Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
    private void naturalist$redirectLegacyId(@Nullable ResourceLocation name, CallbackInfoReturnable<T> cir) {
        if (name == null || !"naturalist".equals(name.getNamespace())) {
            return;
        }
        ResourceLocation target = LegacyIds.renamedTarget(this.key().location(), name);
        if (target != null) {
            cir.setReturnValue(this.get(target));
        }
    }
}
