package com.crispytwig.naturalist.client.model.animal.capybara;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.CapybaraAnimation;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
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

public class CapybaraModel extends NaturalistEntityModel<Capybara> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("capybara"), "main");
	private final ModelPart root;
	private final ModelPart skull;

	public CapybaraModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.skull = this.root.getChild("body").getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.5F, -5.0F, -9.0F, 9.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 28).addBox(-2.5F, -6.0F, -9.5F, 5.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -7.5F));
		PartDefinition leftEar = skull.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(11, 0).addBox(-0.45F, -1.5F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.15F, -4.5F, 0.9F, 0.0F, 0.7854F, 0.0F));
		PartDefinition rightEar = skull.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(11, 0).mirror().addBox(-0.55F, -1.5F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.15F, -4.5F, 0.9F, 0.0F, -0.7854F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(32, 28).addBox(-1.5F, -2.5F, -2.0F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.5F, -5.0F));
		PartDefinition rightArm = root.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(32, 28).mirror().addBox(-1.5F, -2.5F, -2.0F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -6.5F, -5.0F));
		PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-1.5F, -2.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -8.0F, 8.5F));
		PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-1.5F, -2.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -8.0F, 8.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Capybara entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.animateSmooth(entity.sitAnimationState, CapybaraAnimation.CAPYBARA_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.sleepAnimationState, CapybaraAnimation.CAPYBARA_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.sleep2AnimationState, CapybaraAnimation.CAPYBARA_SLEEP2, ageInTicks, partialTick);

		this.animateSmooth(entity.swimAnimationState, CapybaraAnimation.CAPYBARA_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.walkAnimationState, CapybaraAnimation.CAPYBARA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.0F));
		this.animateSmooth(entity.runAnimationState, CapybaraAnimation.CAPYBARA_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 4.0F));

		if (!entity.isSleeping() && !entity.isInSittingPose()) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
