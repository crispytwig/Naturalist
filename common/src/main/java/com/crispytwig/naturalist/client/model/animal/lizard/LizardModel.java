package com.crispytwig.naturalist.client.model.animal.lizard;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.LizardAnimation;
import com.crispytwig.naturalist.world.entity.animal.lizard.Lizard;
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

public class LizardModel extends NaturalistEntityModel<Lizard> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("lizard"), "main");
	private final ModelPart root;
    private final ModelPart neck;
	private final ModelPart tail;

	public LizardModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
        ModelPart skullRot = body.getChild("skullRot");
		this.neck = skullRot.getChild("neck");
		this.tail = body.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.25F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, 0.0F));
		PartDefinition beardie_body = body.addOrReplaceChild("beardie_body", CubeListBuilder.create()
		.texOffs(28, 20).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition basilisk_body = body.addOrReplaceChild("basilisk_body", CubeListBuilder.create()
		.texOffs(33, 0).addBox(0.0F, -12.0F, -5.0F, 0.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -5.0F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(8, 19).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-2.0F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.5F, -1.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition neck_r2 = neck.addOrReplaceChild("neck_r2", CubeListBuilder.create()
		.texOffs(0, 0).addBox(0.0F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(3.0F, -1.5F, -1.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition sleep = neck.addOrReplaceChild("sleep", CubeListBuilder.create()
		.texOffs(8, 52).addBox(-3.025F, -5.0F, -10.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 52).mirror().addBox(3.025F, -5.0F, -10.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 2.0F, 5.0F));
		PartDefinition lower_jaw = neck.addOrReplaceChild("lower_jaw", CubeListBuilder.create()
		.texOffs(33, 54).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -2.0F, -2.0F));
		PartDefinition beardie_head = neck.addOrReplaceChild("beardie_head", CubeListBuilder.create()
		.texOffs(49, 1).mirror().addBox(-3.0F, -11.0F, -11.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.02F)).mirror(false)
		.texOffs(49, 1).addBox(2.0F, -11.0F, -11.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.02F))
		.texOffs(8, 31).addBox(-5.0F, -12.0F, -7.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 6.0F, 5.0F));
		PartDefinition gecko = neck.addOrReplaceChild("gecko", CubeListBuilder.create()
		.texOffs(22, 3).addBox(2.0F, -11.0F, -10.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.02F))
		.texOffs(22, 3).mirror().addBox(-4.0F, -11.0F, -10.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offset(0.0F, 6.0F, 5.0F));
		PartDefinition sleep_gecko = gecko.addOrReplaceChild("sleep_gecko", CubeListBuilder.create()
		.texOffs(0, 58).mirror().addBox(-4.0F, -7.0F, -10.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.04F)).mirror(false)
		.texOffs(0, 58).addBox(2.0F, -7.0F, -10.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.04F)), PartPose.offset(0.0F, -4.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(28, 35).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 5.0F));
		PartDefinition basilisk_tail = tail.addOrReplaceChild("basilisk_tail", CubeListBuilder.create()
		.texOffs(32, 5).addBox(0.0F, -10.0F, 5.0F, 0.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -5.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(0, 19).addBox(0.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.0F, -2.5F, 0.0894F, 0.3829F, 0.6282F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(0, 19).mirror().addBox(-5.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -4.0F, -2.5F, 0.0894F, -0.3829F, -0.6282F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(0, 19).addBox(0.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.0F, 2.5F, -0.0894F, -0.3829F, 0.6282F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(0, 19).mirror().addBox(-5.0F, 0.0F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -4.0F, 2.5F, -0.0894F, 0.3829F, -0.6282F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Lizard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.tail.visible = entity.hasTail();

		this.animateSmooth(entity.sitAnimationState, LizardAnimation.LIZARD_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.walkAnimationState, LizardAnimation.LIZARD_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 4.5F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
