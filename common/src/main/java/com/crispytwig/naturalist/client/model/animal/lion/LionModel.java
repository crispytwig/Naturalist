package com.crispytwig.naturalist.client.model.animal.lion;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.LionAnimation;
import com.crispytwig.naturalist.world.entity.animal.lion.Lion;
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

public class LionModel extends NaturalistEntityModel<Lion> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("lion"), "main");
	private final ModelPart root;
    private final ModelPart neck;
	private final ModelPart awake;
	private final ModelPart angry;
	private final ModelPart asleep;
	private final ModelPart mane;

	public LionModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.neck = body.getChild("neck");
		this.awake = this.neck.getChild("awake");
		this.angry = this.neck.getChild("angry");
		this.asleep = this.neck.getChild("asleep");
		this.mane = this.neck.getChild("mane");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -6.0F, -11.0F, 10.0F, 12.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, -3.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(72, 73).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 13.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(80, 73).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 80).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(68, 19).addBox(-2.4F, -1.8F, -10.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 66).addBox(-1.4F, -0.8F, -10.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offset(-0.1F, -1.2F, -12.0F));
		PartDefinition awake = neck.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 66).addBox(-5.0F, -25.0F, -22.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offset(0.1F, 18.2F, 15.0F));
		PartDefinition angry = neck.addOrReplaceChild("angry", CubeListBuilder.create()
		.texOffs(0, 104).addBox(-5.0F, -25.0F, -22.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offset(0.1F, 18.2F, 15.0F));
		PartDefinition asleep = neck.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 85).addBox(-5.0F, -25.0F, -22.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offset(0.1F, 18.2F, 15.0F));
		PartDefinition jaw = neck.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(68, 26).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(68, 31).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.1F, 2.2F, -7.0F));
		PartDefinition ears = neck.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.1F, -5.8F, -4.5F));
		PartDefinition leftEar = ears.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(80, 31).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -0.5F, 0.0F));
		PartDefinition rightEar = ears.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(80, 31).mirror().addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -0.5F, 0.0F));
		PartDefinition mane = neck.addOrReplaceChild("mane", CubeListBuilder.create()
		.texOffs(0, 36).addBox(-7.9F, -9.8F, -5.0F, 16.0F, 16.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(60, 36).addBox(-7.9F, -9.8F, -5.0F, 16.0F, 16.0F, 14.0F, new CubeDeformation(0.5F))
		.texOffs(36, 66).addBox(-4.9F, -5.8F, -8.5F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -1.0F, 1.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -22.0F, -15.0F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 1.0F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(68, 0).addBox(-3.0F, 0.1F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.75F, 7.9F, 4.5F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(68, 0).mirror().addBox(-1.0F, 0.1F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.75F, 7.9F, 4.5F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.0F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(68, 0).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.1F, 9.5F, 18.5F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(68, 0).mirror().addBox(-2.0F, -1.5F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.1F, 9.5F, 18.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Lion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.mane.visible = entity.hasMane();

		boolean angry = entity.isAggressive();
		boolean sleeping = entity.isSleeping() && !angry;
		this.awake.visible = !sleeping && !angry;
		this.asleep.visible = sleeping;
		this.angry.visible = angry;

		this.animateSmooth(entity.attackAnimationState, LionAnimation.LION_ATTACK, ageInTicks, partialTick);
		this.animateSmooth(entity.sleepAnimationState, LionAnimation.LION_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sleep2AnimationState, LionAnimation.LION_SLEEP2, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, LionAnimation.LION_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, LionAnimation.LION_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.preyAnimationState, LionAnimation.LION_PREY, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 0.8F));
		this.animateSmooth(entity.runAnimationState, LionAnimation.LION_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));

		if (!entity.isSleeping()) {
			applyHeadLook(this.neck, netHeadYaw, headPitch);
		}
	}
}
