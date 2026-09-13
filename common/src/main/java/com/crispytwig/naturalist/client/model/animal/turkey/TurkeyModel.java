package com.crispytwig.naturalist.client.model.animal.turkey;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.TurkeyAnimation;
import com.crispytwig.naturalist.world.entity.animal.turkey.Turkey;
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

public class TurkeyModel extends NaturalistEntityModel<Turkey> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("turkey"), "main");
	private final ModelPart root;
    private final ModelPart skull;

	public TurkeyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skull = body.getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-2.0F, -3.25F, 1.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(12, 9).mirror().addBox(-1.5F, 0.25F, -3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 6).mirror().addBox(-1.5F, 0.25F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(12, 9).addBox(-1.5F, 0.25F, -3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(24, 6).addBox(-1.5F, 0.25F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 14).addBox(-4.5F, -5.0F, -6.0F, 9.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -1.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -8.0F, -2.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-0.5F, -7.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(-1.0F, -6.0F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -6.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 36).addBox(-5.5F, -4.0F, -1.0F, 11.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(-12.5F, -13.0F, 1.1F, 25.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.75F, 6.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(43, 19).mirror().addBox(-0.5F, -0.5F, -3.5F, 1.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 0.5F, -1.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(43, 19).addBox(-0.5F, -0.5F, -3.5F, 1.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.5F, -1.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Turkey entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.peckAnimationState, TurkeyAnimation.TURKEY_PICKING_ON_GROUND, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, TurkeyAnimation.TURKEY_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, TurkeyAnimation.TURKEY_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));
		this.animateSmooth(entity.runAnimationState, TurkeyAnimation.TURKEY_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F));

		applyHeadLook(this.skull, netHeadYaw, headPitch);
	}
}
