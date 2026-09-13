package com.crispytwig.naturalist.client.model.animal.rat;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.RatAnimation;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
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

public class RatModel extends NaturalistEntityModel<Rat> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("rat"), "main");
	private final ModelPart root;
    private final ModelPart skull;
	private final ModelPart awake;
	private final ModelPart sleep;

	public RatModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skull = body.getChild("skull");
		this.awake = this.skull.getChild("awake");
		this.sleep = this.skull.getChild("sleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.5F, -4.0F, -9.0F, 7.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 5.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(42, 16).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 2.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(41, 22).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(4, 3).addBox(-1.0F, 2.0F, -6.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -9.0F));
		PartDefinition awake = skull.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 17).addBox(-2.5F, -8.0F, -10.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 4.0F));
		PartDefinition sleep = skull.addOrReplaceChild("sleep", CubeListBuilder.create()
		.texOffs(42, 0).addBox(-2.5F, -8.0F, -10.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 4.0F));
		PartDefinition leftWhisker = skull.addOrReplaceChild("leftWhisker", CubeListBuilder.create()
		.texOffs(0, 3).addBox(0.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -0.5F, -6.0F));
		PartDefinition rightWhisker = skull.addOrReplaceChild("rightWhisker", CubeListBuilder.create()
		.texOffs(0, 3).mirror().addBox(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -0.5F, -6.0F));
		PartDefinition leftEar = skull.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(0, 0).addBox(0.0F, -2.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(2.5F, -1.5F, 0.0F));
		PartDefinition rightEar = skull.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-3.0F, -2.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-2.5F, -1.5F, 0.0F));
		PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(24, 17).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -1.0F, 5.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(2.5F, -1.0F, -3.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(25, 0).addBox(-1.5F, 0.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(25, 0).mirror().addBox(-1.5F, 0.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(24, 17).mirror().addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -1.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected void setupAnimations(Rat entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sleeping = entity.isSleeping();
		this.awake.visible = !sleeping;
		this.sleep.visible = sleeping;

		this.animateSmooth(entity.sleepAnimationState, RatAnimation.RAT_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, RatAnimation.RAT_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.standingAnimationState, RatAnimation.RAT_STANDING, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, RatAnimation.RAT_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));

		this.animateIdleSmooth(entity.idleAnimationState, RatAnimation.RAT_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, RatAnimation.RAT_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, RatAnimation.RAT_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		if (!sleeping && !entity.isInSittingPose()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
