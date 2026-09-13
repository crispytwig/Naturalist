package com.crispytwig.naturalist.client.model.animal.starfish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.StarfishAnimation;
import com.crispytwig.naturalist.world.entity.animal.starfish.Starfish;
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

public class StarfishModel extends NaturalistEntityModel<Starfish> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("starfish"), "main");
	private final ModelPart root;

	public StarfishModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.0F));
		PartDefinition skull_r1 = skull.addOrReplaceChild("skull_r1", CubeListBuilder.create()
		.texOffs(19, 5).addBox(-2.0F, -2.0F, -11.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 1.0F, -6.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, 1.25F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm_r1 = leftArm.addOrReplaceChild("leftArm_r1", CubeListBuilder.create()
		.texOffs(16, 13).mirror().addBox(-2.0F, -2.0F, -4.5F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.9635F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));
		PartDefinition rightArm_r1 = rightArm.addOrReplaceChild("rightArm_r1", CubeListBuilder.create()
		.texOffs(16, 13).addBox(-2.0F, -2.0F, -4.5F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.9635F, 0.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, -3.5F));
		PartDefinition leftLeg_r1 = leftLeg.addOrReplaceChild("leftLeg_r1", CubeListBuilder.create()
		.texOffs(1, 11).mirror().addBox(-2.0F, -2.0F, -0.75F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -3.5F));
		PartDefinition rightLeg_r1 = rightLeg.addOrReplaceChild("rightLeg_r1", CubeListBuilder.create()
		.texOffs(1, 11).addBox(-2.0F, -2.0F, -0.75F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		return LayerDefinition.create(meshdefinition, 48, 32);
	}

	@Override
	protected void setupAnimations(Starfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.idleAnimationState, StarfishAnimation.STARFISH_IDLE, ageInTicks, partialTick);
	}
}
