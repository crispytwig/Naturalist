package com.crispytwig.naturalist.client.model.animal.elephant;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ElephantAnimation;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
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

public class ElephantModel extends IKEntityModel<Elephant> implements SeatedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("elephant"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart seat;
	private final ModelPart saddle;
	private final ModelPart saddleFront;
	private final ModelPart chests;
	private final ModelPart leftChest;
	private final ModelPart rightChest;
	private final ModelPart neck;
    private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public ElephantModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.seat = this.body.getChild("seat");
		this.saddle = this.body.getChild("saddle");
		this.saddleFront = this.saddle.getChild("saddleFront");
		this.chests = this.body.getChild("chests");
		this.leftChest = this.chests.getChild("leftChest");
		this.rightChest = this.chests.getChild("rightChest");
		this.neck = this.body.getChild("skullRot").getChild("attack").getChild("neck");
        ModelPart legs = this.root.getChild("legs");
		this.leftArm = legs.getChild("leftArm");
		this.rightArm = legs.getChild("rightArm");
		this.leftLeg = legs.getChild("leftLeg");
		this.rightLeg = legs.getChild("rightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 25.0F, -3.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-14.0F, -15.0F, -24.0F, 28.0F, 30.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -40.0F, 3.0F));
		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, -2.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(0, 123).addBox(-14.0F, -15.0F, -24.0F, 28.0F, 30.0F, 48.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition saddleFront = saddle.addOrReplaceChild("saddleFront", CubeListBuilder.create(), PartPose.offset(0.0F, -15.0F, -15.75F));
		PartDefinition chests = body.addOrReplaceChild("chests", CubeListBuilder.create(), PartPose.offset(0.0F, 30.75F, 1.0F));
		PartDefinition leftChest = chests.addOrReplaceChild("leftChest", CubeListBuilder.create()
		.texOffs(177, 6).addBox(14.0F, -35.0F, 3.0F, 4.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightChest = chests.addOrReplaceChild("rightChest", CubeListBuilder.create()
		.texOffs(177, 6).mirror().addBox(-18.0F, -35.0F, 3.0F, 4.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(144, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 24.0F, -1.4399F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -22.3333F));
		PartDefinition attack = skullRot.addOrReplaceChild("attack", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck = attack.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 79).addBox(-10.0F, -17.0F, -15.6667F, 20.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(79, 92).addBox(-21.0F, -9.0F, -1.0F, 21.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -11.0F, -6.6667F, 0.0F, 0.3927F, 0.2182F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(79, 92).mirror().addBox(0.0F, -9.0F, -1.0F, 21.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, -11.0F, -6.6667F, 0.0F, -0.3927F, -0.2182F));
		PartDefinition tusks = neck.addOrReplaceChild("tusks", CubeListBuilder.create(), PartPose.offset(8.0F, 7.5F, -15.6667F));
		PartDefinition left_tusk = tusks.addOrReplaceChild("left_tusk", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition left_tusk_r1 = left_tusk.addOrReplaceChild("left_tusk_r1", CubeListBuilder.create()
		.texOffs(106, 0).addBox(-3.0F, -7.5F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		PartDefinition left_tusk_r2 = left_tusk.addOrReplaceChild("left_tusk_r2", CubeListBuilder.create()
		.texOffs(107, 17).addBox(-2.0F, 4.5F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.02F))
		.texOffs(122, 8).addBox(-2.0F, 19.5F, -11.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.5F, -0.0873F, 0.0F, 0.0F));
		PartDefinition right_tusk = tusks.addOrReplaceChild("right_tusk", CubeListBuilder.create(), PartPose.offsetAndRotation(-16.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition right_tusk_r1 = right_tusk.addOrReplaceChild("right_tusk_r1", CubeListBuilder.create()
		.texOffs(106, 0).mirror().addBox(-3.0F, -7.5F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		PartDefinition right_tusk_r2 = right_tusk.addOrReplaceChild("right_tusk_r2", CubeListBuilder.create()
		.texOffs(107, 17).mirror().addBox(-2.0F, 4.5F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.02F)).mirror(false)
		.texOffs(122, 8).mirror().addBox(-2.0F, 19.5F, -11.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.0F, 0.5F, -0.0873F, 0.0F, 0.0F));
		PartDefinition trunk = neck.addOrReplaceChild("trunk", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -5.0F, -8.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -13.6667F));
		PartDefinition trunk2 = trunk.addOrReplaceChild("trunk2", CubeListBuilder.create()
		.texOffs(0, 25).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -3.0F));
		PartDefinition trunk3 = trunk2.addOrReplaceChild("trunk3", CubeListBuilder.create()
		.texOffs(124, 22).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		PartDefinition trunk4 = trunk3.addOrReplaceChild("trunk4", CubeListBuilder.create()
		.texOffs(150, 21).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(128, 82).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 26.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -27.0F, -13.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(128, 82).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 26.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -27.0F, -13.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(128, 82).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 26.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -27.0F, 19.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(128, 82).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 26.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -27.0F, 19.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	protected void setupAnimations(Elephant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean saddled = entity.isSaddled();
		this.saddle.visible = saddled;
		this.saddleFront.visible = saddled;

		boolean chested = entity.isChested();
		this.chests.visible = chested;
		this.leftChest.visible = chested;
		this.rightChest.visible = chested;

		this.animateSmooth(entity.swingAnimationState, ElephantAnimation.ELEPHANT_SWING, ageInTicks, partialTick);

		float tuned = entity.isBaby() || entity.getTarget() != null || entity.isVehicle() ? 4.4F : 2.9F;
		this.animateIdleSmooth(entity.idleAnimationState, ElephantAnimation.ELEPHANT_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, ElephantAnimation.ELEPHANT_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, tuned));
		this.animateSmooth(entity.runAnimationState, ElephantAnimation.ELEPHANT_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, tuned));

		this.articulateLegs(entity, partialTick);

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}

	@Override
	protected TerrainLegSolver getLegSolver(Elephant entity) {
		return entity.legSolver;
	}

	@Override
	protected ModelPart bodyPart() {
		return this.body;
	}

	@Override
	protected ModelPart headPart() {
		return this.neck;
	}

	@Override
	protected ModelPart[] legParts() {
		return new ModelPart[]{this.leftLeg, this.rightLeg, this.leftArm, this.rightArm};
	}

	@Override
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
