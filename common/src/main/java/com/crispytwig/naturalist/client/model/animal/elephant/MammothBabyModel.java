package com.crispytwig.naturalist.client.model.animal.elephant;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ElephantBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.crispytwig.naturalist.world.entity.TerrainLegSolver;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.IKEntityModel;

public class MammothBabyModel extends IKEntityModel<Elephant> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("mammoth_baby"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart saddle;
	private final ModelPart neck;
	private final ModelPart asleep;
    private final ModelPart rightArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart leftArm;

	public MammothBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.saddle = this.body.getChild("saddle");
		this.neck = this.body.getChild("neck");
		this.asleep = this.neck.getChild("asleep");
        ModelPart legs = this.root.getChild("legs");
		this.rightArm = legs.getChild("rightArm");
		this.rightLeg = legs.getChild("rightLeg");
		this.leftLeg = legs.getChild("leftLeg");
		this.leftArm = legs.getChild("leftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-6.0F, -9.0F, -8.0F, 12.0F, 13.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-6.0F, 4.0F, -8.0F, 12.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.25F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create()
		.texOffs(43, 33).addBox(-6.0F, -7.0F, -8.0F, 12.0F, 13.0F, 16.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 29).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 8.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 42).addBox(-5.0F, -9.125F, -7.75F, 10.0F, 11.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-4.5F, -16.125F, -3.75F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(36, 62).addBox(-5.0F, -9.125F, -7.75F, 10.0F, 11.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -2.875F, -6.25F));
		PartDefinition asleep = neck.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 61).addBox(-5.0F, -26.0F, -13.75F, 10.0F, 11.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.875F, 6.0F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(36, 42).addBox(-0.5F, 0.0F, 0.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -8.125F, -4.25F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(36, 42).mirror().addBox(-5.5F, 0.0F, 0.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -8.125F, -4.25F));
		PartDefinition trunk = neck.addOrReplaceChild("trunk", CubeListBuilder.create()
		.texOffs(56, 16).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.125F, -7.75F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(3.0F, -11.0F, -4.75F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(56, 0).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(56, 0).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, 9.75F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(56, 0).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.75F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(56, 0).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Elephant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.saddle.visible = entity.isSaddled();
		this.asleep.visible = false;

		float tuned = 4.4F;
		this.animateIdleSmooth(entity.idleAnimationState, ElephantBabyAnimation.ELEPHANT_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, ElephantBabyAnimation.ELEPHANT_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, tuned));
		this.animateSmooth(entity.runAnimationState, ElephantBabyAnimation.ELEPHANT_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, tuned));

		this.articulateLegs(entity, partialTick);

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}

	@Override
	protected TerrainLegSolver getLegSolver(Elephant entity) {
		return entity.legSolver;
	}

	@Override
	protected ModelPart bodyPart() {
		return this.body;
	}

	@Override
	protected ModelPart headPart() {
		return this.neck;
	}

	@Override
	protected ModelPart[] legParts() {
		return new ModelPart[]{this.leftLeg, this.rightLeg, this.leftArm, this.rightArm};
	}
}
