package com.crispytwig.naturalist.client.model.animal.bird;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BirdBabyAnimation;
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

public class BirdBabyModel extends NaturalistEntityModel<Bird> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("bird_baby"), "main");
	private final ModelPart root;

	public BirdBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(4, 10).addBox(-1.5F, -2.25F, -1.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.3F))
		.texOffs(0, 2).addBox(0.0F, -5.25F, -1.25F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.75F, 0.0F));
		PartDefinition mouthOpen = neck.addOrReplaceChild("mouthOpen", CubeListBuilder.create()
		.texOffs(13, 0).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.25F, 0.75F));
		PartDefinition mouthClosed = neck.addOrReplaceChild("mouthClosed", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.25F, 0.75F));
		PartDefinition beak = neck.addOrReplaceChild("beak", CubeListBuilder.create()
		.texOffs(10, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.25F, -2.25F));
		PartDefinition leftWing = neck.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(-1, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -0.25F, -0.25F));
		PartDefinition rightWing = neck.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(-1, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -0.25F, -0.25F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	protected void setupAnimations(Bird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, BirdBabyAnimation.BIRD_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BirdBabyAnimation.BIRD_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));

		if (!entity.onGround()) {
			this.root.xRot += entity.getFlyPitch(partialTick);
			this.root.zRot = netHeadYaw * (Mth.PI / 600F);
		}
	}
}
