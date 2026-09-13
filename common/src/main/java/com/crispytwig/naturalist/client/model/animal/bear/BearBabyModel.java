package com.crispytwig.naturalist.client.model.animal.bear;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BearBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
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

public class BearBabyModel extends NaturalistEntityModel<Bear> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("bear_baby"), "main");
	private final ModelPart root;
	private final ModelPart neck;
	private final ModelPart awake;
	private final ModelPart asleep;

	public BearBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("skullRot").getChild("neck");
		this.awake = this.neck.getChild("awake");
		this.asleep = this.neck.getChild("asleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, -1.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-6.5F, -5.5F, -7.25F, 13.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -9.5F, 4.25F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(82, 117).addBox(-6.5F, -13.0F, -3.5F, 13.0F, 1.0F, 10.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 1.0F, 5.75F, 1.5708F, 0.0F, 0.0F));
		PartDefinition bell = body.addOrReplaceChild("bell", CubeListBuilder.create(), PartPose.offset(0.0F, 3.5F, -7.75F));
		PartDefinition bell_r1 = bell.addOrReplaceChild("bell_r1", CubeListBuilder.create()
		.texOffs(116, 118).addBox(-1.5F, -15.0F, -5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 12.5F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, 6.75F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(36, 34).addBox(-1.5F, -3.0F, 2.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 3.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, -7.625F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(12, 39).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -4.75F, -4.125F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(12, 39).mirror().addBox(-1.5F, -3.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -3.75F, -4.125F));
		PartDefinition awake = neck.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 24).addBox(-5.5F, -4.0F, -3.5F, 11.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.75F, -3.125F));
		PartDefinition asleep = neck.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(19, 43).addBox(-5.5F, -16.5F, -18.9F, 11.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.75F, 12.275F));
		PartDefinition snout = neck.addOrReplaceChild("snout", CubeListBuilder.create()
		.texOffs(0, 39).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.25F, -6.625F));
		PartDefinition nose = snout.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, -2.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(4.5F, -5.0F, -0.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(36, 24).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(36, 24).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(36, 24).mirror().addBox(-2.0F, 0.0F, -3.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 0.0F, 8.5F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(36, 24).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Bear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sleeping = entity.isSleeping();
		this.awake.visible = !sleeping;
		this.asleep.visible = sleeping;

		this.animateSmooth(entity.sleepAnimationState, BearBabyAnimation.BEAR_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, BearBabyAnimation.BEAR_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sniffAnimationState, BearBabyAnimation.BEAR_SNIFF, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, BearBabyAnimation.BEAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BearBabyAnimation.BEAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, BearBabyAnimation.BEAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
			applyHeadLook(this.neck, netHeadYaw, headPitch);
		}
	}

}
