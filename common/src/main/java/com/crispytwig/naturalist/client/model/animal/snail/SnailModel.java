package com.crispytwig.naturalist.client.model.animal.snail;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.SnailAnimation;
import com.crispytwig.naturalist.world.entity.SurfaceClimbing;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class SnailModel extends NaturalistEntityModel<Snail> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("snail"), "main");
	private static final float TAIL_STRENGTH = -1.4F;
	private static final float TAIL_MAX = 0.6F;
	private static final float TAIL_LOOK_STRENGTH = 3.0F;
	private static final float MAX_EYE_PITCH = Mth.DEG_TO_RAD * 22.5F;
	private static final float MAX_EYE_YAW = Mth.DEG_TO_RAD * 90.0F;
	private final ModelPart root;
	private final ModelPart body;
    private final ModelPart eyes;
	private final ModelPart rightEye;
	private final ModelPart leftEye;
	private final ModelPart tail;
	private final ModelPart shell;

	public SnailModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
        ModelPart front = this.body.getChild("front");
		this.eyes = front.getChild("eyes");
		this.rightEye = this.eyes.getChild("rightEye");
		this.leftEye = this.eyes.getChild("leftEye");
		this.tail = this.body.getChild("tail");
		this.shell = this.root.getChild("shell");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 1.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create()
		.texOffs(0, 14).addBox(-1.5F, -2.0F, -7.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(23, 8).addBox(-2.0F, -2.0F, -7.25F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition eyes = front.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -7.0F));
		PartDefinition rightEye = eyes.addOrReplaceChild("rightEye", CubeListBuilder.create()
		.texOffs(20, 21).addBox(-1.5F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));
		PartDefinition leftEye = eyes.addOrReplaceChild("leftEye", CubeListBuilder.create()
		.texOffs(20, 21).mirror().addBox(-0.5F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(20, 14).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition shell = root.addOrReplaceChild("shell", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -3.5F, -3.5F, 4.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-2.0F, -3.5F, -3.5F, 4.0F, 7.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -4.5F, -0.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Snail entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		float eyeScale = entity.isBaby() ? 1.5F : 1.0F;
		this.eyes.xScale = eyeScale;
		this.eyes.yScale = eyeScale;
		this.eyes.zScale = eyeScale;

		this.animateSmooth(entity.hideAnimationState, SnailAnimation.SNAIL_HIDE_START, ageInTicks, partialTick);
		this.animateSmooth(entity.hideEndAnimationState, SnailAnimation.SNAIL_HIDE_END, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, SnailAnimation.SNAIL_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.crawlAnimationState, SnailAnimation.SNAIL_CRAWL, ageInTicks, partialTick,
				entity.isClimbing() ? 1.0F : movementAnimationSpeed(entity, limbSwingAmount, 5.0F));

		if (entity.isHidden()) {
			return;
		}
		this.applyLook(entity, partialTick);
	}

	private void applyLook(Snail entity, float partialTick) {
		float bodyLookYaw = entity.getBodyLookYaw(partialTick);
		float bodyYaw = bodyLookYaw * Mth.DEG_TO_RAD;
		this.body.yRot -= bodyYaw;
		this.shell.yRot -= bodyYaw;

		SurfaceClimbing climbing = entity.getClimbing();
		Vec3 normal = climbing.getRenderNormal(partialTick);
		Vec3 forward = climbing.getRenderForwardFlattened(partialTick, normal);
		Vec3 toCamera = Minecraft.getInstance().gameRenderer.mainCamera().position().subtract(entity.position());
		Vec3 plane = SurfaceClimbing.projectOntoPlane(toCamera, normal);
		double planeLen = plane.length();
		Vec3 cameraDir = planeLen < 1.0E-4D ? forward : plane.normalize();

		float eyeYaw = (float) Math.atan2(forward.cross(cameraDir).dot(normal), forward.dot(cameraDir)) - bodyYaw;
		eyeYaw = Mth.clamp(Mth.wrapDegrees(eyeYaw * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD, -MAX_EYE_YAW, MAX_EYE_YAW);
		float eyePitch = Mth.clamp((float) Math.atan2(toCamera.dot(normal), planeLen), -MAX_EYE_PITCH, MAX_EYE_PITCH);
		this.leftEye.xRot -= eyePitch;
		this.leftEye.yRot -= eyeYaw;
		this.rightEye.xRot -= eyePitch;
		this.rightEye.yRot -= eyeYaw;

		this.tail.xRot -= Mth.clamp(climbing.getTailLag(partialTick) * TAIL_STRENGTH, -TAIL_MAX, TAIL_MAX);
		this.tail.yRot -= (entity.getTailLookYaw(partialTick) - bodyLookYaw) * TAIL_LOOK_STRENGTH * Mth.DEG_TO_RAD;
	}
}
