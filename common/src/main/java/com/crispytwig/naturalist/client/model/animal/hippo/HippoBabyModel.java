package com.crispytwig.naturalist.client.model.animal.hippo;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.HippoBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
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

public class HippoBabyModel extends NaturalistEntityModel<Hippo> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("hippo_baby"), "main");
	private final ModelPart root;
    private final ModelPart neck;

	public HippoBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.neck = body.getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -9.0F, -2.0F, 10.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(28, 21).addBox(-4.0F, -3.0F, -3.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, -1.0F, -6.5F, 8.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -1.5F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(0, 31).addBox(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -1.5F, -1.5F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(0, 31).mirror().addBox(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -1.5F, -1.5F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(3.0F, -3.5F, 3.5F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(28, 26).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(28, 26).mirror().addBox(-1.5F, -0.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, 0.0F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(28, 26).mirror().addBox(-1.5F, -0.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, -7.0F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(28, 26).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Hippo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.biteAnimationState, HippoBabyAnimation.HIPPO_HEADBUTT, ageInTicks, partialTick);
		this.animateSmooth(entity.sitAnimationState, HippoBabyAnimation.HIPPO_SIT_IDLE, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, HippoBabyAnimation.HIPPO_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.8F);
		this.animateIdleSmooth(entity.swimIdleAnimationState, HippoBabyAnimation.HIPPO_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.8F);
		this.animateSmooth(entity.walkAnimationState, HippoBabyAnimation.HIPPO_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, HippoBabyAnimation.HIPPO_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.8F));
		this.animateSmooth(entity.swimAnimationState, HippoBabyAnimation.HIPPO_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.1F, LARGE_SWIMMER_LIMB_SWING));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
