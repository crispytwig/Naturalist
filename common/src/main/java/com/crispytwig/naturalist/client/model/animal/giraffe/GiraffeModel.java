package com.crispytwig.naturalist.client.model.animal.giraffe;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.GiraffeAnimation;
import com.crispytwig.naturalist.world.entity.animal.giraffe.Giraffe;
import com.crispytwig.naturalist.world.entity.TerrainLegSolver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.IKEntityModel;
import com.crispytwig.naturalist.client.model.SeatedModel;

public class GiraffeModel extends IKEntityModel<Giraffe> implements SeatedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("giraffe"), "main");
	private final ModelPart root;
	private final ModelPart hips;
	private final ModelPart shoulders;
	private final ModelPart body;
	private final ModelPart seat;
    private final ModelPart head;
	private final ModelPart saddle;
    private final ModelPart leftFoot;
	private final ModelPart rightFoot;
    private final ModelPart leftHand;
	private final ModelPart rightHand;

	public GiraffeModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.hips = this.root.getChild("hips");
		this.shoulders = this.hips.getChild("shoulders");
		this.body = this.shoulders.getChild("body");
		this.seat = this.body.getChild("seat");
        ModelPart neck = this.body.getChild("neck");
		this.head = neck.getChild("head");
		this.saddle = neck.getChild("saddle");
        ModelPart legs = this.root.getChild("legs");
        ModelPart back_legs = legs.getChild("back_legs");
		this.leftFoot = back_legs.getChild("leftFoot");
		this.rightFoot = back_legs.getChild("rightFoot");
        ModelPart front_legs = legs.getChild("front_legs");
		this.leftHand = front_legs.getChild("leftHand");
		this.rightHand = front_legs.getChild("rightHand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));
		PartDefinition hips = root.addOrReplaceChild("hips", CubeListBuilder.create(), PartPose.offset(0.0F, -33.6174F, 12.6415F));
		PartDefinition shoulders = hips.addOrReplaceChild("shoulders", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -23.0F));
		PartDefinition body = shoulders.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(1, 0).addBox(-7.5F, -14.3826F, -14.6415F, 15.0F, 20.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 10.0F));
		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, -12.3826F, -3.6415F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -7.2347F, -3.717F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(1, 49).addBox(-4.5F, -54.8521F, -14.0755F, 9.0F, 17.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 38.9545F, 20.5467F, 0.3927F, 0.0F, 0.0F));
		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create()
		.texOffs(17, 15).addBox(1.0F, -37.5573F, -12.5922F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 8).addBox(1.0F, -37.5573F, -12.5922F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.25F))
		.texOffs(17, 15).mirror().addBox(-3.0F, -37.5573F, -12.5922F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 8).mirror().addBox(-3.0F, -37.5573F, -12.5922F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, -4.8079F, -9.2977F));
		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create()
		.texOffs(0, 4).addBox(3.5F, -86.8521F, -6.8255F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.5F, -86.8521F, -6.8255F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 90).addBox(-1.5F, -86.8521F, -4.8255F, 3.0F, 33.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(62, 49).addBox(-3.5F, -86.8521F, -13.8255F, 7.0F, 33.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(31, 52).addBox(-1.5F, -86.8521F, -16.8255F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 43.7624F, 29.8444F, 0.3927F, 0.0F, 0.0F));
		PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create()
		.texOffs(0, 16).addBox(-2.5F, -4.0F, -7.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(63, 16).addBox(-2.5F, -4.0F, -7.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -25.6445F, -13.8695F, 0.3927F, 0.0F, 0.0F));
		PartDefinition saddle = neck.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(69, 91).addBox(-4.5F, -54.8521F, -14.0755F, 9.0F, 17.0F, 20.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 38.9545F, 20.5467F, 0.3927F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -9.3826F, 13.3585F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(58, 0).addBox(-2.5F, 0.0F, -32.0F, 5.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9635F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(6.0F, -34.0F, 12.5F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftFoot = back_legs.addOrReplaceChild("leftFoot", CubeListBuilder.create(), PartPose.offset(-1.0F, 32.0F, -2.5F));
		PartDefinition left_leg = leftFoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(0, 90).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 32.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -32.0F, 2.5F));
		PartDefinition rightFoot = back_legs.addOrReplaceChild("rightFoot", CubeListBuilder.create(), PartPose.offset(-11.0F, 32.0F, -2.5F));
		PartDefinition right_leg = rightFoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(0, 90).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 32.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -32.0F, 2.5F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -23.0F));
		PartDefinition leftHand = front_legs.addOrReplaceChild("leftHand", CubeListBuilder.create(), PartPose.offset(-1.0F, 32.0F, -2.5F));
		PartDefinition left_arm = leftHand.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(0, 90).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 32.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -32.0F, 2.5F));
		PartDefinition rightHand = front_legs.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offset(-11.0F, 32.0F, -2.5F));
		PartDefinition right_arm = rightHand.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(0, 90).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 32.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -32.0F, 2.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Giraffe entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.saddle.visible = entity.isTame();

		this.animateIdleSmooth(entity.idleAnimationState, GiraffeAnimation.GIRAFFE_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, GiraffeAnimation.GIRAFFE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.3F));
		this.animateSmooth(entity.runAnimationState, GiraffeAnimation.GIRAFFE_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.43F));

		this.articulateLegs(entity, partialTick);

		applyHeadLook(this.head, netHeadYaw, headPitch);
	}

	@Override
	protected TerrainLegSolver getLegSolver(Giraffe entity) {
		return entity.legSolver;
	}

	@Override
	protected ModelPart bodyPart() {
		return this.body;
	}

	@Override
	protected ModelPart headPart() {
		return this.head;
	}

	@Override
	protected ModelPart[] legParts() {
		return new ModelPart[]{this.leftFoot, this.rightFoot, this.leftHand, this.rightHand};
	}

	@Override
	public float seatHeight() {
		return 0.25F;
	}

	@Override
	public void translateToSeat(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.hips.translateAndRotate(poseStack);
		this.shoulders.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		this.seat.translateAndRotate(poseStack);
	}

	@Override
	public float seatZRot() {
		return this.root.zRot + this.hips.zRot + this.shoulders.zRot + this.body.zRot + this.seat.zRot;
	}
}
