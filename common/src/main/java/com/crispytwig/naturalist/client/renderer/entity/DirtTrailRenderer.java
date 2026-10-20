package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.mole.DirtTrailModel;
import com.crispytwig.naturalist.world.entity.animal.mole.DirtTrail;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DirtTrailRenderer extends EntityRenderer<DirtTrail> {
    private static final ResourceLocation TEXTURE = Naturalist.location("textures/entity/mole.png");

    private final DirtTrailModel model;

    public DirtTrailRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DirtTrailModel(context.bakeLayer(DirtTrailModel.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DirtTrail entity) {
        return TEXTURE;
    }

    @Override
    public void render(@NotNull DirtTrail entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, -(entity.getId() % 3) * 0.0625D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees((entity.getId() * 61) % 360));
        if (entity.isSmall()) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(0.0F, 0.01F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTick, 0.0F, 0.0F);
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
