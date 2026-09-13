package com.crispytwig.naturalist.client.model.animal.ostrich;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.OstrichAnimation;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.model.SeatedModel;

public class OstrichModel extends NaturalistEntityModel<Ostrich> implements SeatedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("ostrich"), "main");
	private final ModelPart modelRoot;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart saddle;
	private final ModelPart skull;
	private final ModelPart seat;

	public OstrichModel(ModelPart root) {
		super(root);
		this.modelRoot = root;
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.saddle = this.body.getChild("saddle");
		this.skull = this.body.getChild("skull");
		this.seat = this.body.getChild("seat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -2.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -6.5F, -8.0F, 10.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.5F, 2.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(63, 0).addBox(-5.0F, -28.0F, -3.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 21.5F, -4.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(1, 75).addBox(-0.5F, -17.0F, -3.0F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 74).addBox(-0.5F, -16.0F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.5F, -8.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(2, 32).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(17, 49).addBox(-3.0F, -4.0F, 7.0F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 5.0F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(35, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(60, 39).addBox(-1.0F, 0.0F, 9.0F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(6.0F, -0.5F, -2.0F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(35, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 39).mirror().addBox(-1.0F, 0.0F, 9.0F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-6.0F, -0.5F, -2.0F));
		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(2.75F, -14.5F, 3.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(54, 20).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot", CubeListBuilder.create()
		.texOffs(64, 20).mirror().addBox(-2.0F, -0.5F, -4.5F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offset(0.0F, 14.0F, 0.5F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(54, 20).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 0.0F, 0.0F));
		PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot", CubeListBuilder.create()
		.texOffs(64, 20).addBox(-2.0F, -0.5F, -4.5F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 14.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Ostrich entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.saddle.visible = entity.isSaddled();

		this.animateSmooth(entity.attackAnimationState, OstrichAnimation.OSTRICH_ATTACK, ageInTicks, partialTick);

		this.animateSmooth(entity.sitAnimationState, OstrichAnimation.OSTRICH_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.flapAnimationState, OstrichAnimation.OSTRICH_FLAP, ageInTicks, partialTick);
		this.animateSmooth(entity.buryAnimationState, OstrichAnimation.OSTRICH_BURY_HEAD, ageInTicks, partialTick);
		this.animateIdleSmooth(entity.idleAnimationState, OstrichAnimation.OSTRICH_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, OstrichAnimation.OSTRICH_WALK_B, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.4F, 1.0D));
		this.animateSmooth(entity.runAnimationState, OstrichAnimation.OSTRICH_RUN_B_1, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F, 1.0D));

		if (entity.isVehicle() && entity.getControllingPassenger() instanceof Player player) {
			this.skull.xRot += Mth.clamp(player.getXRot(), -30.0F, 60.0F) * Mth.DEG_TO_RAD;
			this.skull.yRot += Mth.clamp(Mth.wrapDegrees(player.yHeadRot - entity.yBodyRot), -60.0F, 60.0F) * Mth.DEG_TO_RAD;
		} else if (entity.isBaby() || !entity.canHide()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}

	public void translateToSeat(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		this.seat.translateAndRotate(poseStack);
	}

	@Override
	public float seatZRot() {
		return this.root.zRot + this.body.zRot + this.seat.zRot;
	}
}
