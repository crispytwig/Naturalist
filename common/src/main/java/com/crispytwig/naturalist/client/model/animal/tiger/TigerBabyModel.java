package com.crispytwig.naturalist.client.model.animal.tiger;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.TigerBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.tiger.Tiger;
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

public class TigerBabyModel extends NaturalistEntityModel<Tiger> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("tiger_baby"), "main");
	private final ModelPart root;
    private final ModelPart skull;
	private final ModelPart awake;
	private final ModelPart asleep;

	public TigerBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skull = body.getChild("skull");
		this.awake = this.skull.getChild("awake");
		this.asleep = this.skull.getChild("asleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(0, 35).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -1.0F, -4.5F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(0, 35).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -1.0F, -4.5F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(10, 35).addBox(-1.0F, 0.5F, -1.5F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.5F, 5.5F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(10, 35).mirror().addBox(-1.0F, 0.5F, -1.5F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 0.5F, 5.5F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 7.0F, 15.0F, new CubeDeformation(0.026F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(42, 0).addBox(-2.5F, -2.3F, -6.4F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 6).addBox(-2.5F, -1.3F, -6.4F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.7F, -6.6F));
		PartDefinition awake = skull.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 22).addBox(-4.0F, -17.0F, -11.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.7F, 6.6F));
		PartDefinition leftEar = skull.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(20, 35).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -4.8F, 0.1F));
		PartDefinition rightEar = skull.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(20, 35).mirror().addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -4.8F, 0.1F));
		PartDefinition asleep = skull.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 48).addBox(-4.0F, -17.0F, -11.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.7F, 6.6F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(28, 22).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(28, 33).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -3.5F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Tiger entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sleeping = entity.isSleeping() || entity.isInSittingPose();
		this.awake.visible = !sleeping;
		this.asleep.visible = sleeping;

		this.animateSmooth(entity.sleepAnimationState, TigerBabyAnimation.TIGER_BABY_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sleep2AnimationState, TigerBabyAnimation.TIGER_BABY_SLEEP, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, TigerBabyAnimation.TIGER_BABY_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, TigerBabyAnimation.TIGER_BABY_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.preyAnimationState, TigerBabyAnimation.TIGER_BABY_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, TigerBabyAnimation.TIGER_BABY_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		if (!sleeping) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
