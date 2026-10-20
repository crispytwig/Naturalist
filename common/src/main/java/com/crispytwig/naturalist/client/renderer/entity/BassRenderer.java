package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.fish.BassModel;
import com.crispytwig.naturalist.client.model.animal.fish.LargeBassModel;
import com.crispytwig.naturalist.client.model.animal.fish.MediumBassModel;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class BassRenderer extends MobRenderer<Bass, HierarchicalModel<Bass>> {
    private final HierarchicalModel<Bass> normalModel;
    private final HierarchicalModel<Bass> mediumModel;
    private final HierarchicalModel<Bass> largeModel;

    public BassRenderer(EntityRendererProvider.Context context) {
        super(context, new BassModel(context.bakeLayer(BassModel.LAYER_LOCATION)), 0.0F);
        this.normalModel = this.model;
        this.mediumModel = new MediumBassModel(context.bakeLayer(MediumBassModel.LAYER_LOCATION));
        this.largeModel = new LargeBassModel(context.bakeLayer(LargeBassModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Bass entity) {
        return entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull Bass entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isLargeVariant()) {
            this.model = this.largeModel;
        } else if (entity.isMediumVariant()) {
            this.model = this.mediumModel;
        } else {
            this.model = this.normalModel;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void setupRotations(@NotNull Bass entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float nativeScale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, nativeScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.swimTilt.getTilt(partialTick)));
    }
}
