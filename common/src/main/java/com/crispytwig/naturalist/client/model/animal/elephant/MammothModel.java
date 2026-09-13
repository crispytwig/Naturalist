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

public class MammothModel extends IKEntityModel<Elephant> implements SeatedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("mammoth"), "main");
	private final ModelPart root;
	private final ModelPart body;
    private final ModelPart neck;
	private final ModelPart seat;
	private final ModelPart saddle;
	private final ModelPart chests;
	private final ModelPart leftChest;
	private final ModelPart rightChest;
    private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public MammothModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
        ModelPart skullRot = this.body.getChild("skullRot");
        ModelPart attack = skullRot.getChild("attack");
		this.neck = attack.getChild("neck");
		this.seat = this.body.getChild("seat");
		this.saddle = this.body.getChild("saddle");
		this.chests = this.body.getChild("chests");
		this.leftChest = this.chests.getChild("leftChest");
		this.rightChest = this.chests.getChild("rightChest");
        ModelPart legs = this.root.getChild("legs");
		this.leftArm = legs.getChild("leftArm");
		this.rightArm = legs.getChild("rightArm");
		this.leftLeg = legs.getChild("leftLeg");
		this.rightLeg = legs.getChild("rightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, -3.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-15.0F, -18.0F, -25.0F, 30.0F, 38.0F, 54.0F, new CubeDeformation(0.0F))
		.texOffs(168, 64).addBox(-12.0F, -24.0F, -21.0F, 24.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 92).addBox(-15.0F, 20.0F, -25.0F, 30.0F, 6.0F, 54.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -40.0F, 3.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 29.2F, -1.4399F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -22.3333F));
		PartDefinition attack = skullRot.addOrReplaceChild("attack", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck = attack.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 216).addBox(-10.0F, -17.0F, -15.6667F, 20.0F, 22.0F, 18.0F, new CubeDeformation(0.52F))
		.texOffs(114, 0).addBox(-10.0F, -17.0F, -15.6667F, 20.0F, 24.0F, 18.0F, new CubeDeformation(0.02F))
		.texOffs(4, 29).addBox(-6.0F, -22.0F, -11.6667F, 12.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(35, 0).addBox(-4.915F, -27.9F, -57.0872F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(114, 92).addBox(-9.915F, -27.9F, -57.0872F, 5.0F, 4.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(32, 152).addBox(-9.915F, -43.9F, -39.0872F, 5.0F, 20.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 152).addBox(-11.915F, -54.9F, -40.0872F, 9.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6285F, 58.7665F, -3.4972F, -0.3927F, 0.3927F, 0.0F));
		PartDefinition neck_r2 = neck.addOrReplaceChild("neck_r2", CubeListBuilder.create()
		.texOffs(35, 0).mirror().addBox(-0.085F, -27.9F, -57.0872F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(114, 92).mirror().addBox(4.915F, -27.9F, -57.0872F, 5.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(32, 152).mirror().addBox(4.915F, -43.9F, -39.0872F, 5.0F, 20.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 152).mirror().addBox(2.915F, -54.9F, -40.0872F, 9.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.6285F, 58.7665F, -3.4972F, -0.3927F, -0.3927F, 0.0F));
		PartDefinition trunk = neck.addOrReplaceChild("trunk", CubeListBuilder.create()
		.texOffs(114, 114).addBox(-6.0F, -5.0F, -8.0F, 12.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -13.6667F));
		PartDefinition trunk2 = trunk.addOrReplaceChild("trunk2", CubeListBuilder.create()
		.texOffs(142, 92).addBox(-5.0F, 0.0F, -4.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -3.0F));
		PartDefinition trunk3 = trunk2.addOrReplaceChild("trunk3", CubeListBuilder.create()
		.texOffs(80, 152).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		PartDefinition trunk4 = trunk3.addOrReplaceChild("trunk4", CubeListBuilder.create()
		.texOffs(52, 152).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(0.0F, -6.0F, -1.0F, 11.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, -9.0F, -6.6667F, 0.0F, -0.3927F, -0.2182F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-11.0F, -6.0F, -1.0F, 11.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -9.0F, -6.6667F, 0.0F, 0.3927F, 0.2182F));
		PartDefinition seat = body.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offset(0.0F, -16.5F, 4.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(103, 206).addBox(-8.0F, -35.25F, -11.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.5F))
		.texOffs(99, 171).addBox(-14.0F, -30.5F, -48.75F, 28.0F, 36.0F, 48.0F, new CubeDeformation(1.5F))
		.texOffs(176, 94).addBox(-12.0F, -37.499F, -49.7064F, 24.0F, 12.0F, 16.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 13.4991F, 28.7064F));
		PartDefinition chests = body.addOrReplaceChild("chests", CubeListBuilder.create(), PartPose.offset(0.0F, 28.75F, 6.0F));
		PartDefinition leftChest = chests.addOrReplaceChild("leftChest", CubeListBuilder.create()
		.texOffs(193, 7).addBox(14.0F, -35.0F, 3.0F, 4.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));
		PartDefinition rightChest = chests.addOrReplaceChild("rightChest", CubeListBuilder.create()
		.texOffs(193, 7).mirror().addBox(-18.0F, -35.0F, 3.0F, 4.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(0, 92).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -27.0F, -13.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(0, 92).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -27.0F, -13.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(0, 92).addBox(-5.0F, -6.0F, -1.0F, 10.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -27.0F, 19.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(0, 92).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 32.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -27.0F, 24.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	protected void setupAnimations(Elephant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.saddle.visible = entity.isSaddled();

		boolean chested = entity.isChested();
		this.chests.visible = chested;
		this.leftChest.visible = chested;
		this.rightChest.visible = chested;

		this.animateSmooth(entity.swingAnimationState, ElephantAnimation.ELEPHANT_SWING, ageInTicks, partialTick);

		float tuned = entity.isBaby() || entity.getTarget() != null || entity.isVehicle() ? 3.0F : 2.5F;
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
}
