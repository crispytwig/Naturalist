package com.crispytwig.naturalist.client.model.animal.tortoise;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.TortoiseAnimation;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
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

public class TortoiseModel extends NaturalistEntityModel<Tortoise> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("tortoise"), "main");
	private final ModelPart root;
    private final ModelPart neck;

	public TortoiseModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
        ModelPart skullRot = body.getChild("skullRot");
		this.neck = skullRot.getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(60, 13).addBox(-8.0F, -11.0F, -19.0F, 16.0F, 16.0F, 9.0F, new CubeDeformation(0.5F))
		.texOffs(0, 0).addBox(-8.0F, -11.0F, -19.0F, 16.0F, 16.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-5.5F, -10.0F, -22.0F, 11.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.0F, 3.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -8.0F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(29, 26).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition mask = neck.addOrReplaceChild("mask", CubeListBuilder.create()
		.texOffs(28, 39).addBox(-3.0F, -7.0F, -14.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 4.0F, 8.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(51, 0).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -5.0F, -8.0F, 0.0F, -0.3927F, 0.0F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(51, 0).mirror().addBox(-3.0F, -2.0F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, -5.0F, -8.0F, 0.0F, 0.3927F, 0.0F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(51, 0).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -5.0F, 8.0F, 0.0F, -0.3927F, 0.0F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(51, 0).mirror().addBox(-3.0F, -2.0F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, -5.0F, 8.0F, 0.0F, 0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Tortoise entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.hurtAnimationState, TortoiseAnimation.TORTOISE_HURT, ageInTicks, partialTick);
		this.animateSmooth(entity.hideAnimationState, TortoiseAnimation.TORTOISE_HIDE, ageInTicks, partialTick);

		this.animateSmooth(entity.sitAnimationState, TortoiseAnimation.TORTOISE_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.digAnimationState, TortoiseAnimation.TORTOISE_DIG, ageInTicks, partialTick);
		this.animateIdleSmooth(entity.idleAnimationState, TortoiseAnimation.TORTOISE_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, TortoiseAnimation.TORTOISE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 6.0F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
