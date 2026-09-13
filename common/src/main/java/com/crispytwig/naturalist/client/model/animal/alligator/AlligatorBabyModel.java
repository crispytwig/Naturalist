package com.crispytwig.naturalist.client.model.animal.alligator;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.AlligatorBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
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

public class AlligatorBabyModel extends NaturalistEntityModel<Alligator> {
	private static final float HEAD_COUNTER = 0.5F;
	private static final float LIMB_COUNTER = 0.35F;
	private static final float LIMB_LIFT = 3.5F;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("alligator_baby"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart tail;
	private final ModelPart tailTip;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public AlligatorBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.tail = this.body.getChild("tail");
		this.tailTip = this.tail.getChild("tailTip");
		ModelPart legs = this.root.getChild("legs");
		this.leftArm = legs.getChild("leftArm");
		this.rightArm = legs.getChild("rightArm");
		this.leftLeg = legs.getChild("leftLeg");
		this.rightLeg = legs.getChild("rightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(24, 6).addBox(0.5F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 3.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(24, 6).addBox(0.5F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, -3.5F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(24, 6).mirror().addBox(-5.5F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, -3.5F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(24, 6).mirror().addBox(-5.5F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, 3.5F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -1.5F, -4.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.026F))
		.texOffs(18, 11).addBox(2.0F, -3.5F, -2.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(18, 11).addBox(-2.0F, -3.5F, -2.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-3.0F, -2.0F, -3.5F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 26).addBox(0.0F, -3.0F, -3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 22).addBox(-2.0F, -2.0F, -7.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 26).mirror().addBox(-3.0F, -3.0F, -3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(1, 28).addBox(-2.0F, -1.0F, -5.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.5F, -3.5F));
		PartDefinition jaw = neck.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(25, 1).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -3.5F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 18).addBox(-1.0F, -1.5F, 0.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(18, 11).addBox(1.0F, -3.5F, 0.5F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(18, 11).addBox(-1.0F, -3.5F, 0.5F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.5F));
		PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create()
		.texOffs(16, 22).addBox(0.0F, -3.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Alligator entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, AlligatorBabyAnimation.ALLIGATOR_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.6F);
		this.animateSmooth(entity.swimAnimationState, AlligatorBabyAnimation.ALLIGATOR_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 5.0F, LARGE_SWIMMER_LIMB_SWING, 0.1F));
		this.animateSmooth(entity.walkAnimationState, AlligatorBabyAnimation.ALLIGATOR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 5.0F));

		float pitch = entity.getXBodyRot(partialTick) * Mth.DEG_TO_RAD;
		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;

		this.rotatePart(this.body, pitch, 0.0F, roll);
		this.rotatePart(this.neck, -pitch * HEAD_COUNTER, 0.0F, -roll * HEAD_COUNTER);
		this.rotatePart(this.tail, entity.getSegmentPitchOffset(1, partialTick) * Mth.DEG_TO_RAD, entity.getSegmentYawOffset(1, partialTick) * Mth.DEG_TO_RAD, 0.0F);
		this.rotatePart(this.tailTip, entity.getSegmentPitchOffset(2, partialTick) * Mth.DEG_TO_RAD, entity.getSegmentYawOffset(2, partialTick) * Mth.DEG_TO_RAD, 0.0F);

		float limbPitch = pitch * LIMB_COUNTER;
		float limbRoll = -roll * LIMB_COUNTER;
		float limbLift = Mth.sin(pitch) * LIMB_LIFT;
		this.rotatePart(this.leftArm, limbPitch, 0.0F, limbRoll);
		this.rotatePart(this.rightArm, limbPitch, 0.0F, limbRoll);
		this.rotatePart(this.leftLeg, limbPitch, 0.0F, limbRoll);
		this.rotatePart(this.rightLeg, limbPitch, 0.0F, limbRoll);
		this.leftArm.y += limbLift;
		this.rightArm.y += limbLift;
		this.leftLeg.y -= limbLift;
		this.rightLeg.y -= limbLift;

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
