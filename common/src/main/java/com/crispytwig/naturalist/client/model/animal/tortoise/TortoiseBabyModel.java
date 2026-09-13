package com.crispytwig.naturalist.client.model.animal.tortoise;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.TortoiseBabyAnimation;
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

public class TortoiseBabyModel extends NaturalistEntityModel<Tortoise> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("tortoise_baby"), "main");
	private final ModelPart root;
    private final ModelPart neck;
	private final ModelPart asleep;

	public TortoiseBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
        ModelPart skullRot = body.getChild("skullRot");
		this.neck = skullRot.getChild("neck");
		this.asleep = this.neck.getChild("asleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(10, 21).addBox(-3.5F, -11.0F, -22.5F, 7.0F, 7.0F, 4.0F, new CubeDeformation(0.5F))
		.texOffs(0, 0).addBox(-3.5F, -11.0F, -22.5F, 7.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.5F, 8.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-3.5F, -11.0F, -22.5F, 7.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.5F, 8.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, -2.0F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(18, 11).addBox(-1.5F, -3.0F, -4.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition asleep = neck.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(30, 11).addBox(-1.5F, -5.0F, -6.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 2.0F));
		PartDefinition mask = neck.addOrReplaceChild("mask", CubeListBuilder.create()
		.texOffs(30, 5).addBox(-1.5F, -7.0F, -12.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.0F, 8.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.25F, 0.5381F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 3.7119F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(22, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -2.5F, -7.5F, 0.0F, -0.3927F, 0.0F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(22, 0).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, -2.5F, -7.5F, 0.0F, 0.3927F, 0.0F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.25F, 3.5381F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(22, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3827F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(22, 0).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.3827F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	protected void setupAnimations(Tortoise entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.asleep.visible = false;

		this.animateSmooth(entity.sitAnimationState, TortoiseBabyAnimation.TORTOISE_SIT, ageInTicks, partialTick);
		this.animateIdleSmooth(entity.idleAnimationState, TortoiseBabyAnimation.TORTOISE_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, TortoiseBabyAnimation.TORTOISE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 9.5F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
