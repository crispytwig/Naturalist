package com.crispytwig.naturalist.client.model.animal.dragonfly;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DragonflyAnimation;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
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

public class DragonflyModel extends NaturalistEntityModel<Dragonfly> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("dragonfly"), "main");
	private final ModelPart root;

	public DragonflyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 16).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -5.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.1667F, 3.6667F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(18, 13).addBox(-1.5F, -1.1667F, -0.3333F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.8333F, -4.6667F));
		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(1.0F, -1.1667F, 5.1667F));
		PartDefinition wingsBack = wings.addOrReplaceChild("wingsBack", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition wingLeftBack = wingsBack.addOrReplaceChild("wingLeftBack", CubeListBuilder.create()
		.texOffs(12, 6).addBox(0.3378F, 0.3686F, -2.5F, 9.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));
		PartDefinition wingRightBack = wingsBack.addOrReplaceChild("wingRightBack", CubeListBuilder.create()
		.texOffs(12, 6).mirror().addBox(-9.3378F, 0.3686F, -2.5F, 9.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));
		PartDefinition wingsFront = wings.addOrReplaceChild("wingsFront", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -4.0F));
		PartDefinition wingRightFront = wingsFront.addOrReplaceChild("wingRightFront", CubeListBuilder.create()
		.texOffs(12, 3).mirror().addBox(-10.9532F, 0.2113F, -1.5F, 11.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));
		PartDefinition wingLeftFront = wingsFront.addOrReplaceChild("wingLeftFront", CubeListBuilder.create()
		.texOffs(12, 3).addBox(-0.0468F, 0.2113F, -1.5F, 11.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));
		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 1.8333F, 1.6667F));
		PartDefinition legsFront = legs.addOrReplaceChild("legsFront", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition legsMiddle = legs.addOrReplaceChild("legsMiddle", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition legsBack = legs.addOrReplaceChild("legsBack", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Dragonfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flyAnimationState, DragonflyAnimation.DRAGONFLY_FLY, ageInTicks, partialTick);
	}
}
