package com.crispytwig.naturalist.client.model.animal.alligator;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.AlligatorAnimation;
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

public class AlligatorModel extends NaturalistEntityModel<Alligator> {
	private static final float HEAD_COUNTER = 0.5F;
	private static final float LIMB_COUNTER = 0.35F;
	private static final float LIMB_LIFT = 11.0F;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("alligator"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public AlligatorModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		ModelPart legs = this.root.getChild("legs");
		this.leftArm = legs.getChild("left_arm");
		this.rightArm = legs.getChild("right_arm");
		this.leftLeg = legs.getChild("left_leg");
		this.rightLeg = legs.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-6.5F, -4.0F, -12.0F, 13.0F, 9.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-5.5F, -5.0F, -10.0F, 11.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
		.texOffs(41, 34).addBox(-2.5F, -5.0F, -10.0F, 5.0F, 1.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 6.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(50, 0).addBox(-3.5F, -1.0F, -2.0F, 7.0F, 8.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(65, 54).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 14.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(23, 54).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 8.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 65).addBox(-1.5F, -7.0F, 0.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 11.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(76, 75).addBox(-4.5F, -3.0F, -6.0F, 9.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(0.5F, -5.0F, -6.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).mirror().addBox(-4.5F, -5.0F, -6.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 0).addBox(-4.5F, 1.0F, -6.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -12.0F));
		PartDefinition snout = neck.addOrReplaceChild("snout", CubeListBuilder.create()
		.texOffs(42, 54).addBox(-4.5F, -4.0F, -7.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(91, 14).addBox(-4.5F, -5.0F, -7.0F, 9.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 37).addBox(-4.5F, 0.0F, -7.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 1.0F, -8.0F));
		PartDefinition lower_jaw = snout.addOrReplaceChild("lower_jaw", CubeListBuilder.create()
		.texOffs(46, 70).addBox(-4.5F, 0.0F, -9.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(84, 23).addBox(-4.5F, -2.0F, -9.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 2.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(7.0F, -7.0F, -5.0F));
		PartDefinition left_arm = legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-0.5F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create()
		.texOffs(0, 53).addBox(-6.0F, 0.0F, -7.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 7.0F, -1.0F));
		PartDefinition right_arm = legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-4.5F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-14.0F, 0.0F, 0.0F));
		PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create()
		.texOffs(0, 53).mirror().addBox(-6.0F, 0.0F, -7.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 7.0F, -1.0F));
		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-0.5F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 22.0F));
		PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create()
		.texOffs(0, 53).addBox(-4.0F, 0.0F, -8.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 7.0F, -1.0F));
		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-4.5F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-14.0F, 0.0F, 22.0F));
		PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create()
		.texOffs(0, 53).mirror().addBox(-8.0F, 0.0F, -8.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 7.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Alligator entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.biteAnimationState, AlligatorAnimation.ALLIGATOR_BITE, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, AlligatorAnimation.ALLIGATOR_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.6F);
		this.animateSmooth(entity.swimAnimationState, AlligatorAnimation.ALLIGATOR_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING, 0.1F));
		this.animateSmooth(entity.walkAnimationState, AlligatorAnimation.ALLIGATOR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.0F));

		float pitch = entity.getXBodyRot(partialTick) * Mth.DEG_TO_RAD;
		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;

		this.rotatePart(this.body, pitch, 0.0F, roll);
		this.rotatePart(this.neck, -pitch * HEAD_COUNTER, 0.0F, -roll * HEAD_COUNTER);
		this.rotatePart(this.tail, entity.getSegmentPitchOffset(1, partialTick) * Mth.DEG_TO_RAD, entity.getSegmentYawOffset(1, partialTick) * Mth.DEG_TO_RAD, 0.0F);
		this.rotatePart(this.tail2, entity.getSegmentPitchOffset(2, partialTick) * Mth.DEG_TO_RAD, entity.getSegmentYawOffset(2, partialTick) * Mth.DEG_TO_RAD, 0.0F);

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
