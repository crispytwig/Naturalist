package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.komododragon.KomodoDragonModel;
import com.crispytwig.naturalist.world.entity.animal.komododragon.KomodoDragon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class KomodoDragonRenderer extends MobRenderer<KomodoDragon, HierarchicalModel<KomodoDragon>> {
    public KomodoDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new KomodoDragonModel(context.bakeLayer(KomodoDragonModel.LAYER_LOCATION)), 0.65F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull KomodoDragon entity) {
        return entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull KomodoDragon entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.shadowRadius = entity.isBaby() ? 0.3F : 0.65F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void scale(@NotNull KomodoDragon entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.45F, 0.45F, 0.45F);
        }
    }
}
