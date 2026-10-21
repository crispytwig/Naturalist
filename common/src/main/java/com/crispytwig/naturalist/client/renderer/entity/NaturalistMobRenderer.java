package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public abstract class NaturalistMobRenderer<T extends Mob & DataDrivenVariantAnimal> extends MobRenderer<T, HierarchicalModel<T>> {
    private final HierarchicalModel<T> adultModel;
    private final HierarchicalModel<T> babyModel;
    private final float adultShadowRadius;
    private final float babyShadowRadius;

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, HierarchicalModel<T> adultModel, HierarchicalModel<T> babyModel, float shadowRadius) {
        this(context, adultModel, babyModel, shadowRadius, shadowRadius / 2.0F);
    }

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, HierarchicalModel<T> adultModel, HierarchicalModel<T> babyModel, float shadowRadius, float babyShadowRadius) {
        super(context, adultModel, shadowRadius);
        this.adultModel = adultModel;
        this.babyModel = babyModel;
        this.adultShadowRadius = shadowRadius;
        this.babyShadowRadius = babyShadowRadius;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return entity.isBaby() ? entity.getVariantBabyTexture() : entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = entity.isBaby() ? this.babyModel : this.adultModel;
        this.shadowRadius = entity.isBaby() ? this.babyShadowRadius : this.adultShadowRadius;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
