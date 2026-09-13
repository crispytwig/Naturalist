package com.crispytwig.naturalist.client.model.animal.scorpion;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DesertScorpionAnimation;
import com.crispytwig.naturalist.world.entity.animal.scorpion.DesertScorpion;
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

public class DesertScorpionModel extends NaturalistEntityModel<DesertScorpion> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("desert_scorpion"), "main");
	private final ModelPart root;

	public DesertScorpionModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(22, 0).addBox(-1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.5F, -1.0F, -6.0F, 7.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 3.0F));
		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(12, 10).addBox(-1.5F, 0.0F, -1.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.25F, -5.0F, 0.0F, 0.3054F, 0.0F));
		PartDefinition leftClaw = leftArm.addOrReplaceChild("leftClaw", CubeListBuilder.create()
		.texOffs(0, 10).addBox(-3.0F, -2.25F, -6.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.0F, -1.0F, 0.0F, -0.2618F, 0.0F));
		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(12, 10).mirror().addBox(-5.5F, 0.0F, -1.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.25F, -5.0F, 0.0F, -0.3054F, 0.0F));
		PartDefinition rightClaw = rightArm.addOrReplaceChild("rightClaw", CubeListBuilder.create()
		.texOffs(0, 10).mirror().addBox(-2.0F, -2.25F, -6.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.0F, -1.0F, 0.0F, 0.2618F, 0.0F));
		PartDefinition tail_1 = body.addOrReplaceChild("tail_1", CubeListBuilder.create()
		.texOffs(2, 16).addBox(0.0F, -9.4554F, -0.9718F, 0.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5446F, 2.9718F));
		PartDefinition tail_2 = tail_1.addOrReplaceChild("tail_2", CubeListBuilder.create(), PartPose.offset(0.0F, -7.4554F, -0.9718F));
		PartDefinition tail_2_r1 = tail_2.addOrReplaceChild("tail_2_r1", CubeListBuilder.create()
		.texOffs(16, 24).addBox(-2.0F, -1.75F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(23, 0).addBox(0.0F, 2.25F, -3.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 3.0F));
		PartDefinition frontLeftLeg = legs.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create(), PartPose.offset(3.5F, -0.85F, -2.5F));
		PartDefinition frontLeftLeg_r1 = frontLeftLeg.addOrReplaceChild("frontLeftLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 0.575F, 0.0F, 0.0F, 0.0F, -0.829F));
		PartDefinition frontRightLeg = legs.addOrReplaceChild("frontRightLeg", CubeListBuilder.create(), PartPose.offset(-3.5F, -0.85F, -2.5F));
		PartDefinition frontRightLeg_r1 = frontRightLeg.addOrReplaceChild("frontRightLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.575F, 0.0F, 0.0F, 0.0F, 0.829F));
		PartDefinition middleLeftLeg = legs.addOrReplaceChild("middleLeftLeg", CubeListBuilder.create(), PartPose.offset(3.5F, -0.85F, -0.5F));
		PartDefinition middleLeftLeg_r1 = middleLeftLeg.addOrReplaceChild("middleLeftLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 0.575F, 0.0F, 0.0F, 0.0F, -0.829F));
		PartDefinition middleRightLeg = legs.addOrReplaceChild("middleRightLeg", CubeListBuilder.create(), PartPose.offset(-3.5F, -0.85F, -0.5F));
		PartDefinition middleRightLeg_r1 = middleRightLeg.addOrReplaceChild("middleRightLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.575F, 0.0F, 0.0F, 0.0F, 0.829F));
		PartDefinition backLeftLeg = legs.addOrReplaceChild("backLeftLeg", CubeListBuilder.create(), PartPose.offset(3.5F, -0.85F, -0.5F));
		PartDefinition backLeftLeg_r1 = backLeftLeg.addOrReplaceChild("backLeftLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 0.575F, 2.0F, 0.0F, 0.0F, -0.829F));
		PartDefinition backRightLeg = legs.addOrReplaceChild("backRightLeg", CubeListBuilder.create(), PartPose.offset(-3.5F, -0.85F, -0.5F));
		PartDefinition backRightLeg_r1 = backRightLeg.addOrReplaceChild("backRightLeg_r1", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.575F, 2.0F, 0.0F, 0.0F, 0.829F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(DesertScorpion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, DesertScorpionAnimation.DESERT_SCORPION_ATTACK, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, DesertScorpionAnimation.DESERT_SCORPION_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, DesertScorpionAnimation.DESERT_SCORPION_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.runAnimationState, DesertScorpionAnimation.DESERT_SCORPION_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.6F));
	}
}
