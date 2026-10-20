package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class GlowLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Function<T, ResourceLocation> glowmask;

    public GlowLayer(RenderLayerParent<T, M> parent, Function<T, ResourceLocation> glowmask) {
        super(parent);
        this.glowmask = glowmask;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        ResourceLocation texture = this.glowmask.apply(entity);
        if (texture == null) {
            return;
        }
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(texture)), packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
