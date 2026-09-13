package com.crispytwig.naturalist.compat.fieldguide.mixin;

import com.evandev.fieldguide.client.gui.util.IconCacheManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IconCacheManager.class)
public class IconCacheManagerMixin {

    @Unique
    private static final int naturalist$alphaThreshold = 10;

    @Inject(
            method = "lambda$generateAndSaveIcon$2",
            at = @At(value = "INVOKE", target = "Lcom/evandev/fieldguide/client/gui/util/IconCacheManager;mcRegisterWithSilhouette(Ljava/lang/String;Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/platform/NativeImage;)V")
    )
    private static void naturalist$centerIcon(GpuBuffer buffer, String key, String namespace, String fileName, RenderTarget target, ProjectionMatrixBuffer projection, CallbackInfo ci, @Local NativeImage image) {
        if ("naturalist".equals(namespace)) {
            naturalist$centerContent(image);
        }
    }

    @Unique
    private static void naturalist$centerContent(NativeImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int minX = w, minY = h, maxX = -1, maxY = -1;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int alpha = (image.getPixel(x, y) >>> 24) & 0xFF;
                if (alpha >= naturalist$alphaThreshold) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < 0) {
            return;
        }

        int dx = w / 2 - (minX + maxX + 1) / 2;
        int dy = h / 2 - (minY + maxY + 1) / 2;
        if (dx == 0 && dy == 0) {
            return;
        }

        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = image.getPixel(x, y);
            }
        }
        image.fillRect(0, 0, w, h, 0);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                    image.setPixel(nx, ny, pixels[y * w + x]);
                }
            }
        }
    }
}
