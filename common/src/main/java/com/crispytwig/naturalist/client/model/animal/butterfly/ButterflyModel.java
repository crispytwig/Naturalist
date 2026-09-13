package com.crispytwig.naturalist.client.model.animal.butterfly;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ButterflyAnimation;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Butterfly;
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

public class ButterflyModel extends NaturalistEntityModel<Butterfly> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("butterfly"), "main");
	private final ModelPart root;

	public ButterflyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, 0.0F, -3.5F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.1F, -1.5F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(0, 14).addBox(-7.0F, 0.0F, -7.0F, 7.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 1.5F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(0, 27).mirror().addBox(0.0F, 0.0F, -7.0F, 7.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.0F, 1.5F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	protected void setupAnimations(Butterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flyAnimationState, ButterflyAnimation.BUTTERFLY_FLY, ageInTicks, partialTick);
	}
}
