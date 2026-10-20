package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class DyeLayer<T extends LivingEntity & DyeableAnimal, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final String folder;
    private final EnumMap<DyeColor, ResourceLocation> textures = new EnumMap<>(DyeColor.class);

    public DyeLayer(RenderLayerParent<T, M> parent, String folder) {
        super(parent);
        this.folder = folder;
    }

    protected ResourceLocation getDyeTexture(T entity, DyeColor color) {
        ResourceLocation texture = this.textures.get(color);
        if (texture == null) {
            texture = this.getDyeTexture(color.getName());
            this.textures.put(color, texture);
        }
        return texture;
    }

    protected ResourceLocation getDyeTexture(String name) {
        return Naturalist.location("textures/entity/" + this.folder + "/dye/" + name + ".png");
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        DyeColor color = entity.getDyeColor();
        if (color == null || entity.isInvisible()) {
            return;
        }
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getDyeTexture(entity, color))), packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
