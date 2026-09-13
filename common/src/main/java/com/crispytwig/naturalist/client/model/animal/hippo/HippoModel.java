package com.crispytwig.naturalist.client.model.animal.hippo;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.HippoAnimation;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class HippoModel extends NaturalistEntityModel<Hippo> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("hippo"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart bone;
	private final ModelPart neck;
	private final ModelPart botJaw;

	public HippoModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.bone = this.body.getChild("bone");
		this.neck = this.bone.getChild("neck");
		this.botJaw = this.neck.getChild("botJaw");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(8, 66).addBox(-11.0F, -9.5F, -16.0F, 22.0F, 19.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.5F, 7.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(111, 63).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 18.0F, 0.1745F, 0.0F, 0.0F));
		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -17.0F));
		PartDefinition neck = bone.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition topJaw = neck.addOrReplaceChild("topJaw", CubeListBuilder.create()
		.texOffs(76, 23).addBox(-6.5F, -6.0F, -19.0F, 13.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.5F, -7.0F, -9.0F, 13.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(35, 0).addBox(2.5F, -8.0F, -9.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(35, 31).addBox(-6.5F, -8.0F, -9.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.5F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(5.5F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 49).addBox(3.5F, 0.0F, -15.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 49).mirror().addBox(-5.5F, 0.0F, -15.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 11).addBox(-6.5F, 0.0F, -5.0F, 13.0F, 5.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 1.0F));
		PartDefinition botJaw = neck.addOrReplaceChild("botJaw", CubeListBuilder.create()
		.texOffs(76, 44).addBox(-6.5F, 0.0F, -14.0F, 13.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(77, 46).addBox(2.5F, -5.0F, -13.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(77, 46).mirror().addBox(-4.5F, -5.0F, -13.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 21).addBox(-6.5F, 0.0F, -4.0F, 13.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));
		PartDefinition skinFlap = botJaw.addOrReplaceChild("skinFlap", CubeListBuilder.create()
		.texOffs(82, 7).addBox(-6.5F, -4.5F, -2.0F, 13.0F, 7.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -1.5F, -1.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(3, 68).addBox(-3.0F, -1.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -9.0F, -3.5F));
		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(3, 68).mirror().addBox(-3.0F, -1.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -9.0F, -3.5F));
		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(3, 68).addBox(-3.0F, -1.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -9.0F, 20.5F));
		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(3, 68).mirror().addBox(-3.0F, -1.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -9.0F, 19.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Hippo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.biteAnimationState, HippoAnimation.HIPPO_BITE, ageInTicks, partialTick);
		this.animateSmooth(entity.sleepAnimationState, HippoAnimation.HIPPO_SLEEP, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, HippoAnimation.HIPPO_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.8F);
		this.animateIdleSmooth(entity.swimIdleAnimationState, HippoAnimation.HIPPO_SWIM_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 0.8F);
		this.animateSmooth(entity.walkAnimationState, HippoAnimation.HIPPO_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));
		this.animateSmooth(entity.runAnimationState, HippoAnimation.HIPPO_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.8F));
		this.animateSmooth(entity.swimAnimationState, HippoAnimation.HIPPO_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.1F, LARGE_SWIMMER_LIMB_SWING));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}

	public void translateToBotJaw(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		this.bone.translateAndRotate(poseStack);
		this.neck.translateAndRotate(poseStack);
		this.botJaw.translateAndRotate(poseStack);
	}
}
