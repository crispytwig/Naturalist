package com.crispytwig.naturalist.client.model.animal.tiger;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.TigerAnimation;
import com.crispytwig.naturalist.world.entity.animal.tiger.Tiger;
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

public class TigerModel extends NaturalistEntityModel<Tiger> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("tiger"), "main");
	private final ModelPart root;
    private final ModelPart skullRot;
    private final ModelPart sleep;

	public TigerModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.skullRot = body.getChild("skullRot");
        ModelPart skull = this.skullRot.getChild("skull");
		this.sleep = skull.getChild("sleep");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -6.0F, -13.0F, 10.0F, 12.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.6F, -1.5F));
		PartDefinition skullRot = body.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, -3.9F, -12.875F));
		PartDefinition skull = skullRot.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(46, 12).addBox(2.0F, -2.5F, -4.125F, 5.0F, 9.0F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(46, 12).mirror().addBox(-7.0F, -2.5F, -4.125F, 5.0F, 9.0F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 38).addBox(-5.0F, -3.5F, -7.125F, 10.0F, 8.0F, 7.0F, new CubeDeformation(0.02F))
		.texOffs(46, 0).addBox(-2.5F, 0.5F, -9.125F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(62, 0).addBox(-2.5F, 0.5F, -9.125F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition sleep = skull.addOrReplaceChild("sleep", CubeListBuilder.create()
		.texOffs(6, 59).addBox(0.95F, -21.05F, -21.525F, 2.1F, 1.1F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 59).mirror().addBox(-3.05F, -21.05F, -21.525F, 2.1F, 1.1F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 20.5F, 14.35F));
		PartDefinition right_ears = skull.addOrReplaceChild("right_ears", CubeListBuilder.create()
		.texOffs(0, 19).mirror().addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -3.0F, -4.625F));
		PartDefinition left_ears = skull.addOrReplaceChild("left_ears", CubeListBuilder.create()
		.texOffs(0, 19).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -3.0F, -4.625F));
		PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create()
		.texOffs(18, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -4.4F, 11.5F, 1.5708F, 0.0F, 0.0F));
		PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(16, 17).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create()
		.texOffs(16, 17).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -22.0F, -15.0F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 1.0F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(34, 38).mirror().addBox(-2.0F, -1.9F, -2.5F, 4.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.75F, 7.9F, 5.5F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(34, 38).addBox(-2.0F, -1.9F, -2.5F, 4.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.75F, 7.9F, 5.5F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.0F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-2.0F, -1.5F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.1F, 9.5F, 18.5F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.1F, 9.5F, 18.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Tiger entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean sleeping = entity.isSleeping() || entity.isInSittingPose();
		this.sleep.visible = sleeping;

		this.animateSmooth(entity.attackAnimationState, TigerAnimation.TIGER_ATTACK, ageInTicks, partialTick);
		this.animateSmooth(entity.sleepAnimationState, TigerAnimation.TIGER_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sleep2AnimationState, TigerAnimation.TIGER_SLEEP2, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, TigerAnimation.TIGER_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, TigerAnimation.TIGER_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));
		this.animateSmooth(entity.preyAnimationState, TigerAnimation.TIGER_PREY, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 0.8F));
		this.animateSmooth(entity.runAnimationState, TigerAnimation.TIGER_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));

		if (!sleeping) {
			applyHeadLook(this.skullRot, netHeadYaw, headPitch);
		}
	}
}
