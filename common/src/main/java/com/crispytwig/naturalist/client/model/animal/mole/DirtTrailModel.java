package com.crispytwig.naturalist.client.model.animal.mole;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DirtTrailAnimation;
import com.crispytwig.naturalist.world.entity.animal.mole.DirtTrail;
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

public class DirtTrailModel extends NaturalistEntityModel<DirtTrail> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("dirt_trail"), "main");
	private final ModelPart mound;
	private final ModelPart extra1;
	private final ModelPart extra2;
	private final ModelPart extra3;

	public DirtTrailModel(ModelPart root) {
		super(root);
		this.mound = root.getChild("mound");
		this.extra1 = this.mound.getChild("extra1");
		this.extra2 = this.mound.getChild("extra2");
		this.extra3 = this.mound.getChild("extra3");
	}

	@Override
	protected String getRootPartName() {
		return "mound";
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mound = partdefinition.addOrReplaceChild("mound", CubeListBuilder.create()
		.texOffs(4, 28).addBox(-5.0F, -3.5F, -5.0F, 10.0F, 3.5F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition extra1 = mound.addOrReplaceChild("extra1", CubeListBuilder.create(), PartPose.offset(-3.5F, -1.9F, -4.0F));
		PartDefinition extra1_r1 = extra1.addOrReplaceChild("extra1_r1", CubeListBuilder.create()
		.texOffs(35, 29).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 4.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));
		PartDefinition extra1_r2 = extra1.addOrReplaceChild("extra1_r2", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 2.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, -1.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition extra2 = mound.addOrReplaceChild("extra2", CubeListBuilder.create(), PartPose.offset(-5.0F, -0.9F, 1.5F));
		PartDefinition extra2_r1 = extra2.addOrReplaceChild("extra2_r1", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 2.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition extra3 = mound.addOrReplaceChild("extra3", CubeListBuilder.create()
		.texOffs(45, 38).addBox(4.0F, -2.4F, -1.0F, 3.0F, 2.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition extra3_r1 = extra3.addOrReplaceChild("extra3_r1", CubeListBuilder.create()
		.texOffs(35, 29).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 4.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.9F, 4.0F, 0.6155F, 0.5236F, 0.9553F));
		PartDefinition extra3_r2 = extra3.addOrReplaceChild("extra3_r2", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 2.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -0.9F, 5.5F, 0.0F, -0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(DirtTrail entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		int bits = entity.getUUID().hashCode();
		this.extra1.visible = (bits & 1) != 0;
		this.extra2.visible = (bits & 2) != 0;
		this.extra3.visible = (bits & 4) != 0;

		this.animateUnblended(entity.spawnAnimationState, DirtTrailAnimation.DIRT_TRAIL_SPAWN, ageInTicks);
	}
}
