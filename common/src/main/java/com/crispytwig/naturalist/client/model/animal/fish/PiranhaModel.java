package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.PiranhaAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Piranha;
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

public class PiranhaModel extends NaturalistEntityModel<Piranha> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("piranha"), "main");
	private final ModelPart root;

	public PiranhaModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 1.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 18).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(12, 12).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 6.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition jaw = body.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(0, 2).addBox(-1.5F, -1.0F, -4.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -2.0F));
		PartDefinition right_fin = body.addOrReplaceChild("right_fin", CubeListBuilder.create()
		.texOffs(2, 16).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition left_fin = body.addOrReplaceChild("left_fin", CubeListBuilder.create()
		.texOffs(2, 12).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition fin_top = body.addOrReplaceChild("fin_top", CubeListBuilder.create()
		.texOffs(20, 2).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -3.0F));
		PartDefinition fin_bottom = body.addOrReplaceChild("fin_bottom", CubeListBuilder.create()
		.texOffs(20, 12).addBox(0.0F, 0.0F, 1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -3.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(6, 3).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(Piranha entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, PiranhaAnimation.PIRANHA_ATTACK, ageInTicks, partialTick);

		this.animateSmooth(entity.flopAnimationState, PiranhaAnimation.PIRANHA_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, PiranhaAnimation.PIRANHA_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F, SMALL_SWIMMER_LIMB_SWING));

		this.root.xRot += entity.swimTilt.getSwimPitch(partialTick) * Mth.DEG_TO_RAD;
	}
}
