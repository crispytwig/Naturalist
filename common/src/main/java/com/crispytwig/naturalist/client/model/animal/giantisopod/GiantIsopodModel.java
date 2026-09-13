package com.crispytwig.naturalist.client.model.animal.giantisopod;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.GiantIsopodAnimation;
import com.crispytwig.naturalist.world.entity.animal.giantisopod.GiantIsopod;
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

public class GiantIsopodModel extends NaturalistEntityModel<GiantIsopod> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("giant_isopod"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart rolled;

	public GiantIsopodModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.rolled = this.root.getChild("rolled");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 24).addBox(-5.5F, -6.0F, -10.0F, 11.0F, 6.0F, 10.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -2.0F, 2.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(32, 32).addBox(-4.0F, 0.0F, -8.75F, 4.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.0F, -1.25F, 0.0F, 0.0F, -0.3927F));
		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create()
		.texOffs(32, 32).mirror().addBox(0.0F, 0.0F, -8.75F, 4.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, 0.0F, -1.25F, 0.0F, 0.0F, 0.3927F));
		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create()
		.texOffs(34, 0).addBox(-5.5F, -3.0F, -0.5F, 11.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.5F));
		PartDefinition body2_r1 = body2.addOrReplaceChild("body2_r1", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, 0.0F, 1.25F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 3.0F, -1.75F, 0.0F, 0.0F, -0.3927F));
		PartDefinition body2_r2 = body2.addOrReplaceChild("body2_r2", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 1.25F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, 3.0F, -1.75F, 0.0F, 0.0F, 0.3927F));
		PartDefinition tail = body2.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(24, 42).addBox(-4.5F, -2.0F, 0.0F, 9.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(24, 24).addBox(-5.5F, 2.0F, 0.0F, 11.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 4.5F));
		PartDefinition tailLegs = tail.addOrReplaceChild("tailLegs", CubeListBuilder.create(), PartPose.offset(2.5F, 2.0F, 0.0F));
		PartDefinition leftPleopod = tailLegs.addOrReplaceChild("leftPleopod", CubeListBuilder.create()
		.texOffs(0, 7).addBox(-2.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		PartDefinition rightPleopod = tailLegs.addOrReplaceChild("rightPleopod", CubeListBuilder.create()
		.texOffs(0, 7).mirror().addBox(-4.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 40).addBox(-4.5F, -2.0F, -3.0F, 9.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -10.0F));
		PartDefinition antennae = skull.addOrReplaceChild("antennae", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, 0.3054F, 0.0F, 0.0F));
		PartDefinition leftAntennae = antennae.addOrReplaceChild("leftAntennae", CubeListBuilder.create()
		.texOffs(39, 11).addBox(-1.5F, 0.0F, -7.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 2.0F));
		PartDefinition rightAntennae = antennae.addOrReplaceChild("rightAntennae", CubeListBuilder.create()
		.texOffs(39, 11).mirror().addBox(-3.5F, 0.0F, -7.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, 2.0F));
		PartDefinition backLegs = body.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, -2.0F));
		PartDefinition leftBackLegs = backLegs.addOrReplaceChild("leftBackLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg5 = leftBackLegs.addOrReplaceChild("leftLeg5", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -2.0F, 2.0F));
		PartDefinition leftLeg6 = leftBackLegs.addOrReplaceChild("leftLeg6", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -2.0F, 4.0F));
		PartDefinition rightBackLegs = backLegs.addOrReplaceChild("rightBackLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg5 = rightBackLegs.addOrReplaceChild("rightLeg5", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -2.0F, 2.0F));
		PartDefinition rightLeg6 = rightBackLegs.addOrReplaceChild("rightLeg6", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -2.0F, 4.0F));
		PartDefinition legs = backLegs.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, 0.0F));
		PartDefinition leftLegs = legs.addOrReplaceChild("leftLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg1 = leftLegs.addOrReplaceChild("leftLeg1", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -4.0F, -7.0F));
		PartDefinition leftLeg2 = leftLegs.addOrReplaceChild("leftLeg2", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -4.0F, -5.0F));
		PartDefinition leftLeg3 = leftLegs.addOrReplaceChild("leftLeg3", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -4.0F, -3.0F));
		PartDefinition leftLeg4 = leftLegs.addOrReplaceChild("leftLeg4", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -4.0F, -0.5F));
		PartDefinition rightLegs = legs.addOrReplaceChild("rightLegs", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, 0.0F));
		PartDefinition rightLeg1 = rightLegs.addOrReplaceChild("rightLeg1", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -4.0F, -7.0F));
		PartDefinition rightLeg2 = rightLegs.addOrReplaceChild("rightLeg2", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -4.0F, -5.0F));
		PartDefinition rightLeg3 = rightLegs.addOrReplaceChild("rightLeg3", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -4.0F, -3.0F));
		PartDefinition rightLeg4 = rightLegs.addOrReplaceChild("rightLeg4", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -4.0F, -0.5F));
		PartDefinition rolled = root.addOrReplaceChild("rolled", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.5F, -14.0F, -4.0F, 11.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -2.0F));
		PartDefinition antennae2 = rolled.addOrReplaceChild("antennae2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -4.0F));
		PartDefinition leftAntennae2 = antennae2.addOrReplaceChild("leftAntennae2", CubeListBuilder.create()
		.texOffs(41, 13).addBox(-1.5F, 0.0F, -7.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 2.0F));
		PartDefinition rightAntennae2 = antennae2.addOrReplaceChild("rightAntennae2", CubeListBuilder.create()
		.texOffs(41, 13).mirror().addBox(-3.5F, 0.0F, -7.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 80, 64);
	}

	@Override
	protected void setupAnimations(GiantIsopod entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		boolean hiding = entity.canHide();
		this.rolled.visible = hiding || entity.hideEndAnimationState.isStarted();

		this.animateSmooth(entity.hideAnimationState, GiantIsopodAnimation.GIANT_ISOPOD_HIDE_START, ageInTicks, partialTick);
		this.animateSmooth(entity.hideEndAnimationState, GiantIsopodAnimation.GIANT_ISOPOD_HIDE_END, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, GiantIsopodAnimation.GIANT_ISOPOD_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F, LARGE_SWIMMER_LIMB_SWING));

		this.animateIdleSmooth(entity.idleAnimationState, GiantIsopodAnimation.GIANT_ISOPOD_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, GiantIsopodAnimation.GIANT_ISOPOD_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 6.0F));
	}
}
