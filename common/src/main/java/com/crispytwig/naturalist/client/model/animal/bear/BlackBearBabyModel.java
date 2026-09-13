package com.crispytwig.naturalist.client.model.animal.bear;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BlackBearBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
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

public class BlackBearBabyModel extends NaturalistEntityModel<BlackBear> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("black_bear_baby"), "main");
	private final ModelPart root;
	private final ModelPart skull;

	public BlackBearBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.skull = this.root.getChild("body").getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(2.75F, -4.0F, 4.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(20, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(20, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(20, 18).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 0.0F, -6.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(20, 18).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.5F, -4.0F, -5.0F, 9.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 1.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 27).mirror().addBox(-4.0F, -3.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 18).addBox(-3.0F, -2.0F, -3.5F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(2.0F, -3.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.5F));
		PartDefinition nose = skull.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(20, 25).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -3.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(BlackBear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.sleepAnimationState, BlackBearBabyAnimation.BEAR_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, BlackBearBabyAnimation.BEAR_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sniffAnimationState, BlackBearBabyAnimation.BEAR_SNIFF, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, BlackBearBabyAnimation.BEAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BlackBearBabyAnimation.BEAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.4F));
		this.animateSmooth(entity.runAnimationState, BlackBearBabyAnimation.BEAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));

		if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}

}
