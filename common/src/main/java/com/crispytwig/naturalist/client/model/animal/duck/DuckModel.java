package com.crispytwig.naturalist.client.model.animal.duck;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DuckAnimation;
import com.crispytwig.naturalist.world.entity.animal.duck.Duck;
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

public class DuckModel extends NaturalistEntityModel<Duck> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("duck"), "main");
	private final ModelPart root;
	private final ModelPart neck;

	public DuckModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -2.0F, -3.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -0.5F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(27, 5).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.5F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(52, 11).addBox(-1.5F, -7.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -3.0F));
		PartDefinition bill = neck.addOrReplaceChild("bill", CubeListBuilder.create()
		.texOffs(26, 0).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, -1.5F));
		PartDefinition bowtie = neck.addOrReplaceChild("bowtie", CubeListBuilder.create()
		.texOffs(0, 19).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -1.75F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(42, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -2.0F, 0.5F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(42, 0).mirror().addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -2.0F, 0.5F));
		PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(19, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -4.0F, 1.0F));
		PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot", CubeListBuilder.create()
		.texOffs(29, 12).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(38, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -4.0F, 1.0F));
		PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot", CubeListBuilder.create()
		.texOffs(29, 12).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected void setupAnimations(Duck entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.sitAnimationState, DuckAnimation.DUCK_SIT, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, DuckAnimation.DUCK_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.swimAnimationState, DuckAnimation.DUCK_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.walkAnimationState, DuckAnimation.DUCK_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));
		this.animateSmooth(entity.runAnimationState, DuckAnimation.DUCK_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.flapAnimationState, DuckAnimation.DUCK_FLAP, ageInTicks, partialTick);

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
