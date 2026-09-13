package com.crispytwig.naturalist.client.model.animal.firefly;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.FireflyBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
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

public class FireflyBabyModel extends NaturalistEntityModel<Firefly> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("firefly_baby"), "main");
	private final ModelPart root;

	public FireflyBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 2.0F));
		PartDefinition butt = body.addOrReplaceChild("butt", CubeListBuilder.create()
		.texOffs(2, 0).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -2.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 4).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));
		PartDefinition antenna = neck.addOrReplaceChild("antenna", CubeListBuilder.create()
		.texOffs(0, 10).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	protected void setupAnimations(Firefly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, FireflyBabyAnimation.FIREFLY_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, FireflyBabyAnimation.FIREFLY_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));
	}
}
