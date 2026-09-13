package com.crispytwig.naturalist.client.model.animal.deer;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DeerAnimation;
import com.crispytwig.naturalist.world.entity.animal.deer.Deer;
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

public class DeerModel extends NaturalistEntityModel<Deer> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("deer"), "main");
	private final ModelPart root;
    private final ModelPart saddle;
	private final ModelPart neck;
	private final ModelPart sleep;

	public DeerModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.saddle = body.getChild("saddle");
		this.neck = body.getChild("skullRot").getChild("neck");
		this.sleep = this.neck.getChild("sleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -5.0F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(77, 5).addBox(-5.0F, -21.0F, -3.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 16.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(5, 11).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 9.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -9.0F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(44, 5).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(41, 29).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition sleep = neck.addOrReplaceChild("sleep", CubeListBuilder.create()
		.texOffs(17, 81).addBox(-3.0F, -29.025F, -12.025F, 2.025F, 1.025F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(15, 79).addBox(-3.0F, -29.025F, -12.025F, 0.0F, 1.025F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 81).mirror().addBox(1.0F, -29.025F, -12.025F, 2.025F, 1.025F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(15, 79).mirror().addBox(3.025F, -29.025F, -12.025F, 0.0F, 1.025F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 17.0F, 9.0F));
		PartDefinition left_ear = neck.addOrReplaceChild("left_ear", CubeListBuilder.create()
		.texOffs(1, 6).addBox(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -12.0F, 0.5F));
		PartDefinition right_ear = neck.addOrReplaceChild("right_ear", CubeListBuilder.create()
		.texOffs(1, 6).mirror().addBox(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -12.0F, 0.5F));
		PartDefinition antlers = neck.addOrReplaceChild("antlers", CubeListBuilder.create()
		.texOffs(0, 29).addBox(-1.0F, -9.01F, -4.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).mirror().addBox(-13.0F, -9.01F, -4.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -14.0F, 1.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -6.0F));
		PartDefinition frontLegs = legs.addOrReplaceChild("frontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 6.0F));
		PartDefinition leftArm = frontLegs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(1, 48).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.25F, -14.0F, -5.0F));
		PartDefinition rightArm = frontLegs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(1, 48).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.25F, -14.0F, -5.0F));
		PartDefinition backLegs = legs.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 6.0F));
		PartDefinition leftLeg = backLegs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(1, 48).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.25F, -14.0F, 9.0F));
		PartDefinition rightLeg = backLegs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(1, 48).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.25F, -14.0F, 9.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Deer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.sleep.visible = false;
		this.saddle.visible = false;

		this.animateSmooth(entity.eatAnimationState, DeerAnimation.DEER_EAT, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, DeerAnimation.DEER_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, DeerAnimation.DEER_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.4F));
		this.animateSmooth(entity.runAnimationState, DeerAnimation.DEER_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.3F));

		if (!entity.isEating()) {
			applyHeadLook(this.neck, netHeadYaw, headPitch);
		}
	}
}
