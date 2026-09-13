package com.crispytwig.naturalist.client.model.animal.ant;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.AntAnimation;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
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

public class AntModel extends NaturalistEntityModel<Ant> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("ant"), "main");
	private final ModelPart root;

	public AntModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 21).addBox(-1.5F, -3.5F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.5F, -5.5F, 0.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -2.5F, 1.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -2.0F));
		PartDefinition antennae = skull.addOrReplaceChild("antennae", CubeListBuilder.create()
		.texOffs(17, 19).addBox(-2.5F, -5.0F, -3.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -5.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, -0.5F));
		PartDefinition left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));
		PartDefinition left_middle = left_legs.addOrReplaceChild("left_middle", CubeListBuilder.create()
		.texOffs(17, 4).addBox(-0.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -2.0F, 0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition left_front = left_legs.addOrReplaceChild("left_front", CubeListBuilder.create()
		.texOffs(17, 4).addBox(-0.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -1.75F, -2.25F, 0.0F, 0.6109F, 0.7854F));
		PartDefinition left_back = left_legs.addOrReplaceChild("left_back", CubeListBuilder.create()
		.texOffs(17, 4).addBox(-0.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -1.75F, 3.25F, 0.0F, -0.6109F, 0.7854F));
		PartDefinition right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));
		PartDefinition right_middle = right_legs.addOrReplaceChild("right_middle", CubeListBuilder.create()
		.texOffs(17, 4).mirror().addBox(-3.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -2.0F, 0.5F, 0.0F, 0.0F, -0.7854F));
		PartDefinition right_front = right_legs.addOrReplaceChild("right_front", CubeListBuilder.create()
		.texOffs(17, 4).mirror().addBox(-3.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -1.75F, -2.25F, 0.0F, -0.6109F, -0.7854F));
		PartDefinition right_back = right_legs.addOrReplaceChild("right_back", CubeListBuilder.create()
		.texOffs(17, 4).mirror().addBox(-3.5F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -1.75F, 3.25F, 0.0F, 0.6109F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Ant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, AntAnimation.ANT_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, AntAnimation.ANT_WALK, ageInTicks, partialTick,
				entity.isClimbing() ? 1.0F : movementAnimationSpeed(entity, limbSwingAmount, 3.0F));
	}
}
