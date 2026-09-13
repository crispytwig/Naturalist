package com.crispytwig.naturalist.client.model;

import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.MultipartMob;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class NaturalistEntityModel<E extends Entity> extends EntityModel<NaturalistRenderState<E>> {
    private static final String DEFAULT_ROOT_PART_NAME = "root";
    private static final double GAIT_FACTOR = 0.65D;
    private static final double LIMB_SWING_PER_SPEED = 8.64D;
    protected static final float IDLE_FADE_SCALE = 2.5F;

    public static final double SMALL_SWIMMER_LIMB_SWING = 0.25D;
    public static final double LARGE_SWIMMER_LIMB_SWING = 0.5D;

    private final Map<AnimationDefinition, KeyframeAnimation> bakedAnimations = new IdentityHashMap<>();
    private ModelPart animationRoot;
    private NaturalistRenderState<E> renderState;

    protected NaturalistEntityModel(ModelPart root) {
        super(root);
    }

    protected NaturalistEntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    protected String getRootPartName() {
        return DEFAULT_ROOT_PART_NAME;
    }

    private ModelPart animationRoot() {
        if (this.animationRoot == null) {
            String name = this.getRootPartName();
            ModelPart found = name.equals(DEFAULT_ROOT_PART_NAME)
                    ? (this.root.hasChild(name) ? this.root.getChild(name) : null)
                    : this.root.createPartLookup().apply(name);
            this.animationRoot = found != null ? found : this.root;
        }
        return this.animationRoot;
    }

    protected KeyframeAnimation bakedAnimation(AnimationDefinition definition) {
        return this.bakedAnimations.computeIfAbsent(definition, def -> {
            ModelPart animRoot = this.animationRoot();
            String name = this.getRootPartName();
            ModelPart bakeRoot = name.equals(DEFAULT_ROOT_PART_NAME) ? animRoot : new ModelPart(List.of(), Map.of(name, animRoot));
            Function<String, ModelPart> lookup = bakeRoot.createPartLookup();
            Map<String, List<AnimationChannel>> present = new LinkedHashMap<>();
            def.boneAnimations().forEach((bone, channels) -> {
                if (lookup.apply(bone) != null) {
                    present.put(bone, channels);
                }
            });
            AnimationDefinition filtered = present.size() == def.boneAnimations().size()
                    ? def
                    : new AnimationDefinition(def.lengthInSeconds(), def.looping(), present);
            return filtered.bake(bakeRoot);
        });
    }

    @Override
    public final void setupAnim(NaturalistRenderState<E> state) {
        super.setupAnim(state);
        this.renderState = state;
        this.setupAnimations(state.entity, state.walkAnimationPos, state.walkAnimationSpeed, state.ageInTicks, state.partialTick, state.yRot, state.xRot);
        this.renderState = null;
    }

    protected NaturalistRenderState<E> renderState() {
        return this.renderState;
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
        this.bakedAnimation(definition).apply(state.getAccumulatedTime(), factor);
    }

    protected void animateUnblended(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks) {
        if (state.isStarted()) {
            state.updateTime(ageInTicks, 1.0F);
        }
        this.bakedAnimation(definition).apply(state.getAccumulatedTime(), 1.0F);
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
        this.bakedAnimation(definition).apply(state.getAccumulatedTime(), factor);
    }
}
