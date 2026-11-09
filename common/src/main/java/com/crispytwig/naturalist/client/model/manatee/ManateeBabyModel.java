package com.crispytwig.naturalist.client.model.manatee;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ManateeBabyModel extends ManateeModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("manatee_baby"), "main");

	public ManateeBabyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 28).addBox(-4.0F, -2.825F, -4.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(22, 40).addBox(-2.0F, -1.825F, -6.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.175F, -6.0F));
		PartDefinition nose = skull.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0F, 0.675F, -4.2F));
		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(0, 38).addBox(0.2F, -0.9F, -1.4F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.8F, -2.1F, -3.6F));
		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(0, 38).mirror().addBox(-6.2F, -0.9F, -1.4F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.8F, -2.1F, -3.6F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(24, 28).addBox(-2.0F, -2.5F, -0.2F, 4.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 4.2F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(0, 18).addBox(-6.0F, -0.5F, -2.5F, 12.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.3F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}
