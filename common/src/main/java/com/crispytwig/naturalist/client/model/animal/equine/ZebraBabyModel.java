package com.crispytwig.naturalist.client.model.animal.equine;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ZebraBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.equine.Zebra;
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

public class ZebraBabyModel extends NaturalistEntityModel<Zebra> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("zebra_baby"), "main");
	private final ModelPart root;
    private final ModelPart neck;

	public ZebraBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.neck = body.getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-0.375F, -8.5F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(26, 33).addBox(0.25F, -0.5F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.875F, 0.0F, -5.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(26, 33).mirror().addBox(-0.75F, -0.5F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.125F, 0.0F, -5.5F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(26, 33).addBox(-0.5F, -0.5F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.125F, 0.0F, 5.5F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(26, 33).addBox(-1.5F, -0.5F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.875F, 0.0F, 5.5F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -3.5F, -7.0F, 6.0F, 7.0F, 14.0F, new CubeDeformation(0.026F)), PartPose.offset(0.0F, -12.5F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(26, 21).addBox(-1.25F, -6.2078F, -1.4434F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.984F, -6.3572F, 0.3927F, 0.0F, 0.0F));
		PartDefinition skull2 = neck.addOrReplaceChild("skull2", CubeListBuilder.create()
		.texOffs(0, 21).addBox(-2.0F, -3.6918F, -6.8006F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, -6.516F, 0.3572F));
		PartDefinition leftEar = skull2.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(12, 34).mirror().addBox(-1.0F, -4.1918F, -0.8006F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -3.5F, 2.0F));
		PartDefinition rightEar = skull2.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(12, 34).addBox(-1.0F, -4.1918F, -0.8006F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.5F, 2.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(8, 34).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-1.0F, 7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 7.0F, 0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Zebra entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, ZebraBabyAnimation.ZEBRA_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, ZebraBabyAnimation.ZEBRA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.0F));
		this.animateSmooth(entity.runAnimationState, ZebraBabyAnimation.ZEBRA_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
