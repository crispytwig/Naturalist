package com.crispytwig.naturalist.client.model.animal.deer;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DeerBabyAnimation;
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

public class DeerBabyModel extends NaturalistEntityModel<Deer> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("deer_baby"), "main");
	private final ModelPart root;
	private final ModelPart neck;

	public DeerBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -1.5F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(16, 12).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 12).addBox(-1.0F, -6.0F, -2.0F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 18).addBox(-1.0F, -6.0F, -4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -4.0F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(10, 20).addBox(0.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.02F)), PartPose.offset(1.0F, -5.0F, 1.0F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(10, 20).mirror().addBox(-2.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offset(-1.0F, -5.0F, 1.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.25F, -6.0F, -3.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(10, 12).mirror().addBox(-0.75F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(10, 12).addBox(-0.25F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 7.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(10, 12).mirror().addBox(-0.75F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 7.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(10, 12).addBox(-0.25F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Deer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.eatAnimationState, DeerBabyAnimation.DEER_EAT, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, DeerBabyAnimation.DEER_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 1.5F);
		this.animateSmooth(entity.walkAnimationState, DeerBabyAnimation.DEER_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.7F));
		this.animateSmooth(entity.runAnimationState, DeerBabyAnimation.DEER_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));

		if (!entity.isEating()) {
			applyHeadLook(this.neck, netHeadYaw, headPitch);
		}
	}
}
