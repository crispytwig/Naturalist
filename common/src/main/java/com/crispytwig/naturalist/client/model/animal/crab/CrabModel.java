package com.crispytwig.naturalist.client.model.animal.crab;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.renderer.entity.CrabRenderer;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.client.animation.definitions.CrabAnimation;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class CrabModel extends NaturalistEntityModel<Crab> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("crab"), "main");
	private static final float MAX_EYE_PITCH = Mth.DEG_TO_RAD * 22.5F;
	private static final float MAX_EYE_YAW = Mth.DEG_TO_RAD * 90.0F;
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart crabBody;
    private final ModelPart leftEye;
	private final ModelPart rightEye;
	private final ModelPart rightClaw;
	private final ModelPart rightArm;
	private final ModelPart rightItem;

	public CrabModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.crabBody = this.head.getChild("crabBody");
        ModelPart crabHead = this.crabBody.getChild("crabHead");
		this.leftEye = crabHead.getChild("leftEye");
		this.rightEye = crabHead.getChild("rightEye");
		this.rightClaw = this.crabBody.getChild("rightClaw");
		this.rightArm = this.rightClaw.getChild("rightArm");
		this.rightItem = this.rightArm.getChild("rightItem");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 6.5F, 0.0F));
		PartDefinition crabBody = head.addOrReplaceChild("crabBody", CubeListBuilder.create(), PartPose.offset(0.5F, -8.75F, -1.0F));
		PartDefinition shell = crabBody.addOrReplaceChild("shell", CubeListBuilder.create()
		.texOffs(0, 18).addBox(-3.0F, -5.0F, -1.0F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
		PartDefinition crabHead = crabBody.addOrReplaceChild("crabHead", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -0.25F, -1.0F));
		PartDefinition leftEye = crabHead.addOrReplaceChild("leftEye", CubeListBuilder.create()
		.texOffs(0, 6).mirror().addBox(-0.5F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -1.0F, -2.0F));
		PartDefinition rightEye = crabHead.addOrReplaceChild("rightEye", CubeListBuilder.create()
		.texOffs(0, 6).addBox(-1.5F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, -2.0F));
		PartDefinition leftClaw = crabBody.addOrReplaceChild("leftClaw", CubeListBuilder.create()
		.texOffs(0, 10).mirror().addBox(-0.5F, -1.0F, -3.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 0.75F, -2.0F));
		PartDefinition rightClaw = crabBody.addOrReplaceChild("rightClaw", CubeListBuilder.create()
		.texOffs(0, 10).addBox(-2.5F, -1.0F, -3.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.75F, -2.0F));
		PartDefinition rightArm = rightClaw.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -1.0F, -2.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition legs = head.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.5F, -8.0F, -1.5F));
		PartDefinition rightLegs = legs.addOrReplaceChild("rightLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.5F));
		PartDefinition crabRightArm = rightLegs.addOrReplaceChild("crabRightArm", CubeListBuilder.create()
		.texOffs(9, 2).mirror().addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.3927F));
		PartDefinition crabRightLeg = rightLegs.addOrReplaceChild("crabRightLeg", CubeListBuilder.create()
		.texOffs(9, 2).mirror().addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.3927F, -0.3927F));
		PartDefinition leftLegs = legs.addOrReplaceChild("leftLegs", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 0.5F));
		PartDefinition crabLeftLeg = leftLegs.addOrReplaceChild("crabLeftLeg", CubeListBuilder.create()
		.texOffs(9, 2).addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3927F, 0.3927F));
		PartDefinition crabLeftArm = leftLegs.addOrReplaceChild("crabLeftArm", CubeListBuilder.create()
		.texOffs(9, 2).addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.3927F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Crab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.root.yRot += Mth.HALF_PI;

		this.animateSmooth(entity.hideAnimationState, CrabAnimation.CRAB_HIDE, ageInTicks, partialTick);
		this.animateSmooth(entity.hideLoopAnimationState, CrabAnimation.CRAB_HIDE_LOOP, ageInTicks, partialTick);
		this.animateSmooth(entity.peekAnimationState, CrabAnimation.CRAB_HIDE_PEEK, ageInTicks, partialTick);
		this.animateSmooth(entity.unhideAnimationState, CrabAnimation.CRAB_UNHIDE, ageInTicks, partialTick);

		this.animateSmooth(entity.danceAnimationState, CrabAnimation.CRAB_DANCE, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, CrabAnimation.CRAB_SIT_LOOP, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, CrabAnimation.CRAB_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, CrabAnimation.CRAB_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 4.0F));

		this.animateSmooth(entity.swingAnimationState, CrabAnimation.CRAB_SWING_RIGHT, ageInTicks, partialTick);

		applyLook(this.renderState(), MAX_EYE_YAW, MAX_EYE_PITCH, this.leftEye, this.rightEye);
	}

	static void applyLook(NaturalistRenderState<Crab> state, float maxYaw, float maxPitch, ModelPart leftEye, ModelPart rightEye) {
		if (!(state instanceof CrabRenderer.CrabRenderState look) || look.portrait) {
			return;
		}
		float eyeYaw = Mth.clamp(look.eyeYaw, -maxYaw, maxYaw);
		float eyePitch = Mth.clamp(look.eyePitch, -maxPitch, maxPitch);
		leftEye.xRot -= eyePitch;
		leftEye.yRot -= eyeYaw;
		if (rightEye != null) {
			rightEye.xRot -= eyePitch;
			rightEye.yRot -= eyeYaw;
		}
	}

	public void translateToItem(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.head.translateAndRotate(poseStack);
		this.crabBody.translateAndRotate(poseStack);
		this.rightClaw.translateAndRotate(poseStack);
		this.rightArm.translateAndRotate(poseStack);
		this.rightItem.translateAndRotate(poseStack);
	}
}
