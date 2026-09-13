package com.crispytwig.naturalist.client.model.animal.bird;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BirdAnimation;
import com.crispytwig.naturalist.world.entity.animal.bird.Bird;
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

public class BirdModel extends NaturalistEntityModel<Bird> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("bird"), "main");
	private final ModelPart root;
	private final ModelPart head;

	public BirdModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.head = this.root.getChild("bodyRot").getChild("body").getChild("headRot").getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition bodyRot = root.addOrReplaceChild("bodyRot", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition body = bodyRot.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 7).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.05F))
		.texOffs(16, 15).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(8, 16).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));
		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition leftWingShoulderRot = wings.addOrReplaceChild("leftWingShoulderRot", CubeListBuilder.create(), PartPose.offset(1.5F, 3.0F, 1.5F));
		PartDefinition leftWingRot = leftWingShoulderRot.addOrReplaceChild("leftWingRot", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, -1.5F));
		PartDefinition leftWing = leftWingRot.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(14, 6).addBox(-1.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.0F));
		PartDefinition rightWingShoulderRot = wings.addOrReplaceChild("rightWingShoulderRot", CubeListBuilder.create(), PartPose.offset(-1.5F, 3.0F, 1.5F));
		PartDefinition rightWingRot = rightWingShoulderRot.addOrReplaceChild("rightWingRot", CubeListBuilder.create(), PartPose.offset(-0.5F, -1.0F, -1.5F));
		PartDefinition rightWing = rightWingRot.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(14, 6).mirror().addBox(0.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 0.0F, 0.0F));
		PartDefinition headRot = body.addOrReplaceChild("headRot", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));
		PartDefinition head = headRot.addOrReplaceChild("head", CubeListBuilder.create()
		.texOffs(0, 21).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 1).addBox(-0.5F, -2.0F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition comb = head.addOrReplaceChild("comb", CubeListBuilder.create(), PartPose.offset(0.0F, -2.6F, 0.0F));
		PartDefinition comb_r1 = comb.addOrReplaceChild("comb_r1", CubeListBuilder.create()
		.texOffs(19, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition legs = bodyRot.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.5F));
		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(16, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));
		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(20, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Bird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, BirdAnimation.PASSERINE_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BirdAnimation.PASSERINE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));
		this.animateSmooth(entity.flyAnimationState, BirdAnimation.PASSERINE_FLY, ageInTicks, partialTick);

		applyHeadLook(this.head, netHeadYaw, headPitch);

		if (!entity.onGround()) {
			this.root.xRot += entity.getFlyPitch(partialTick);
			this.root.zRot = netHeadYaw * (Mth.PI / 600F);
		}
	}
}
