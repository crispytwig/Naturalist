package com.crispytwig.naturalist.client.model.whaleshark;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.common.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.common.entity.whaleshark.WhaleShark;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class WhaleSharkBabyModel extends NaturalistEntityModel<WhaleShark> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("whale_shark_baby"), "main");
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart body;
	private final ModelPart skull;
	private final ModelPart mouthOpen;
	private final ModelPart mouthClosed;
	private final ModelPart tail;

	public WhaleSharkBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.main = this.root.getChild("main");
		this.body = this.main.getChild("body");
		this.skull = this.body.getChild("skull");
		this.mouthOpen = this.skull.getChild("mouthOpen");
		this.mouthClosed = this.skull.getChild("mouthClosed");
		this.tail = this.body.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));
		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(44, 0).addBox(0.0F, -12.75F, -1.0F, 0.0F, 11.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 7.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-5.0F, 1.0F, -15.0F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create(), PartPose.offset(0.0F, -3.9167F, -8.0F));
		PartDefinition jaw = skull.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(0.0F, 5.9167F, 0.0F));
		PartDefinition mouthOpen = skull.addOrReplaceChild("mouthOpen", CubeListBuilder.create()
		.texOffs(1, 74).addBox(-5.0F, -3.0F, -1.0F, 10.0F, 2.0F, 6.0F, new CubeDeformation(0.02F))
		.texOffs(0, 21).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 7.9167F, -6.0F));
		PartDefinition mouthClosed = skull.addOrReplaceChild("mouthClosed", CubeListBuilder.create()
		.texOffs(0, 61).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 7.9167F, -6.0F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(0, 33).addBox(-1.0F, 0.0F, -1.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.02F)), PartPose.offset(4.0F, 2.0F, -4.0F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(0, 33).mirror().addBox(-9.0F, 0.0F, -1.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offset(-4.0F, 2.0F, -4.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(38, 21).addBox(-2.0F, -2.4714F, -1.6381F, 4.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.4714F, 5.6381F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(0, 51).addBox(0.0F, 3.0F, -6.0F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4714F, 7.3619F, -0.7854F, 0.0F, 0.0F));
		PartDefinition tail_r2 = tail.addOrReplaceChild("tail_r2", CubeListBuilder.create()
		.texOffs(38, 37).addBox(0.0F, -11.0F, -3.0F, 0.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4714F, 8.3619F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(WhaleShark entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		AquaticBodyTilt tilt = entity.bodyTilt;
		this.animateSmooth(entity.swimAnimationState, WhaleSharkBabyAnimation.WHALE_SHARK_BABY_SWIM, ageInTicks, partialTick, tilt.animSpeed.get(partialTick), entity.getSwimAmplitude(partialTick));

		this.rotatePart(this.root, 0.0F, tilt.turnYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.main, tilt.pitch.radians(partialTick), 0.0F, tilt.roll.radians(partialTick));
		this.rotatePart(this.body, 0.0F, tilt.bodyYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.tail, tilt.tailPitch.radians(partialTick), tilt.tailYaw.radians(partialTick), tilt.tailRoll.radians(partialTick));
		this.rotatePart(this.skull, 0.0F, tilt.headYaw.radians(partialTick), tilt.headRoll.radians(partialTick));

		this.mouthOpen.visible = entity.isMouthOpen();
		this.mouthClosed.visible = !entity.isMouthOpen();
	}
}
