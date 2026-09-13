package com.crispytwig.naturalist.client.model.animal.equine;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ZebraAnimation;
import com.crispytwig.naturalist.world.entity.animal.equine.Zebra;
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

public class ZebraModel extends NaturalistEntityModel<Zebra> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("zebra"), "main");
	private final ModelPart root;
    private final ModelPart neck;
	private final ModelPart bridle;
	private final ModelPart reinsR;
	private final ModelPart reinsL;
	private final ModelPart chest;
	private final ModelPart saddle;

	public ZebraModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.neck = body.getChild("neck");
		this.bridle = this.neck.getChild("bridle");
		this.reinsR = this.neck.getChild("reinsR");
		this.reinsL = this.neck.getChild("reinsL");
		this.chest = body.getChild("chest");
		this.saddle = body.getChild("saddle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 32).addBox(-5.0F, -5.0F, -11.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -8.0F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(0, 35).addBox(-2.0F, -27.75F, -11.0F, 4.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(56, 36).addBox(-1.0F, -32.75F, -4.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-2.0F, -32.75F, -16.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-3.0F, -32.75F, -11.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.5059F, 15.3032F, 0.5236F, 0.0F, 0.0F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create(), PartPose.offset(1.5975F, -15.1003F, -4.6882F));
		PartDefinition leftEar_r1 = leftEar.addOrReplaceChild("leftEar_r1", CubeListBuilder.create()
		.texOffs(19, 14).mirror().addBox(1.0458F, -36.862F, -5.01F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.8696F, 24.2988F, 19.2366F, 0.5087F, -0.1298F, 0.228F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create(), PartPose.offset(-1.5975F, -15.1003F, -4.6882F));
		PartDefinition rightEar_r1 = rightEar.addOrReplaceChild("rightEar_r1", CubeListBuilder.create()
		.texOffs(19, 14).addBox(-3.0458F, -36.862F, -5.01F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.8696F, 24.2988F, 19.2366F, 0.5087F, 0.1298F, -0.228F));
		PartDefinition bridle = neck.addOrReplaceChild("bridle", CubeListBuilder.create(), PartPose.offset(0.0F, 10.7224F, 15.4282F));
		PartDefinition bridle_r1 = bridle.addOrReplaceChild("bridle_r1", CubeListBuilder.create()
		.texOffs(19, 0).addBox(-2.0F, -32.75F, -12.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.05F))
		.texOffs(29, 5).addBox(2.0F, -30.75F, -13.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(29, 5).mirror().addBox(-3.0F, -30.75F, -13.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(1, 1).addBox(-3.0F, -32.75F, -11.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, -0.2165F, -0.125F, 0.5236F, 0.0F, 0.0F));
		PartDefinition reinsR = neck.addOrReplaceChild("reinsR", CubeListBuilder.create(), PartPose.offset(-3.1F, -9.4413F, -10.6471F));
		PartDefinition reinsR_r1 = reinsR.addOrReplaceChild("reinsR_r1", CubeListBuilder.create()
		.texOffs(32, 2).mirror().addBox(-3.1F, -31.75F, -12.0F, 0.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.1F, 31.2243F, 9.816F, -0.0873F, 0.0F, 0.0F));
		PartDefinition reinsL = neck.addOrReplaceChild("reinsL", CubeListBuilder.create(), PartPose.offset(3.1F, -9.4413F, -10.6471F));
		PartDefinition reinsL_r1 = reinsL.addOrReplaceChild("reinsL_r1", CubeListBuilder.create()
		.texOffs(32, 2).addBox(3.1F, -31.75F, -12.0F, 0.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1F, 31.2243F, 9.816F, -0.0873F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 11.0F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(47, 39).addBox(-0.5F, -20.0F, 10.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(45, 47).addBox(-1.0F, -13.0F, 10.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.8205F, 0.4737F, 0.5236F, 0.0F, 0.0F));
		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition chest_left = chest.addOrReplaceChild("chest_left", CubeListBuilder.create()
		.texOffs(26, 21).mirror().addBox(-7.0F, 7.0F, 0.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -12.0F, 3.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition chest_right = chest.addOrReplaceChild("chest_right", CubeListBuilder.create()
		.texOffs(26, 21).mirror().addBox(-8.0F, 7.0F, 0.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -12.0F, 10.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(26, 0).mirror().addBox(-5.0F, -4.5F, -4.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.5F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
		PartDefinition frontLegs = legs.addOrReplaceChild("frontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition rightArm = frontLegs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(48, 21).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.9F, -11.0F, -9.0F));
		PartDefinition leftArm = frontLegs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(48, 21).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.9F, -11.0F, -9.0F));
		PartDefinition backLegs = legs.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition leftLeg = backLegs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(48, 21).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.9F, -11.0F, 9.0F));
		PartDefinition rightLeg = backLegs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(48, 21).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.9F, -11.0F, 9.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Zebra entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.saddle.visible = entity.isSaddled();

		boolean saddled = entity.isTamed() && entity.isSaddled();
		this.bridle.visible = saddled;
		this.reinsR.visible = saddled && entity.isVehicle();
		this.reinsL.visible = saddled && entity.isVehicle();

		this.chest.visible = entity.hasChest();

		this.animateIdleSmooth(entity.idleAnimationState, ZebraAnimation.ZEBRA_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, ZebraAnimation.ZEBRA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.runAnimationState, ZebraAnimation.ZEBRA_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
