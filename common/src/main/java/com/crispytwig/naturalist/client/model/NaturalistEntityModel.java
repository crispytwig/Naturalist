package com.crispytwig.naturalist.client.model;

import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class NaturalistEntityModel<E extends Entity> extends HierarchicalModel<E> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private static final double GAIT_FACTOR = 0.65D;
    private static final double LIMB_SWING_PER_SPEED = 8.64D;
    protected static final float IDLE_FADE_SCALE = 2.5F;

    public static final double SMALL_SWIMMER_LIMB_SWING = 0.25D;
    public static final double LARGE_SWIMMER_LIMB_SWING = 0.5D;

    private final Map<String, Optional<ModelPart>> partsByName = new HashMap<>();
    private ModelPart[] allParts;

    public NaturalistEntityModel() {
        super();
    }

    public NaturalistEntityModel(Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
    }

    protected String getRootPartName() {
        return "root";
    }

    @Override
    public @NonNull Optional<ModelPart> getAnyDescendantWithName(String name) {
        Optional<ModelPart> cached = this.partsByName.get(name);
        if (cached != null) {
            return cached;
        }
        Optional<ModelPart> resolved = this.root().getAllParts()
                .filter(part -> part.hasChild(name))
                .findFirst()
                .map(part -> part.getChild(name));
        if (resolved.isEmpty() && name.equals(this.getRootPartName())) {
            resolved = Optional.of(this.root());
        }
        this.partsByName.put(name, resolved);
        return resolved;
    }

    protected void resetPose() {
        if (this.allParts == null) {
            this.allParts = this.root().getAllParts().toArray(ModelPart[]::new);
        }
        for (ModelPart part : this.allParts) {
            part.resetPose();
        }
    }

    @Override
    public final void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetPose();
        this.setupAnimations(entity, limbSwing, limbSwingAmount, ageInTicks, ageInTicks - entity.tickCount, netHeadYaw, headPitch);
    }

    protected abstract void setupAnimations(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch);

    protected static void applyHeadLook(ModelPart part, float netHeadYaw, float headPitch) {
        part.xRot += headPitch * Mth.DEG_TO_RAD;
        part.yRot += netHeadYaw * Mth.DEG_TO_RAD;
    }

    protected void rotatePart(ModelPart part, float dx, float dy, float dz) {
        part.xRot += dx;
        part.yRot += dy;
        part.zRot += dz;
    }

    protected void bend(ModelPart part, MultipartMob mob, int segment, float partialTick) {
        this.rotatePart(part,
                mob.getSegmentPitchOffset(segment, partialTick) * Mth.DEG_TO_RAD,
                mob.getSegmentYawOffset(segment, partialTick) * Mth.DEG_TO_RAD,
                0.0F);
    }

    protected static float movementAnimationSpeed(LivingEntity entity, float limbSwingAmount, float baseSpeed) {
        double gaitLimbSwing = Math.min(LIMB_SWING_PER_SPEED * entity.getAttributeValue(Attributes.MOVEMENT_SPEED), 1.0D) * GAIT_FACTOR;
        return movementAnimationSpeed(entity, limbSwingAmount, baseSpeed, gaitLimbSwing);
    }

    protected static float movementAnimationSpeed(LivingEntity entity, float limbSwingAmount, float baseSpeed, double referenceLimbSwing) {
        return movementAnimationSpeed(entity, limbSwingAmount, baseSpeed, referenceLimbSwing, 0.4F);
    }

    protected static float movementAnimationSpeed(LivingEntity entity, float limbSwingAmount, float baseSpeed, double referenceLimbSwing, float minSpeed) {
        return baseSpeed * Mth.clamp(limbSwingAmount / (float) Math.max(referenceLimbSwing, 0.05D), minSpeed, 2.0F);
    }

    protected void animateSmooth(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks, float partialTick) {
        this.animateSmooth(state, definition, ageInTicks, partialTick, 1.0F);
    }

    protected void animateSmooth(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks, float partialTick, float speed) {
        float factor = state.factor(partialTick);
        if (factor <= SmoothAnimationState.ACTIVE_THRESHOLD) {
            return;
        }
        state.updateTime(ageInTicks, speed);
        KeyframeAnimations.animate(this, definition, state.getAccumulatedTime(), factor, ANIMATION_VECTOR_CACHE);
    }

    protected void animateUnblended(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks) {
        if (state.isStarted()) {
            state.updateTime(ageInTicks, 1.0F);
        }
        KeyframeAnimations.animate(this, definition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
    }

    protected void animateIdleSmooth(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks, float partialTick, float limbSwingAmount) {
        this.animateIdleSmooth(state, definition, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 1.0F);
    }

    protected void animateIdleSmooth(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks, float partialTick, float limbSwingAmount, float animationScaleFactor, float speed) {
        float factor = state.factor(partialTick) * (1.0F - Math.min(limbSwingAmount * animationScaleFactor, 1.0F));
        if (factor <= SmoothAnimationState.ACTIVE_THRESHOLD) {
            return;
        }
        state.updateTime(ageInTicks, speed);
        KeyframeAnimations.animate(this, definition, state.getAccumulatedTime(), factor, ANIMATION_VECTOR_CACHE);
    }
}
