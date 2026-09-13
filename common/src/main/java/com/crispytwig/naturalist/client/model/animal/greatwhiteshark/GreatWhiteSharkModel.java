package com.crispytwig.naturalist.client.model.animal.greatwhiteshark;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.GreatWhiteSharkAnimation;
import com.crispytwig.naturalist.world.entity.animal.greatwhiteshark.GreatWhiteShark;
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

public class GreatWhiteSharkModel extends NaturalistEntityModel<GreatWhiteShark> {
	private static final float HEAD_ROLL_STABILIZE = 0.35F;
	private static final float FLUKE_ROLL_FOLLOW = 0.4F;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("great_white_shark"), "main");
	private final ModelPart root;
    private final ModelPart skullRot;
	private final ModelPart tail;
    private final ModelPart fluke;

	public GreatWhiteSharkModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skullRot = body.getChild("skullRot");
		this.tail = body.getChild("tail");
        ModelPart tail2 = this.tail.getChild("tail2");
		this.fluke = tail2.getChild("fluke");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-10.0F, -6.5F, -17.0F, 20.0F, 21.0F, 40.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -15.5F, -11.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3333F, -16.8333F));
		PartDefinition topJaw = skullRot.addOrReplaceChild("topJaw", CubeListBuilder.create()
		.texOffs(54, 77).addBox(-7.0F, -2.0F, -21.0F, 14.0F, 11.0F, 22.0F, new CubeDeformation(0.04F))
		.texOffs(68, 114).addBox(-7.0F, 1.0F, -10.0F, 14.0F, 8.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(43, 138).addBox(-17.0F, 1.0F, -21.0F, 34.0F, 8.0F, 11.0F, new CubeDeformation(0.02F))
		.texOffs(80, 26).addBox(-6.0F, 8.0F, -20.0F, 12.0F, 2.0F, 10.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -4.8333F, -0.1667F));
		PartDefinition bottomJaw = skullRot.addOrReplaceChild("bottomJaw", CubeListBuilder.create()
		.texOffs(104, 75).addBox(-6.0F, -0.1667F, -12.1667F, 12.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(114, 1).addBox(-5.0F, -1.1667F, -11.1667F, 10.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(7, 106).addBox(-7.0F, -0.1667F, -3.1667F, 14.0F, 5.0F, 3.0F, new CubeDeformation(0.02F))
		.texOffs(1, 132).addBox(-7.0F, -0.0809F, -0.1809F, 14.0F, 5.0F, 7.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 4.2476F, -5.9858F, 0.0873F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 61).addBox(-7.0F, -6.1048F, -4.9323F, 14.0F, 14.0F, 24.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 2.6048F, 22.9323F));
		PartDefinition bottomFin = tail.addOrReplaceChild("bottomFin", CubeListBuilder.create()
		.texOffs(0, 61).addBox(-1.0F, -0.5F, 0.2F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 8.3952F, 6.0677F, 0.9599F, 0.0F, 0.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(80, 0).addBox(-3.0F, -5.0F, -2.0F, 6.0F, 9.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.1048F, 19.0677F));
		PartDefinition fluke = tail2.addOrReplaceChild("fluke", CubeListBuilder.create()
		.texOffs(52, 61).addBox(-13.0F, -1.5F, 0.0F, 28.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 13.0F, 0.0F, 0.0F, -1.5708F));
		PartDefinition backFin = body.addOrReplaceChild("backFin", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, 14.0F, -16.5F, 2.0F, 23.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -25.5F, 22.5F, -0.3491F, 0.0F, 0.0F));
		PartDefinition rightFin = root.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(37, 103).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 21.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -7.0F, -14.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition leftFin = root.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(37, 103).mirror().addBox(-1.0F, 0.0F, -5.0F, 2.0F, 21.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(10.0F, -7.0F, -14.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 160, 160);
	}

	@Override
	protected void setupAnimations(GreatWhiteShark entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, GreatWhiteSharkAnimation.SHARK_ATTACK, ageInTicks, partialTick);

		this.animateSmooth(entity.flopAnimationState, GreatWhiteSharkAnimation.SHARK_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.idleAnimationState, GreatWhiteSharkAnimation.SHARK_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, GreatWhiteSharkAnimation.SHARK_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.swimFastAnimationState, GreatWhiteSharkAnimation.SHARK_SWIM_FAST, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));

		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;

		this.rotatePart(this.root, entity.getXBodyRot(partialTick) * Mth.DEG_TO_RAD, 0.0F, roll);
		this.bend(this.skullRot, entity, 0, partialTick);
		this.bend(this.tail, entity, 1, partialTick);
		this.bend(this.fluke, entity, 2, partialTick);
		this.rotatePart(this.skullRot, 0.0F, 0.0F, -roll * HEAD_ROLL_STABILIZE);
		this.rotatePart(this.fluke, 0.0F, 0.0F, roll * FLUKE_ROLL_FOLLOW);
	}
}
