package com.crispytwig.naturalist.client.model.animal.mole;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.MoleAnimation;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
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

public class MoleModel extends NaturalistEntityModel<Mole> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("mole"), "main");
	private final ModelPart modelRoot;
    private final ModelPart skull;

	public MoleModel(ModelPart root) {
		super(root);
		this.modelRoot = root;
		this.skull = root.getChild("root").getChild("body").getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.0F, -3.0F, -4.5F, 8.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 1.5F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(15, 19).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 4.5F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 16).addBox(-3.0F, -2.5F, -3.6333F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -4.8667F));
		PartDefinition nose = skull.addOrReplaceChild("nose", CubeListBuilder.create()
		.texOffs(26, 5).addBox(-2.5F, -2.0F, -2.1F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -3.6333F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(4.0F, -2.5F, -2.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(24, 16).addBox(0.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(24, 16).mirror().addBox(-6.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(0, 25).addBox(0.0F, -1.5F, -0.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 8.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(0, 25).mirror().addBox(-4.0F, -1.5F, -0.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, 1.0F, 8.0F));
		PartDefinition mound = partdefinition.addOrReplaceChild("mound", CubeListBuilder.create()
		.texOffs(4, 28).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(45, 38).addBox(4.0F, -1.9F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition mound_r1 = mound.addOrReplaceChild("mound_r1", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -0.9F, 5.5F, 0.0F, -0.3927F, 0.0F));
		PartDefinition mound_r2 = mound.addOrReplaceChild("mound_r2", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -0.9F, -5.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition mound_r3 = mound.addOrReplaceChild("mound_r3", CubeListBuilder.create()
		.texOffs(45, 38).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -0.9F, 1.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition mound_r4 = mound.addOrReplaceChild("mound_r4", CubeListBuilder.create()
		.texOffs(35, 29).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.9F, -4.0F, 0.0F, -1.1781F, 0.0F));
		PartDefinition mound_r5 = mound.addOrReplaceChild("mound_r5", CubeListBuilder.create()
		.texOffs(35, 29).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.9F, 4.0F, 0.6155F, 0.5236F, 0.9553F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Mole entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.modelRoot.getChild("mound").visible = entity.isRolledUp();

		this.animateSmooth(entity.digDownAnimationState, MoleAnimation.MOLE_DIG_DOWN, ageInTicks, partialTick);
		this.animateSmooth(entity.undergroundAnimationState, MoleAnimation.MOLE_UNDERGROUND_HIDDEN, ageInTicks, partialTick);
		this.animateSmooth(entity.spawnAnimationState, MoleAnimation.MOLE_SPAWN, ageInTicks, partialTick);
		this.animateSmooth(entity.peekAnimationState, MoleAnimation.MOLE_PEEK, ageInTicks, partialTick);
		this.animateSmooth(entity.digUpAnimationState, MoleAnimation.MOLE_DIG_UP, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, MoleAnimation.MOLE_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, MoleAnimation.MOLE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.0F));

		if (!entity.isRolledUp()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
