package com.crispytwig.naturalist.client.model.animal.butterfly;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.CaterpillarAnimation;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Caterpillar;
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

public class CaterpillarModel extends NaturalistEntityModel<Caterpillar> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("caterpillar"), "main");
	private final ModelPart root;

	public CaterpillarModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, -3.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(20, 2).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(0.0F, 0.0F, -1.0F));
		PartDefinition antennae = neck.addOrReplaceChild("antennae", CubeListBuilder.create()
		.texOffs(0, 24).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.03F)), PartPose.offset(0.0F, -1.5F, -2.0F));
		PartDefinition extended = body.addOrReplaceChild("extended", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.5F));
		PartDefinition chest = extended.addOrReplaceChild("chest", CubeListBuilder.create()
		.texOffs(7, 7).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 1.5F));
		PartDefinition butt = extended.addOrReplaceChild("butt", CubeListBuilder.create()
		.texOffs(41, 23).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Caterpillar entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, CaterpillarAnimation.CATERPILLAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.crawlAnimationState, CaterpillarAnimation.CATERPILLAR_CRAWL, ageInTicks, partialTick,
				entity.isClimbing() ? 1.0F : movementAnimationSpeed(entity, limbSwingAmount, 3.0F));
	}
}
