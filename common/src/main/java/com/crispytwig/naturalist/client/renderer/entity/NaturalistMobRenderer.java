package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import org.jspecify.annotations.Nullable;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

public abstract class NaturalistMobRenderer<T extends Mob & DataDrivenVariantAnimal> extends MobRenderer<T, NaturalistRenderState<T>, NaturalistEntityModel<T>> {
    private final NaturalistEntityModel<T> adultModel;
    private final @Nullable NaturalistEntityModel<T> babyModel;
    private final float babyScale;
    private final float babyShadowRadius;

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, NaturalistEntityModel<T> model, float shadowRadius) {
        this(context, model, shadowRadius, 1.0F, shadowRadius);
    }

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, NaturalistEntityModel<T> model, float shadowRadius, float babyScale, float babyShadowRadius) {
        super(context, model, shadowRadius);
        this.adultModel = model;
        this.babyModel = null;
        this.babyScale = babyScale;
        this.babyShadowRadius = babyShadowRadius;
    }

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, NaturalistEntityModel<T> adultModel, NaturalistEntityModel<T> babyModel, float shadowRadius) {
        this(context, adultModel, babyModel, shadowRadius, shadowRadius / 2.0F);
    }

    protected NaturalistMobRenderer(EntityRendererProvider.Context context, NaturalistEntityModel<T> adultModel, NaturalistEntityModel<T> babyModel, float shadowRadius, float babyShadowRadius) {
        super(context, adultModel, shadowRadius);
        this.adultModel = adultModel;
        this.babyModel = babyModel;
        this.babyScale = 1.0F;
        this.babyShadowRadius = babyShadowRadius;
    }

    protected Identifier getTexture(T entity) {
        return this.babyModel != null && entity.isBaby() ? entity.getVariantBabyTexture() : entity.getVariantTexture();
    }

    @Override
    public NaturalistRenderState<T> createRenderState() {
        return new NaturalistRenderState<>();
    }

    @Override
    public void extractRenderState(T entity, NaturalistRenderState<T> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.texture = this.getTexture(entity);
    }

    @Override
    public Identifier getTextureLocation(NaturalistRenderState<T> state) {
        return state.texture;
    }

    @Override
    protected void scale(NaturalistRenderState<T> state, PoseStack poseStack) {
        if (state.isBaby && this.babyScale != 1.0F) {
            poseStack.scale(this.babyScale, this.babyScale, this.babyScale);
        }
    }

    @Override
    protected float getShadowRadius(NaturalistRenderState<T> state) {
        if (this.babyModel != null) {
            return (state.isBaby ? this.babyShadowRadius : this.shadowRadius) * state.ageScale;
        }
        float radius = super.getShadowRadius(state);
        return state.isBaby && this.shadowRadius > 0.0F ? radius * (this.babyShadowRadius / this.shadowRadius) : radius;
    }

    @Override
    public void submit(NaturalistRenderState<T> state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (this.babyModel != null) {
            this.model = state.isBaby ? this.babyModel : this.adultModel;
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
