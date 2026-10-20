package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TortoiseMaskLayer<M extends EntityModel<Tortoise>> extends RenderLayer<Tortoise, M> {
    private static final ResourceLocation DONATELLO = Naturalist.location("textures/entity/tortoise/donatello.png");
    private static final ResourceLocation LEONARDO = Naturalist.location("textures/entity/tortoise/leonardo.png");
    private static final ResourceLocation MICHELANGELO = Naturalist.location("textures/entity/tortoise/michelangelo.png");
    private static final ResourceLocation RAPHAEL = Naturalist.location("textures/entity/tortoise/raphael.png");

    public TortoiseMaskLayer(RenderLayerParent<Tortoise, M> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Tortoise entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible() || !entity.hasCustomName()) {
            return;
        }
        ResourceLocation skin = switch (entity.getName().getString()) {
            case "Donatello" -> DONATELLO;
            case "Leonardo" -> LEONARDO;
            case "Michelangelo" -> MICHELANGELO;
            case "Raphael" -> RAPHAEL;
            default -> null;
        };
        if (skin == null) {
            return;
        }
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(skin)), packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
