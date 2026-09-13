package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BlobfishAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class BlobfishGrayModel extends NaturalistEntityModel<Blobfish> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("blobfish_gray"), "main");
	private final ModelPart root;

	public BlobfishGrayModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -1.0F));
		PartDefinition sf_head = root.addOrReplaceChild("sf_head", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 6.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, -2.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 12).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(23, 7).addBox(0.0F, -5.0F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(24, 0).addBox(0.0F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 3.0F, 1.0F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(24, 0).mirror().addBox(-5.0F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 3.0F, 1.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(27, 10).addBox(0.0F, -3.5F, 0.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected void setupAnimations(Blobfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.idleAnimationState, BlobfishAnimation.BLOBFISH_PINK_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, BlobfishAnimation.BLOBFISH_GRAY_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F, SMALL_SWIMMER_LIMB_SWING));

		this.root.xRot += entity.swimTilt.getSwimPitch(partialTick) * Mth.DEG_TO_RAD;
		if (entity.isConverting()) {
			this.root.yRot -= Mth.cos((entity.tickCount + partialTick) * 3.0F) * 0.1F;
		}
	}
}
