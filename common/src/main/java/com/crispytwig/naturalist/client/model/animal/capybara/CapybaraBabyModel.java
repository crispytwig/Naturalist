package com.crispytwig.naturalist.client.model.animal.capybara;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.CapybaraBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
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

public class CapybaraBabyModel extends NaturalistEntityModel<Capybara> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("capybara_baby"), "main");
	private final ModelPart root;
	private final ModelPart skull;

	public CapybaraBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.skull = this.root.getChild("body").getChild("skull");
		this.skull.getChild("asleep").visible = false;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-0.75F, 19.25F, 1.75F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-0.75F, 1.25F, 2.25F));
		PartDefinition backLegs = legs.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = backLegs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(18, 15).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = backLegs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(18, 15).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));
		PartDefinition frontLegs = legs.addOrReplaceChild("frontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -7.0F));
		PartDefinition rightArm = frontLegs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(18, 15).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = frontLegs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(18, 15).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -3.0F, -4.5F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, -1.25F, -2.25F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 15).addBox(-1.5F, -3.5F, -5.5F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -4.0F));
		PartDefinition asleep = skull.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(12, 21).addBox(-1.5F, -11.0F, -10.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.5F, 4.5F));
		PartDefinition leftEar = skull.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(16, 20).addBox(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -3.0F, 0.5F));
		PartDefinition rightEar = skull.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(16, 20).mirror().addBox(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -3.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	protected void setupAnimations(Capybara entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.animateSmooth(entity.sitAnimationState, CapybaraBabyAnimation.CAPYBARA_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sleepAnimationState, CapybaraBabyAnimation.CAPYBARA_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sleep2AnimationState, CapybaraBabyAnimation.CAPYBARA_SLEEP, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, CapybaraBabyAnimation.CAPYBARA_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.swimAnimationState, CapybaraBabyAnimation.CAPYBARA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 4.0F, LARGE_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.walkAnimationState, CapybaraBabyAnimation.CAPYBARA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 4.0F));
		this.animateSmooth(entity.runAnimationState, CapybaraBabyAnimation.CAPYBARA_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.0F));

		if (!entity.isSleeping() && !entity.isInSittingPose()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
