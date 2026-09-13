package com.crispytwig.naturalist.client.model.animal.scorpion;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.JungleScorpionAnimation;
import com.crispytwig.naturalist.world.entity.animal.scorpion.JungleScorpion;
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

public class JungleScorpionModel extends NaturalistEntityModel<JungleScorpion> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("jungle_scorpion"), "main");
	private final ModelPart root;

	public JungleScorpionModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(48, 3).addBox(-3.0F, -8.0F, 1.0F, 6.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-4.5F, -8.0F, -10.0F, 9.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(27, 36).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 10.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(48, 28).addBox(-1.5F, -10.0F, -1.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.75F, 0.3927F, 0.0F, 0.0F));
		PartDefinition stinger = tail2.addOrReplaceChild("stinger", CubeListBuilder.create()
		.texOffs(65, 30).addBox(-3.0F, -4.9002F, -5.7549F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(39, 13).addBox(-0.5F, -4.1502F, -10.7549F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -10.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(-0.5F, -5.0F, -6.0F));
		PartDefinition leftArm = arms.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(4, 43).addBox(-1.5F, -1.5F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.5F, -1.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition leftClaw = leftArm.addOrReplaceChild("leftClaw", CubeListBuilder.create()
		.texOffs(2, 22).addBox(-2.0F, -4.5F, -10.0F, 4.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, -6.0F, -0.3927F, 0.7854F, -0.7854F));
		PartDefinition rightArm = arms.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(4, 43).mirror().addBox(-0.5F, -1.5F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.5F, -1.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition rightClaw = rightArm.addOrReplaceChild("rightClaw", CubeListBuilder.create()
		.texOffs(2, 22).mirror().addBox(-2.0F, -4.5F, -10.0F, 4.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, -1.0F, -6.0F, -0.3927F, -0.7854F, 0.7854F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -1.0F));
		PartDefinition leftLegs = legs.addOrReplaceChild("leftLegs", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -0.5F, 0.5F, 0.0F, 0.0F, 0.2182F));
		PartDefinition frontLeftLeg = leftLegs.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create()
		.texOffs(54, 21).addBox(0.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.3927F, 0.3054F));
		PartDefinition middleLeftLeg = leftLegs.addOrReplaceChild("middleLeftLeg", CubeListBuilder.create()
		.texOffs(54, 21).addBox(0.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3054F));
		PartDefinition backLeftLeg = leftLegs.addOrReplaceChild("backLeftLeg", CubeListBuilder.create()
		.texOffs(54, 21).addBox(0.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 3.0F, 0.0F, -0.3927F, 0.3054F));
		PartDefinition rightLegs = legs.addOrReplaceChild("rightLegs", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -0.5F, 0.5F, 0.0F, 0.0F, -0.2182F));
		PartDefinition frontRightLeg = rightLegs.addOrReplaceChild("frontRightLeg", CubeListBuilder.create()
		.texOffs(54, 21).mirror().addBox(-11.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, -0.3927F, -0.3054F));
		PartDefinition middleRightLeg = rightLegs.addOrReplaceChild("middleRightLeg", CubeListBuilder.create()
		.texOffs(54, 21).mirror().addBox(-11.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3054F));
		PartDefinition backRightLeg = rightLegs.addOrReplaceChild("backRightLeg", CubeListBuilder.create()
		.texOffs(54, 21).mirror().addBox(-11.0F, -1.5F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 3.0F, 0.0F, 0.3927F, -0.3054F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	protected void setupAnimations(JungleScorpion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, JungleScorpionAnimation.JUNGLE_SCORPION_ATTACK, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, JungleScorpionAnimation.JUNGLE_SCORPION_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, JungleScorpionAnimation.JUNGLE_SCORPION_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.runAnimationState, JungleScorpionAnimation.JUNGLE_SCORPION_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.6F));
	}
}
