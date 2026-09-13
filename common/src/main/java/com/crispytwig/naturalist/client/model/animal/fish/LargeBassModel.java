package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BassAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class LargeBassModel extends NaturalistEntityModel<Bass> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("bass_large"), "main");
	private final ModelPart root;

	public LargeBassModel(ModelPart root) {
		super(root, RenderTypes::entityCutoutCull);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.25F, -3.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 8).addBox(-1.0F, -2.25F, -4.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-1.5F, -2.25F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(14, 12).addBox(0.0F, -5.0F, 0.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(0.0F, 2.0F, 0.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.0F));

		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(1, 3).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 1.0F, 2.0F, 0.3655F, 0.7119F, 0.5299F));

		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(1, 3).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.0F, 2.0F, 0.3655F, -0.7119F, -0.5299F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Bass entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flopAnimationState, BassAnimation.BASS_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, BassAnimation.BASS_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, SMALL_SWIMMER_LIMB_SWING));

		this.root.xRot += entity.swimTilt.getSwimPitch(partialTick) * Mth.DEG_TO_RAD;
	}
}
