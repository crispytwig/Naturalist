package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.RayAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
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

public class RayModel extends NaturalistEntityModel<Ray> {
	private static final float COUNTER_GAIN = 1.6F;
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("ray"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart leftFin;
	private final ModelPart rightFin;
	private final ModelPart tail;

	public RayModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.leftFin = this.body.getChild("leftFin");
		this.rightFin = this.body.getChild("rightFin");
		this.tail = this.body.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.5F, -2.0F, -7.0F, 11.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(38, 4).addBox(0.0F, -3.0F, -1.0F, 0.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(23, 43).addBox(-2.5F, -1.0F, -10.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition mantaMouth = body.addOrReplaceChild("mantaMouth", CubeListBuilder.create(), PartPose.offset(4.5F, -0.5F, -7.0F));
		PartDefinition leftMouth = mantaMouth.addOrReplaceChild("leftMouth", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.0F, -1.5F, -4.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightMouth = mantaMouth.addOrReplaceChild("rightMouth", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -4.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, 0.0F, 0.0F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(0, 29).addBox(0.0F, 0.0F, -5.0F, 6.0F, 2.0F, 10.0F, new CubeDeformation(0.01F))
		.texOffs(49, 26).addBox(0.0F, 0.0F, -7.0F, 6.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 0.0F, 0.0F));
		PartDefinition leftFin2 = leftFin.addOrReplaceChild("leftFin2", CubeListBuilder.create()
		.texOffs(0, 19).addBox(0.0F, 0.0F, -4.0F, 13.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(51, 9).addBox(0.0F, 0.0F, -5.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, -1.0F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(0, 29).mirror().addBox(-6.0F, 0.0F, -5.0F, 6.0F, 2.0F, 10.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(49, 26).mirror().addBox(-6.0F, 0.0F, -7.0F, 6.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 0.0F, 0.0F));
		PartDefinition rightFin2 = rightFin.addOrReplaceChild("rightFin2", CubeListBuilder.create()
		.texOffs(0, 19).mirror().addBox(-13.0F, 0.0F, -4.0F, 13.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(51, 9).mirror().addBox(-5.0F, 0.0F, -5.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, -1.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(23, 29).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 8.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(37, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 7.0F));
		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create()
		.texOffs(54, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	protected void setupAnimations(Ray entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.idleAnimationState, RayAnimation.RAY_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, RayAnimation.RAY_SWIM_IDLE_EVENT, ageInTicks, partialTick);

		float bodyRot = entity.swimTilt.getSwimPitch(partialTick);
		float finCounter = (bodyRot - entity.getFinLag(partialTick)) * Mth.DEG_TO_RAD * COUNTER_GAIN;
		float tailCounter = (bodyRot - entity.getTailLag(partialTick)) * Mth.DEG_TO_RAD * COUNTER_GAIN;

		this.body.xRot += bodyRot * Mth.DEG_TO_RAD;
		this.leftFin.xRot -= finCounter;
		this.rightFin.xRot -= finCounter;
		this.tail.xRot -= tailCounter;
	}
}
