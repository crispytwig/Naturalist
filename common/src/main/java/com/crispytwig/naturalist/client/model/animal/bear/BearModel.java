package com.crispytwig.naturalist.client.model.animal.bear;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BearAnimation;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
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

public class BearModel extends NaturalistEntityModel<Bear> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("bear"), "main");
	private final ModelPart root;
    private final ModelPart normalBody;
	private final ModelPart shearedBody;
	private final ModelPart neck;
	private final ModelPart angrySnout;
	private final ModelPart awake;
	private final ModelPart angry;
	private final ModelPart asleep;
	private final ModelPart legs;
	private final ModelPart rightArm;
    private final ModelPart honeyArm;
	private final ModelPart berryArm;
	private final ModelPart rightHand;

	public BearModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("butt").getChild("body");
		this.normalBody = body.getChild("normalBody");
		this.shearedBody = body.getChild("shearedBody");
		this.neck = body.getChild("skullRot").getChild("neck");
		this.angrySnout = this.neck.getChild("angrySnout");
		this.awake = this.neck.getChild("awake");
		this.angry = this.neck.getChild("angry");
		this.asleep = this.neck.getChild("asleep");
		this.legs = this.root.getChild("legs");
		this.rightArm = this.legs.getChild("rightArm");
        ModelPart saucyArm = this.rightArm.getChild("saucyArm");
		this.honeyArm = saucyArm.getChild("honeyArm");
		this.berryArm = saucyArm.getChild("berryArm");
		this.rightHand = this.rightArm.getChild("rightHand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 0.0F));
		PartDefinition butt = root.addOrReplaceChild("butt", CubeListBuilder.create(), PartPose.offset(1.0F, -15.0F, 11.0F));
		PartDefinition body = butt.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -12.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(64, 99).addBox(-8.5F, -13.0F, -6.5F, 17.0F, 1.0F, 15.0F, new CubeDeformation(0.3F))
		.texOffs(0, 0).addBox(-8.5F, -13.0F, -6.5F, 17.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 1.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition bell = body.addOrReplaceChild("bell", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -12.5F));
		PartDefinition bell_r1 = bell.addOrReplaceChild("bell_r1", CubeListBuilder.create()
		.texOffs(116, 118).addBox(-1.5F, -15.0F, -5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 12.5F, 1.5708F, 0.0F, 0.0F));
		PartDefinition normalBody = body.addOrReplaceChild("normalBody", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 1.0F));
		PartDefinition normalBody_r1 = normalBody.addOrReplaceChild("normalBody_r1", CubeListBuilder.create()
		.texOffs(0, 25).addBox(-7.5F, -3.0F, -6.5F, 15.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition shearedBody = body.addOrReplaceChild("shearedBody", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 1.0F));
		PartDefinition shearedBody_r1 = shearedBody.addOrReplaceChild("shearedBody_r1", CubeListBuilder.create()
		.texOffs(1, 68).addBox(-7.5F, -3.0F, -6.5F, 15.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 12.0F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(43, 30).addBox(-1.5F, -3.0F, 2.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 3.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 2.75F, -12.375F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition normalSnout = neck.addOrReplaceChild("normalSnout", CubeListBuilder.create()
		.texOffs(69, 0).addBox(-3.5F, -15.0F, -24.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 13.375F));
		PartDefinition angrySnout = neck.addOrReplaceChild("angrySnout", CubeListBuilder.create()
		.texOffs(69, 11).addBox(-3.5F, -15.0F, -24.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 13.375F));
		PartDefinition nose = neck.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(49, 0).addBox(-3.5F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -0.75F, -9.625F));
		PartDefinition awake = neck.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(92, 0).addBox(-5.5F, -19.0F, -20.0F, 11.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 13.375F));
		PartDefinition angry = neck.addOrReplaceChild("angry", CubeListBuilder.create()
		.texOffs(92, 36).addBox(-5.5F, -19.0F, -20.0F, 11.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 13.375F));
		PartDefinition asleep = neck.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(92, 18).addBox(-5.5F, -19.0F, -20.0F, 11.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 13.375F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(90, 0).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -6.25F, -4.125F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(90, 0).mirror().addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -6.25F, -4.125F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-3.0F, -9.0F, -9.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition normalArm = rightArm.addOrReplaceChild("normalArm", CubeListBuilder.create()
		.texOffs(50, 49).mirror().addBox(-6.5F, -9.0F, -12.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 9.0F, 9.0F));
		PartDefinition saucyArm = rightArm.addOrReplaceChild("saucyArm", CubeListBuilder.create()
		.texOffs(73, 49).mirror().addBox(-6.5F, -9.0F, -12.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 9.0F, 9.0F));
		PartDefinition honeyArm = saucyArm.addOrReplaceChild("honeyArm", CubeListBuilder.create()
		.texOffs(27, 52).mirror().addBox(-6.5F, -6.0F, -12.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition berryArm = saucyArm.addOrReplaceChild("berryArm", CubeListBuilder.create()
		.texOffs(4, 52).mirror().addBox(-6.5F, -6.0F, -12.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offset(-0.5F, 7.0F, -3.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(50, 49).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 16.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(50, 49).mirror().addBox(-2.5F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 16.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(50, 49).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Bear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sheared = entity.isSheared();
		this.normalBody.visible = !sheared;
		this.shearedBody.visible = sheared;

		boolean angry = entity.isAngry() || entity.isAggressive();
		boolean sleeping = entity.isSleeping() && !angry;
		this.awake.visible = !sleeping && !angry;
		this.asleep.visible = sleeping;
		this.angry.visible = angry;
		this.angrySnout.visible = angry;

        this.berryArm.visible = entity.isEating() && entity.getMainHandItem().is(Items.SWEET_BERRIES);
        this.honeyArm.visible = entity.isEating() && entity.getMainHandItem().is(Items.HONEYCOMB);

		this.animateSmooth(entity.sleepAnimationState, BearAnimation.BEAR_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, BearAnimation.BEAR_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sniffAnimationState, BearAnimation.BEAR_SNIFF, ageInTicks, partialTick);
		this.animateSmooth(entity.eatAnimationState, BearAnimation.BEAR_EAT, ageInTicks, partialTick);
		this.animateSmooth(entity.attackAnimationState, BearAnimation.BEAR_ATTACK, ageInTicks, partialTick, 1.3F);

		this.animateIdleSmooth(entity.idleAnimationState, BearAnimation.BEAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BearAnimation.BEAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.runAnimationState, BearAnimation.BEAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));

		if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
			applyHeadLook(this.neck, netHeadYaw, headPitch);
		}
	}

	public void translateToRightHand(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.legs.translateAndRotate(poseStack);
		this.rightArm.translateAndRotate(poseStack);
		this.rightHand.translateAndRotate(poseStack);
	}
}
