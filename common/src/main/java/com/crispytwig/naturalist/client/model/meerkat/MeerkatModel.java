package com.crispytwig.naturalist.client.model.meerkat;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.common.entity.meerkat.Meerkat;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.Set;

public class MeerkatModel extends NaturalistEntityModel<Meerkat> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("meerkat"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart awake;
	private final ModelPart asleep;

	public MeerkatModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		ModelPart body = this.root.getChild("main").getChild("waist").getChild("body");
		this.head = body.getChild(body.hasChild("lookoutHead") ? "lookoutHead" : "headRotation");
		ModelPart skull = this.head.getChild("skull");
		this.awake = skull.getChild("awake");
		this.asleep = skull.getChild("asleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -4.75F, 0.0F));
		PartDefinition waist = main.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -1.25F, -4.0F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(8, 17).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.75F, 3.5F));
		PartDefinition lookoutHead = body.addOrReplaceChild("lookoutHead", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, -4.0F));
		PartDefinition skull = lookoutHead.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 7).addBox(2.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-1.0F, -1.5F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 7).mirror().addBox(-3.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));
		PartDefinition awake = skull.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-2.0F, -8.0F, -6.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 4.0F));
		PartDefinition asleep = skull.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 24).addBox(-2.0F, -8.0F, -6.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 4.0F));
		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(13, 20).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.95F, 1.75F, -3.0F));
		PartDefinition leftHand = leftArm.addOrReplaceChild("leftHand", CubeListBuilder.create()
		.texOffs(11, 18).addBox(-0.5F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));
		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(13, 20).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.95F, 1.75F, -3.0F));
		PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create()
		.texOffs(11, 18).mirror().addBox(-1.5F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 0.0F));
		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition legs = main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.25F, 1.25F, 2.75F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(14, 11).mirror().addBox(-0.5F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(13, 20).mirror().addBox(-1.5F, 3.45F, -3.0F, 2.0F, 0.0F, 2.0F, Set.of(Direction.DOWN)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(14, 11).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(13, 20).addBox(-0.5F, 3.45F, -3.0F, 2.0F, 0.0F, 2.0F, Set.of(Direction.DOWN)), PartPose.offset(2.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Meerkat entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		boolean baby = entity.isBaby();
		this.animateSmooth(entity.idleAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_IDLE : MeerkatAnimation.MEERKAT_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.walkAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_WALK : MeerkatAnimation.MEERKAT_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, baby ? 3.5F : 3.0F));
		this.animateSmooth(entity.runAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_RUN : MeerkatAnimation.MEERKAT_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.sleepAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_SLEEP : MeerkatAnimation.MEERKAT_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_SIT_IDLE : MeerkatAnimation.MEERKAT_SIT_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.lookoutAnimationState, baby ? MeerkatBabyAnimation.MEERKAT_BABY_IDLE_EVENT : MeerkatAnimation.MEERKAT_IDLE_EVENT, ageInTicks, partialTick);

		float aimYaw = baby ? 0.0F : entity.getAimYaw(partialTick);
		float blend = baby ? 0.0F : entity.getLookoutBlend(partialTick);
		float headYaw = Mth.clamp(netHeadYaw - aimYaw, -50.0F, 50.0F) * Mth.DEG_TO_RAD;
		this.rotatePart(this.root, 0.0F, aimYaw * Mth.DEG_TO_RAD, 0.0F);
		this.rotatePart(this.head, Mth.clamp(headPitch, -35.0F, 35.0F) * Mth.DEG_TO_RAD, (1.0F - blend) * headYaw, blend * headYaw);

		this.awake.visible = !entity.isSleeping();
		this.asleep.visible = entity.isSleeping();
	}
}
