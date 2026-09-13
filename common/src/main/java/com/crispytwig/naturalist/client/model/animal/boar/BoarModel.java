package com.crispytwig.naturalist.client.model.animal.boar;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BoarAnimation;
import com.crispytwig.naturalist.world.entity.animal.boar.Boar;
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

public class BoarModel extends NaturalistEntityModel<Boar> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("boar"), "main");
	private final ModelPart root;
	private final ModelPart neck;

	public BoarModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create()
		.texOffs(36, 25).addBox(0.0F, -23.0F, 4.0F, 0.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -23.0F, -5.0F, 10.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 15.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -9.0F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(0, 25).addBox(-4.0F, -14.7071F, -18.2929F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-6.0F, -14.7071F, -14.2929F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).mirror().addBox(4.0F, -14.7071F, -14.2929F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 20).mirror().addBox(-6.5F, -18.705F, -8.8772F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 20).addBox(3.5F, -18.705F, -8.8772F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.8284F, 15.5564F, 0.7854F, 0.0F, 0.0F));
		PartDefinition tusks = neck.addOrReplaceChild("tusks", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.2426F, -5.6569F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tusks_r1 = tusks.addOrReplaceChild("tusks_r1", CubeListBuilder.create()
		.texOffs(38, 11).addBox(4.0F, 2.9076F, -5.1931F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(38, 11).mirror().addBox(-4.0F, 2.9076F, -5.1931F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 4.3424F, 1.1931F, -2.3562F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(3.0F, 5.0F, -5.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(38, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(38, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 12.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(38, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 12.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(38, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Boar entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, BoarAnimation.BOAR_ATTACK, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, BoarAnimation.BOAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BoarAnimation.BOAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, BoarAnimation.BOAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
