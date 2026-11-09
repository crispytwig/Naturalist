package com.crispytwig.naturalist.client.model.seahorse;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.common.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.common.entity.seahorse.Seahorse;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jspecify.annotations.Nullable;

public class SeahorseModel extends NaturalistEntityModel<Seahorse> {
	private static final String PREGNANT_PART = "pregnant";

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("seahorse"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart skull;
	private final @Nullable ModelPart pregnant;

	public SeahorseModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("main").getChild("body");
		this.tail = this.body.getChild("tail");
		this.skull = this.body.getChild("skull");
		this.pregnant = this.body.hasChild(PREGNANT_PART) ? this.body.getChild(PREGNANT_PART) : null;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 28.0F, 0.0F));
		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.5F, -8.5F, 0.0F));
		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(26, 10).addBox(-2.0F, -2.5F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition pregnant = body.addOrReplaceChild("pregnant", CubeListBuilder.create()
		.texOffs(16, 0).addBox(-2.0F, -5.0F, -3.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 2.5F, 0.0F));
		PartDefinition dorsalFin = body.addOrReplaceChild("dorsalFin", CubeListBuilder.create()
		.texOffs(14, 15).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.6F, -0.5F, 2.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(10, 29).addBox(-0.5F, -3.0F, -3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.25F))
		.texOffs(0, 28).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(0.0F, -6.0F, -1.5F, 0.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(16, 10).addBox(-0.5F, -3.0F, -3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.5F, -0.5F));
		PartDefinition rightEar = skull.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(26, 19).mirror().addBox(0.0F, -3.5F, 0.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -2.5F, 2.5F));
		PartDefinition leftEar = skull.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(26, 19).addBox(0.0F, -3.5F, 0.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.5F, 2.5F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, -1).addBox(0.0F, -1.0F, -6.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.5F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	protected AnimationDefinition swimAnimation() {
		return SeahorseAnimation.SEAHORSE_SWIM;
	}

	protected AnimationDefinition flopAnimation() {
		return SeahorseAnimation.SEAHORSE_FLOP;
	}

	@Override
	protected void setupAnimations(Seahorse entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		AquaticBodyTilt tilt = entity.bodyTilt;
		this.animateSmooth(entity.flopAnimationState, this.flopAnimation(), ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, this.swimAnimation(), ageInTicks, partialTick, tilt.animSpeed.get(partialTick));

		float swim = entity.swimAnimationState.factor(partialTick);
		this.rotatePart(this.root, swim * tilt.pitch.radians(partialTick), 0.0F, swim * tilt.roll.radians(partialTick));
		this.rotatePart(this.body, 0.0F, swim * tilt.bodyYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.tail, 0.0F, swim * tilt.tailYaw.radians(partialTick), swim * tilt.tailRoll.radians(partialTick));
		this.rotatePart(this.skull, 0.0F, swim * tilt.headYaw.radians(partialTick), swim * tilt.headRoll.radians(partialTick));

		if (this.pregnant != null) {
			this.pregnant.visible = entity.isPregnant();
		}
	}
}
