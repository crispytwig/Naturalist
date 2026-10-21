package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.hedgehog.HedgehogModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.client.renderer.entity.layers.HedgehogGlintLayer;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class HedgehogRenderer extends MobRenderer<Hedgehog, HierarchicalModel<Hedgehog>> {
    public HedgehogRenderer(EntityRendererProvider.Context context) {
        super(context, new HedgehogModel(context.bakeLayer(HedgehogModel.LAYER_LOCATION)), 0.25F);
        this.addLayer(new DyeLayer<>(this, "hedgehog"));
        this.addLayer(new HedgehogGlintLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Hedgehog entity) {
        return entity.getVariantTexture();
    }

    @Override
    protected void scale(@NotNull Hedgehog entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public void render(@NotNull Hedgehog entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.shadowRadius = entity.isBaby() ? 0.12F : 0.25F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
