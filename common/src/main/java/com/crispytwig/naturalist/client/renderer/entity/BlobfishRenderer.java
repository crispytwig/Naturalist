package com.crispytwig.naturalist.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishGrayModel;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishPinkModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BlobfishRenderer extends MobRenderer<Blobfish, HierarchicalModel<Blobfish>> {
    private static final ResourceLocation GRAY = Naturalist.location("textures/entity/blobfish/gray.png");
    private static final ResourceLocation PINK = Naturalist.location("textures/entity/blobfish/pink.png");

    private final HierarchicalModel<Blobfish> pinkModel;
    private final HierarchicalModel<Blobfish> grayModel;

    public BlobfishRenderer(EntityRendererProvider.Context context) {
        super(context, new BlobfishPinkModel(context.bakeLayer(BlobfishPinkModel.LAYER_LOCATION)), 0.0F);
        this.pinkModel = this.model;
        this.grayModel = new BlobfishGrayModel(context.bakeLayer(BlobfishGrayModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Blobfish entity) {
        if (entity.hasNonDefaultVariant()) {
            return entity.getVariantTexture();
        }
        return entity.isGray() ? GRAY : PINK;
    }

    @Override
    public void render(@NotNull Blobfish entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = entity.isGray() ? this.grayModel : this.pinkModel;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void setupRotations(@NotNull Blobfish entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.swimTilt.getTilt(partialTick)));
    }
}
