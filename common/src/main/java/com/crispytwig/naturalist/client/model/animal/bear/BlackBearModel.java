package com.crispytwig.naturalist.client.model.animal.bear;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BlackBearAnimation;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class BlackBearModel extends NaturalistEntityModel<BlackBear> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("black_bear"), "main");
	private final ModelPart root;
	private final ModelPart skullRot;
    private final ModelPart head_angry;
	private final ModelPart sleep;
	private final ModelPart snout;
	private final ModelPart snout_berries;
	private final ModelPart snout_honey;
	private final ModelPart snout_angry;
	private final ModelPart front_legs;
	private final ModelPart left_arm;
	private final ModelPart left_arm_berries;
	private final ModelPart left_arm_honey;
	private final ModelPart right_arm;
	private final ModelPart right_arm_berries;
	private final ModelPart right_arm_honey;

	public BlackBearModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.skullRot = this.root.getChild("body").getChild("skullRot");
        ModelPart skull = this.skullRot.getChild("skull");
		this.head_angry = skull.getChild("head_angry");
		this.sleep = skull.getChild("sleep");
		this.snout = skull.getChild("snout");
		this.snout_berries = this.snout.getChild("snout_berries");
		this.snout_honey = this.snout.getChild("snout_honey");
		this.snout_angry = this.snout.getChild("snout_angry");
		this.front_legs = this.root.getChild("front_legs");
		this.left_arm = this.front_legs.getChild("left_arm");
		this.left_arm_berries = this.left_arm.getChild("left_arm_berries");
		this.left_arm_honey = this.left_arm.getChild("left_arm_honey");
		this.right_arm = this.front_legs.getChild("right_arm");
		this.right_arm_berries = this.right_arm.getChild("right_arm_berries");
		this.right_arm_honey = this.right_arm.getChild("right_arm_honey");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -0.8F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(33, 1).addBox(-6.0F, -6.2F, -9.4F, 12.0F, 12.0F, 19.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -12.8F, 0.2F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(71, 104).addBox(-6.5F, -13.0F, -6.5F, 11.0F, 1.0F, 11.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(1.0F, -0.7F, 3.4F, 1.5708F, 0.0F, 0.0F));
		PartDefinition bell = body.addOrReplaceChild("bell", CubeListBuilder.create(), PartPose.offset(0.0F, 3.8F, -9.9F));
		PartDefinition bell_r1 = bell.addOrReplaceChild("bell_r1", CubeListBuilder.create()
		.texOffs(116, 118).addBox(-1.5F, -15.0F, -5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 12.5F, 1.5708F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 2.6F, -9.4F));
		PartDefinition skull = skullRot.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.5F, -2.0F, -6.4F, 9.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.8F, 0.4F));
		PartDefinition head_angry = skull.addOrReplaceChild("head_angry", CubeListBuilder.create()
		.texOffs(0, 14).addBox(-4.5F, -17.0F, -16.0F, 9.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 9.6F));
		PartDefinition sleep = skull.addOrReplaceChild("sleep", CubeListBuilder.create()
		.texOffs(1, 1).addBox(-3.5F, -16.0F, -16.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).mirror().addBox(1.5F, -16.0F, -16.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 15.0F, 9.575F));
		PartDefinition left_ear = skull.addOrReplaceChild("left_ear", CubeListBuilder.create()
		.texOffs(25, 2).addBox(-0.22F, -1.36F, 0.12F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.72F, -1.64F, -3.52F));
		PartDefinition right_ear = skull.addOrReplaceChild("right_ear", CubeListBuilder.create()
		.texOffs(25, 2).mirror().addBox(-1.58F, -1.36F, 0.12F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.92F, -1.64F, -3.52F));
		PartDefinition snout = skull.addOrReplaceChild("snout", CubeListBuilder.create()
		.texOffs(0, 28).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -6.4F));
		PartDefinition rightItem = snout.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.92F, 0.4F, 0.52F, 0.2618F, 0.2618F, -0.192F));
		PartDefinition nose = snout.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(32, 0).addBox(-2.0F, -2.2F, -1.4F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(-0.5F, 0.2F, -2.6F));
		PartDefinition snout_berries = snout.addOrReplaceChild("snout_berries", CubeListBuilder.create()
		.texOffs(0, 48).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition snout_honey = snout.addOrReplaceChild("snout_honey", CubeListBuilder.create()
		.texOffs(0, 58).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition snout_angry = snout.addOrReplaceChild("snout_angry", CubeListBuilder.create()
		.texOffs(0, 38).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition front_legs = root.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.8F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(21, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -7.0F, -6.0F));
		PartDefinition left_arm_berries = left_arm.addOrReplaceChild("left_arm_berries", CubeListBuilder.create()
		.texOffs(20, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm_honey = left_arm.addOrReplaceChild("left_arm_honey", CubeListBuilder.create()
		.texOffs(20, 57).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(21, 33).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -7.0F, -6.0F));
		PartDefinition right_arm_berries = right_arm.addOrReplaceChild("right_arm_berries", CubeListBuilder.create()
		.texOffs(20, 45).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition right_arm_honey = right_arm.addOrReplaceChild("right_arm_honey", CubeListBuilder.create()
		.texOffs(20, 57).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition back_legs = root.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.8F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(77, 8).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -7.0F, 6.0F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(77, 8).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -7.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(BlackBear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sleeping = entity.isSleeping();
		boolean angry = (entity.isAngry() || entity.isAggressive()) && !entity.isBaby();
		boolean holdingBerries = entity.getMainHandItem().is(Items.SWEET_BERRIES);
		boolean holdingHoney = entity.getMainHandItem().is(Items.HONEYCOMB);
		boolean holdingVariant = holdingBerries || holdingHoney;

		this.sleep.visible = sleeping;
		this.head_angry.visible = !sleeping && angry;
		this.snout.skipDraw = holdingVariant || angry;
		this.snout_angry.visible = !holdingVariant && !sleeping && angry;
		this.left_arm.skipDraw = holdingVariant;
		this.right_arm.skipDraw = holdingVariant;
		this.snout_berries.visible = holdingBerries;
		this.left_arm_berries.visible = holdingBerries;
		this.right_arm_berries.visible = holdingBerries;
		this.snout_honey.visible = holdingHoney;
		this.left_arm_honey.visible = holdingHoney;
		this.right_arm_honey.visible = holdingHoney;

		this.animateSmooth(entity.sleepAnimationState, BlackBearAnimation.BEAR_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, BlackBearAnimation.BEAR_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sniffAnimationState, BlackBearAnimation.BEAR_SNIFF, ageInTicks, partialTick);
		this.animateSmooth(entity.eatAnimationState, BlackBearAnimation.BEAR_EAT, ageInTicks, partialTick);
		this.animateSmooth(entity.attackAnimationState, BlackBearAnimation.BEAR_ATTACK, ageInTicks, partialTick, 1.3F);

		this.animateIdleSmooth(entity.idleAnimationState, BlackBearAnimation.BEAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BlackBearAnimation.BEAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.8F));
		this.animateSmooth(entity.runAnimationState, BlackBearAnimation.BEAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));

		if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
			applyHeadLook(this.skullRot, netHeadYaw, headPitch);
		}
	}

	public void translateToRightArm(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.front_legs.translateAndRotate(poseStack);
		this.right_arm.translateAndRotate(poseStack);
	}
}
