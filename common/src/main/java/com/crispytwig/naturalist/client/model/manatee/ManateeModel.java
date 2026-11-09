package com.crispytwig.naturalist.client.model.manatee;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.common.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.common.entity.manatee.Manatee;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ManateeModel extends NaturalistEntityModel<Manatee> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("manatee"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart skull;
	private final ModelPart nose;
	private final ModelPart tail;
	private final ModelPart tail2;

	public ManateeModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.skull = this.body.getChild("skull");
		this.nose = this.skull.getChild("nose");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-8.0F, -14.0F, -10.0F, 16.0F, 13.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(44, 46).addBox(-6.0F, -3.375F, -5.0F, 12.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.625F, -10.0F));
		PartDefinition nose = skull.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(53, 67).addBox(-4.0F, -4.5F, -3.0F, 8.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.125F, -7.0F));
		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(56, 31).addBox(0.0F, -0.5F, -3.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -3.5F, -6.0F));
		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(56, 31).mirror().addBox(-10.0F, -0.5F, -3.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, -3.5F, -6.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 44).addBox(-4.0F, -3.5F, -1.0F, 8.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, 7.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(0, 31).addBox(-8.0F, -0.5F, -3.5F, 16.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 10.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Manatee entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		AquaticBodyTilt tilt = entity.bodyTilt;
		this.animateSmooth(entity.swimAnimationState, ManateeAnimation.MANATEE_SWIM, ageInTicks, partialTick, tilt.animSpeed.get(partialTick));
		this.animateSmooth(entity.rollAnimationState, ManateeAnimation.MANATEE_ROLL, ageInTicks, partialTick);

		this.rotatePart(this.root, tilt.pitch.radians(partialTick), 0.0F, tilt.roll.radians(partialTick));
		this.rotatePart(this.body, 0.0F, tilt.bodyYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.tail, tilt.tailPitch.radians(partialTick), tilt.tailYaw.radians(partialTick), tilt.tailRoll.radians(partialTick));
		this.rotatePart(this.tail2, 0.0F, tilt.flukeYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.skull, 0.0F, tilt.headYaw.radians(partialTick), tilt.headRoll.radians(partialTick));

		float surface = entity.surfaceAnimationState.factor(partialTick);
		if (surface > 0.0F) {
			float seconds = ageInTicks / 20.0F;
			float sway = Mth.sin(seconds * 60.0F * Mth.DEG_TO_RAD);
			this.rotatePart(this.root, surface * (-18.0F + sway * 2.0F) * Mth.DEG_TO_RAD, 0.0F, 0.0F);
			this.root.y -= surface * Mth.sin(seconds * 50.0F * Mth.DEG_TO_RAD) * 0.5F;
			this.rotatePart(this.skull, surface * (-15.0F + sway * 3.0F) * Mth.DEG_TO_RAD, 0.0F, 0.0F);
			this.rotatePart(this.nose, surface * -10.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F);
		}
	}
}
