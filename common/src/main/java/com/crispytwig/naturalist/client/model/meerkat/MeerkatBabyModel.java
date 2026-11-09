package com.crispytwig.naturalist.client.model.meerkat;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MeerkatBabyModel extends MeerkatModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("meerkat_baby"), "main");

	public MeerkatBabyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition waist = main.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 1.5F));
		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -1.5F));
		PartDefinition headRotation = body.addOrReplaceChild("headRotation", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, -2.5F));
		PartDefinition skull = headRotation.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(10, 6).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(1.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).mirror().addBox(-2.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.5F));
		PartDefinition awake = skull.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 6).addBox(-1.5F, -4.0F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 2.0F));
		PartDefinition asleep = skull.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 16).addBox(-1.5F, -4.0F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 2.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(7, 11).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 2.0F));
		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(10, 9).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.5F, -1.65F, 0.0F, -0.3927F, 0.0F));
		PartDefinition leftHand = leftArm.addOrReplaceChild("leftHand", CubeListBuilder.create()
		.texOffs(10, 8).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));
		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(10, 9).mirror().addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 0.5F, -1.65F, 0.0F, 0.3927F, 0.0F));
		PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create()
		.texOffs(10, 8).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.5F, 0.0F));
		PartDefinition legs = main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 1.65F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(10, 11).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
		PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot", CubeListBuilder.create()
		.texOffs(0, 10).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(10, 11).mirror().addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
		PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot", CubeListBuilder.create()
		.texOffs(0, 10).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 32);
	}
}
