package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FireflyGlowLayer extends RenderLayer<Firefly, HierarchicalModel<Firefly>> {
    private static final ResourceLocation GLOW = Naturalist.location("textures/entity/firefly_glow.png");
    private static final ResourceLocation GLOW_E = Naturalist.location("textures/entity/firefly_glow_e.png");
    private static final int TOTAL_FRAMES = 30;
    private static final int TICKS_PER_FRAME = 1;

    public FireflyGlowLayer(RenderLayerParent<Firefly, HierarchicalModel<Firefly>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Firefly entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        int frame;
        if (entity.isGlowing()) {
            frame = Math.min((entity.tickCount - entity.getGlowStartTick()) / TICKS_PER_FRAME, TOTAL_FRAMES - 1);
        } else {
            frame = 0;
        }

        VertexConsumer baseBuffer = new AnimatedUVVertexConsumer(bufferSource.getBuffer(RenderType.entityCutoutNoCull(GLOW)), TOTAL_FRAMES, frame);
        this.getParentModel().renderToBuffer(poseStack, baseBuffer, packedLight, OverlayTexture.NO_OVERLAY, -1);

        if (entity.isGlowing()) {
            VertexConsumer emissiveBuffer = new AnimatedUVVertexConsumer(bufferSource.getBuffer(RenderType.entityTranslucentEmissive(GLOW_E)), TOTAL_FRAMES, frame);
            this.getParentModel().renderToBuffer(poseStack, emissiveBuffer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}
