package com.crispytwig.naturalist.client.model.animal.whale;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.WhaleAnimation;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
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

public class WhaleModel extends NaturalistEntityModel<Whale> {
	private static final float FIN_COUNTER_GAIN = 1.6F;
	private static final float HEAD_ROLL_STABILIZE = 0.35F;
	private static final float FLUKE_ROLL_FOLLOW = 0.4F;
	private static final float HEAD_DROOP_ANGLE = (float) Math.toRadians(-45.0D);
	private static final float TAIL_DROOP_ANGLE = (float) Math.toRadians(90.0D);

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("whale"), "main");
	private final ModelPart root;
    private final ModelPart skullRot;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart fluke;
	private final ModelPart rightFin;
	private final ModelPart leftFin;

	public WhaleModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skullRot = body.getChild("skullRot");
		this.tail = body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.fluke = this.tail2.getChild("fluke");
		this.rightFin = this.root.getChild("rightFin");
		this.leftFin = this.root.getChild("leftFin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-17.0F, -18.5F, -25.0F, 34.0F, 36.0F, 58.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.5F, -14.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create()
		.texOffs(184, 53).addBox(-14.0F, -1.8333F, -12.9167F, 28.0F, 16.0F, 13.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -5.6667F, -25.0833F));
		PartDefinition topJaw = skullRot.addOrReplaceChild("topJaw", CubeListBuilder.create()
		.texOffs(126, 0).addBox(-14.0F, -10.0F, -37.0F, 28.0F, 11.0F, 42.0F, new CubeDeformation(0.0F))
		.texOffs(161, 186).addBox(-12.0F, 1.0F, -35.0F, 24.0F, 8.0F, 0.0F, new CubeDeformation(-0.02F))
		.texOffs(128, 134).addBox(-12.0F, 1.0F, -35.0F, 0.0F, 8.0F, 42.0F, new CubeDeformation(-0.02F))
		.texOffs(128, 134).mirror().addBox(12.0F, 1.0F, -35.0F, 0.0F, 8.0F, 42.0F, new CubeDeformation(-0.02F)).mirror(false), PartPose.offset(0.0F, -2.8333F, -4.9167F));
		PartDefinition bottomJaw = skullRot.addOrReplaceChild("bottomJaw", CubeListBuilder.create()
		.texOffs(158, 227).addBox(-14.0F, 0.0F, -29.0F, 28.0F, 16.0F, 29.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-12.0F, 0.0F, -27.0F, 0.0F, 5.0F, 27.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).mirror().addBox(12.0F, 0.0F, -27.0F, 0.0F, 5.0F, 27.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 8).addBox(-12.0F, 0.0F, -27.0F, 24.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 0.0F, -27.0F, 0.0F, 6.0F, 29.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(8.0F, 0.0F, -27.0F, 0.0F, 6.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(96, 127).addBox(-12.0F, 5.0F, -27.0F, 24.0F, 0.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.8333F, -12.9167F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(3, 141).addBox(-13.0F, -11.1048F, -3.9323F, 26.0F, 26.0F, 46.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -2.3952F, 32.9323F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(16, 220).addBox(-9.0F, -8.0F, -4.0F, 18.0F, 15.0F, 37.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.1048F, 42.0677F));
		PartDefinition fluke = tail2.addOrReplaceChild("fluke", CubeListBuilder.create()
		.texOffs(94, 94).addBox(-28.0F, -2.5F, -4.0F, 56.0F, 5.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 26.0F));
		PartDefinition bone = tail.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, -3.6048F, 19.5677F));
		PartDefinition bone_r1 = bone.addOrReplaceChild("bone_r1", CubeListBuilder.create()
		.texOffs(184, 127).addBox(-2.0F, -15.5F, -15.5F, 4.0F, 11.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition rightFin = root.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(226, 142).addBox(-1.5F, 0.0F, -10.0F, 3.0F, 47.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -9.5F, -14.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition leftFin = root.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(226, 142).mirror().addBox(-1.5F, 0.0F, -10.0F, 3.0F, 47.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(17.0F, -9.5F, -14.0F, 0.0F, 0.0F, -1.5708F));
		PartDefinition particle = root.addOrReplaceChild("particle", CubeListBuilder.create(), PartPose.offset(0.0F, -34.0F, -51.0F));

		return LayerDefinition.create(meshdefinition, 272, 272);
	}

	@Override
	protected void setupAnimations(Whale entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flopAnimationState, WhaleAnimation.WHALE_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.idleAnimationState, WhaleAnimation.WHALE_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, WhaleAnimation.WHALE_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));

		float bodyPitch = entity.getXBodyRot(partialTick);
		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;
		float finCounter = (bodyPitch - entity.getFinLag(partialTick)) * Mth.DEG_TO_RAD * FIN_COUNTER_GAIN;
		float droopF = entity.getFrontDroop(partialTick) * HEAD_DROOP_ANGLE;
		float droopB = entity.getBackDroop(partialTick) * TAIL_DROOP_ANGLE;

		this.rotatePart(this.root, bodyPitch * Mth.DEG_TO_RAD, 0.0F, roll);
		this.bend(this.skullRot, entity, 0, partialTick);
		this.bend(this.tail, entity, 1, partialTick);
		this.bend(this.tail2, entity, 2, partialTick);
		this.bend(this.fluke, entity, 3, partialTick);
		this.rotatePart(this.leftFin, -finCounter, 0.0F, 0.0F);
		this.rotatePart(this.rightFin, -finCounter, 0.0F, 0.0F);
		this.rotatePart(this.skullRot, droopF, 0.0F, -roll * HEAD_ROLL_STABILIZE);
		this.rotatePart(this.tail, droopB, 0.0F, 0.0F);
		this.rotatePart(this.tail2, droopB, 0.0F, 0.0F);
		this.rotatePart(this.fluke, droopB, 0.0F, roll * FLUKE_ROLL_FOLLOW);
	}
}
