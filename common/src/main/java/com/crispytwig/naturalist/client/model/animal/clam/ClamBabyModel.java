package com.crispytwig.naturalist.client.model.animal.clam;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ClamBabyModel extends NaturalistEntityModel<Clam> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("clam_baby"), "main");

	public ClamBabyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 4.0F));
		root.addOrReplaceChild("top", CubeListBuilder.create()
		.texOffs(1, 27).addBox(-6.0F, -2.5F, -9.0F, 12.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bottom = root.addOrReplaceChild("bottom", CubeListBuilder.create()
		.texOffs(1, 1).addBox(-6.0F, -0.5F, -9.0F, 12.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(76, 29).addBox(-3.0F, -1.5F, -7.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = bottom.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -1.75F, -2.0F, -0.2182F, -0.2618F, -0.192F));
		root.addOrReplaceChild("hinge", CubeListBuilder.create()
		.texOffs(59, 53).addBox(-3.0F, -1.5F, -0.25F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.25F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Clam entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
	}
}
