package com.crispytwig.naturalist.client.model.animal.crab;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.CrabBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
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

public class CrabBabyModel extends NaturalistEntityModel<Crab> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("crab_baby"), "main");
	private static final float MAX_EYE_PITCH = Mth.DEG_TO_RAD * 10F;
	private static final float MAX_EYE_YAW = Mth.DEG_TO_RAD * 22.5F;
	private final ModelPart root;
	private final ModelPart leftEye;

	public CrabBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.leftEye = this.root.getChild("crabBody").getChild("leftEye");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition crabBody = root.addOrReplaceChild("crabBody", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, -0.5F));
		PartDefinition crabTorso = crabBody.addOrReplaceChild("crabTorso", CubeListBuilder.create()
		.texOffs(0, 3).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.026F)), PartPose.offset(0.0F, -0.5F, 0.5F));
		PartDefinition shell = crabBody.addOrReplaceChild("shell", CubeListBuilder.create()
		.texOffs(0, 12).addBox(-1.5F, -4.0F, -0.5F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -0.25F));
		PartDefinition leftClaw = crabBody.addOrReplaceChild("leftClaw", CubeListBuilder.create()
		.texOffs(0, 12).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(6, 7).addBox(0.0F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.5F, -1.5F));
		PartDefinition rightClaw = crabBody.addOrReplaceChild("rightClaw", CubeListBuilder.create()
		.texOffs(0, 12).mirror().addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(6, 7).mirror().addBox(-1.0F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 0.5F, -1.5F));
		PartDefinition leftEye = crabBody.addOrReplaceChild("leftEye", CubeListBuilder.create()
		.texOffs(0, 7).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -1.5F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, -0.5F));
		PartDefinition crabLeftArm = legs.addOrReplaceChild("crabLeftArm", CubeListBuilder.create(), PartPose.offset(1.0F, 0.5F, 0.0F));
		PartDefinition crabLeftArm_r1 = crabLeftArm.addOrReplaceChild("crabLeftArm_r1", CubeListBuilder.create()
		.texOffs(0, 10).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition crabRightArm = legs.addOrReplaceChild("crabRightArm", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.5F, 0.0F));
		PartDefinition crabRightArm_r1 = crabRightArm.addOrReplaceChild("crabRightArm_r1", CubeListBuilder.create()
		.texOffs(0, 10).mirror().addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		PartDefinition crabLeftLeg = legs.addOrReplaceChild("crabLeftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.5F, 1.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition crabLeftLeg_r1 = crabLeftLeg.addOrReplaceChild("crabLeftLeg_r1", CubeListBuilder.create()
		.texOffs(0, 10).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition crabRightLeg = legs.addOrReplaceChild("crabRightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.5F, 1.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition crabRightLeg_r1 = crabRightLeg.addOrReplaceChild("crabRightLeg_r1", CubeListBuilder.create()
		.texOffs(0, 10).mirror().addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Crab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.root.yRot += Mth.HALF_PI;

		this.animateSmooth(entity.sitAnimationState, CrabBabyAnimation.CRAB_BABY_SIT_IDLE, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, CrabBabyAnimation.CRAB_BABY_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, CrabBabyAnimation.CRAB_BABY_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 6.0F));

		CrabModel.applyLook(this.renderState(), MAX_EYE_YAW, MAX_EYE_PITCH, this.leftEye, null);
	}
}
