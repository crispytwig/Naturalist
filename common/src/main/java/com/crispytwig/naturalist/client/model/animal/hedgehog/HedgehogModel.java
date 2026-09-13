package com.crispytwig.naturalist.client.model.animal.hedgehog;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.HedgehogAnimation;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class HedgehogModel extends NaturalistEntityModel<Hedgehog> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("hedgehog"), "main");
	private final ModelPart root;
	private final ModelPart rolled;

	public HedgehogModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.rolled = this.root.getChild("rolled");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition rolled = root.addOrReplaceChild("rolled", CubeListBuilder.create()
		.texOffs(30, 1).addBox(-3.0F, -2.25F, -3.25F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(25, 25).addBox(-2.0F, -3.25F, -4.25F, 4.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(26, 16).addBox(-5.0F, -4.25F, 1.75F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(26, 16).addBox(-5.0F, -4.25F, -1.25F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.75F, 0.25F));
		PartDefinition unrolled = root.addOrReplaceChild("unrolled", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = unrolled.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -2.5F, -3.5F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.01F))
		.texOffs(0, 16).addBox(-5.0F, -4.5F, -2.5F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-5.0F, -4.5F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-5.0F, -4.5F, 2.5F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(5, 4).mirror().addBox(3.0F, -2.5F, -3.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 4).addBox(-4.0F, -2.5F, -3.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -0.5F));
		PartDefinition snout = body.addOrReplaceChild("snout", CubeListBuilder.create()
		.texOffs(0, 12).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(13, 13).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -3.5F));
		PartDefinition legs = unrolled.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -0.5F));
		PartDefinition left_arm = legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(21, 3).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(21, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(1.75F, 0.0F, -1.5F));
		PartDefinition right_arm = legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(21, 3).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.75F, 0.0F, -1.5F));
		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(21, 3).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(21, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(1.75F, 0.0F, 1.5F));
		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(21, 3).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.75F, 0.0F, 1.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Hedgehog entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.animateSmooth(entity.sitAnimationState, HedgehogAnimation.HEDGEHOG_SIT, ageInTicks, partialTick);
		this.animateIfStarted(entity.hideAnimationState, HedgehogAnimation.HEDGEHOG_HIDE, ageInTicks);
		this.animateIfStarted(entity.unhideAnimationState, HedgehogAnimation.HEDGEHOG_UNHIDE, ageInTicks);
		this.animateSmooth(entity.rollGroundAnimationState, HedgehogAnimation.HEDGEHOG_ROLL_GROUND, ageInTicks, partialTick);
		this.animateSmooth(entity.rollAirAnimationState, HedgehogAnimation.HEDGEHOG_ROLL_AIR, ageInTicks, partialTick);
		this.animateSmooth(entity.idleEventAnimationState, HedgehogAnimation.HEDGEHOG_IDLE_EVENT, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, HedgehogAnimation.HEDGEHOG_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, HedgehogAnimation.HEDGEHOG_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));

		this.rolled.visible = entity.isFullyRolled();
	}

	private void animateIfStarted(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks) {
		if (!state.isStarted()) {
			return;
		}
		state.updateTime(ageInTicks, 1.0F);
		this.bakedAnimation(definition).apply(state.getAccumulatedTime(), 1.0F);
	}
}
