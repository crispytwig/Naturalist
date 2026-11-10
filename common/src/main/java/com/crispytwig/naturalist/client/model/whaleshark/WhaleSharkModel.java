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

public class WhaleSharkModel extends NaturalistEntityModel<WhaleShark> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("whale_shark"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart skullRot;
	private final ModelPart mouthOpen;
	private final ModelPart openOverlay;
	private final ModelPart mouthClosed;
	private final ModelPart closedOverlay;
	private final ModelPart tail;
	private final ModelPart tail2;

	public WhaleSharkModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.skullRot = this.body.getChild("skullRot");
		ModelPart skull = this.skullRot.getChild("skull");
		this.mouthOpen = skull.getChild("mouthOpen");
		this.openOverlay = this.mouthOpen.getChild("openOverlay");
		this.mouthClosed = skull.getChild("mouthClosed");
		this.closedOverlay = this.mouthClosed.getChild("closedOverlay");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-15.0F, -9.0F, -27.0F, 30.0F, 23.0F, 45.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -15.0F, -11.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -27.0F));
		PartDefinition skull = skullRot.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(94, 109).addBox(-15.0F, -3.0F, -26.0F, 30.0F, 9.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition mouthOpen = skull.addOrReplaceChild("mouthOpen", CubeListBuilder.create()
		.texOffs(0, 68).addBox(-18.0F, -7.5F, -13.0F, 36.0F, 15.0F, 26.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 1.5F, -13.0F));
		PartDefinition openOverlay = mouthOpen.addOrReplaceChild("openOverlay", CubeListBuilder.create()
		.texOffs(132, 146).addBox(-18.0F, -18.0F, -64.0F, 36.0F, 15.0F, 26.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 10.5F, 51.0F));
		PartDefinition mouthClosed = skull.addOrReplaceChild("mouthClosed", CubeListBuilder.create()
		.texOffs(0, 195).addBox(-18.0F, -7.5F, -13.0F, 36.0F, 15.0F, 26.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 1.5F, -13.0F));
		PartDefinition closedOverlay = mouthClosed.addOrReplaceChild("closedOverlay", CubeListBuilder.create()
		.texOffs(132, 195).addBox(-18.0F, -18.0F, -64.0F, 36.0F, 15.0F, 26.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 10.5F, 51.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 109).addBox(-9.0F, -8.1048F, -0.9323F, 18.0F, 16.0F, 29.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 2.1048F, 15.9323F));
		PartDefinition bottomFin = tail.addOrReplaceChild("bottomFin", CubeListBuilder.create()
		.texOffs(150, 40).addBox(-1.0F, -2.2234F, -0.5057F, 2.0F, 13.0F, 9.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 8.3952F, 6.0677F, 0.9599F, 0.0F, 0.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(94, 137).addBox(-4.0F, -6.0F, 0.0F, 8.0F, 12.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.1048F, 28.0677F));
		PartDefinition fluke = tail2.addOrReplaceChild("fluke", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, -2.0F, 0.0F, 0.0F, -1.5708F));
		PartDefinition fluke_r1 = fluke.addOrReplaceChild("fluke_r1", CubeListBuilder.create()
		.texOffs(124, 83).addBox(14.8492F, -1.0F, 16.7561F, 11.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9549F, 0.0F, -2.545F, 0.0F, -0.7854F, 0.0F));
		PartDefinition fluke_r2 = fluke.addOrReplaceChild("fluke_r2", CubeListBuilder.create()
		.texOffs(124, 68).addBox(3.5355F, -1.0F, 3.6924F, 38.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 5.5F, 0.0F, -0.7854F, 0.0F));
		PartDefinition backFin = body.addOrReplaceChild("backFin", CubeListBuilder.create()
		.texOffs(150, 0).addBox(-1.5F, -21.0F, -0.5F, 2.0F, 21.0F, 19.0F, new CubeDeformation(0.0F))
		.texOffs(36, 154).addBox(-1.5F, -11.0F, -10.5F, 2.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -6.0F, 12.5F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(0, 154).addBox(-1.0F, 0.0F, -9.5F, 2.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(60, 154).addBox(-1.0F, 16.0F, -3.5F, 2.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 8.0F, -9.5F, 0.4363F, 0.0F, -0.7854F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(0, 154).mirror().addBox(-1.0F, 0.0F, -9.5F, 2.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 154).mirror().addBox(-1.0F, 16.0F, -3.5F, 2.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-15.0F, 8.0F, -9.5F, 0.4363F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	protected void setupAnimations(WhaleShark entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		AquaticBodyTilt tilt = entity.bodyTilt;
		this.animateSmooth(entity.swimAnimationState, WhaleSharkAnimation.WHALE_SHARK_SWIM, ageInTicks, partialTick, tilt.animSpeed.get(partialTick), entity.getSwimAmplitude(partialTick));

		this.rotatePart(this.root, 0.0F, tilt.turnYaw.radians(partialTick), 0.0F);
		this.rotatePart(this.body, tilt.pitch.radians(partialTick), tilt.bodyYaw.radians(partialTick), tilt.roll.radians(partialTick));
		this.rotatePart(this.skullRot, 0.0F, tilt.headYaw.radians(partialTick), tilt.headRoll.radians(partialTick));
		this.rotatePart(this.tail, tilt.tailPitch.radians(partialTick), tilt.tailYaw.radians(partialTick), tilt.tailRoll.radians(partialTick));
		this.rotatePart(this.tail2, 0.0F, tilt.flukeYaw.radians(partialTick), 0.0F);

		this.mouthOpen.visible = entity.isMouthOpen();
		this.mouthClosed.visible = !entity.isMouthOpen();
		this.openOverlay.visible = entity.isMouthInked();
		this.closedOverlay.visible = entity.isMouthInked();
	}
}
