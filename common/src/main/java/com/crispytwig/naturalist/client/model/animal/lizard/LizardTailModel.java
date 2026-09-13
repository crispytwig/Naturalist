package com.crispytwig.naturalist.client.model.animal.lizard;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.LizardTailAnimation;
import com.crispytwig.naturalist.world.entity.animal.lizard.LizardTail;
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

public class LizardTailModel extends NaturalistEntityModel<LizardTail> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("lizard_tail"), "main");
	private final ModelPart root;

	public LizardTailModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(0.0F, -5.5F, -5.0F, 0.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected void setupAnimations(LizardTail entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flopAnimationState, LizardTailAnimation.REPTILE_TAIL_FLOP, ageInTicks, partialTick);
	}
}
