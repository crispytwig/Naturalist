package com.crispytwig.naturalist.compat.fieldguide.mixin;

import com.crispytwig.naturalist.client.NaturalistPortraitRenderState;
import com.evandev.fieldguide.client.gui.util.EntryRenderHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryRenderHelper.class)
public class EntryRenderHelperMixin {
    @Unique
    private static final String naturalist$renderEntity = "renderEntity(Lnet/minecraft/world/entity/Entity;Ljava/lang/Object;ZFIIIIFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lcom/evandev/fieldguide/api/variant/VariantDef;Lcom/evandev/fieldguide/api/variant/VariantProvider;)V";

    @Inject(method = naturalist$renderEntity, at = @At("HEAD"))
    private static void naturalist$portraitStart(CallbackInfo ci) {
        NaturalistPortraitRenderState.ACTIVE = true;
    }

    @Inject(method = naturalist$renderEntity, at = @At("RETURN"))
    private static void naturalist$portraitEnd(CallbackInfo ci) {
        NaturalistPortraitRenderState.ACTIVE = false;
    }

    @ModifyArg(
            method = naturalist$renderEntity,
            at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationY(F)Lorg/joml/Quaternionf;")
    )
    private static float naturalist$rotatePortrait(float angle, @Local(argsOnly = true) Entity entity) {
        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (id.getNamespace().equals("naturalist") && id.getPath().equals("crab")) {
            return angle + (float) Math.toRadians(90.0);
        }
        return angle;
    }
}
