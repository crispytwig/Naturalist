package com.crispytwig.naturalist.client.model.animal.giraffe;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.GiraffeBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.giraffe.Giraffe;
import com.crispytwig.naturalist.world.entity.TerrainLegSolver;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.IKEntityModel;

public class GiraffeBabyModel extends IKEntityModel<Giraffe> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("giraffe_baby"), "main");
	private final ModelPart root;
	private final ModelPart body;
    private final ModelPart rightLeg;
	private final ModelPart leftLeg;
    private final ModelPart rightArm;
	private final ModelPart leftArm;

	public GiraffeBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
        ModelPart legs = this.root.getChild("legs");
        ModelPart backLegs = legs.getChild("backLegs");
		this.rightLeg = backLegs.getChild("rightLeg");
		this.leftLeg = backLegs.getChild("leftLeg");
        ModelPart frontLegs = legs.getChild("frontLegs");
		this.rightArm = frontLegs.getChild("rightArm");
		this.leftArm = frontLegs.getChild("leftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.5F, -4.0F, -6.5F, 7.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.5F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 6.5F));
		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
		.texOffs(-13, 41).addBox(-3.5F, -22.0F, 7.0F, 7.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0724F, -22.9936F, -1.3526F, 0.0F, 0.0F));
		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 21).addBox(-1.5F, -13.0F, -3.5F, 3.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.5F));
		PartDefinition bone = neck.addOrReplaceChild("bone", CubeListBuilder.create()
		.texOffs(16, 32).addBox(-0.5F, -37.0F, -3.0F, 1.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 4.5F));
		PartDefinition skull2 = neck.addOrReplaceChild("skull2", CubeListBuilder.create()
		.texOffs(36, 32).addBox(-2.0F, -3.0F, -9.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(-2.0F, -3.0F, -9.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.25F))
		.texOffs(16, 21).addBox(-3.0F, -5.0F, -4.5F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.025F))
		.texOffs(40, 11).addBox(-3.0F, -8.0F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 16).addBox(-3.0F, -8.0F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F))
		.texOffs(40, 11).mirror().addBox(1.0F, -8.0F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(40, 16).mirror().addBox(1.0F, -8.0F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, -12.0F, 0.0F));
		PartDefinition rightEar = skull2.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(40, 8).addBox(-4.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -4.0F, 0.0F));
		PartDefinition leftEar = skull2.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(40, 8).mirror().addBox(0.5F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5F, -4.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-1.5F, 2.0F, 0.5F));
		PartDefinition backLegs = legs.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.0F));
		PartDefinition rightLeg = backLegs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(26, 32).addBox(-1.5F, 1.0F, -1.5F, 2.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = backLegs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(26, 32).mirror().addBox(-0.5F, 1.0F, -1.5F, 2.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 0.0F, 0.0F));
		PartDefinition frontLegs = legs.addOrReplaceChild("frontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -5.0F));
		PartDefinition rightArm = frontLegs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(26, 32).addBox(-1.5F, 1.0F, -1.5F, 2.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = frontLegs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(26, 32).mirror().addBox(-0.5F, 1.0F, -1.5F, 2.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Giraffe entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, GiraffeBabyAnimation.GIRAFFE_IDLE, ageInTicks, partialTick, limbSwingAmount, IDLE_FADE_SCALE, 1.4F);
		this.animateSmooth(entity.walkAnimationState, GiraffeBabyAnimation.GIRAFFE_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.56F));
		this.animateSmooth(entity.runAnimationState, GiraffeBabyAnimation.GIRAFFE_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.56F));

		this.articulateLegs(entity, partialTick);
	}

	@Override
	protected TerrainLegSolver getLegSolver(Giraffe entity) {
		return entity.legSolver;
	}

	@Override
	protected ModelPart bodyPart() {
		return this.body;
	}

	@Nullable
	@Override
	protected ModelPart headPart() {
		return null;
	}

	@Override
	protected ModelPart[] legParts() {
		return new ModelPart[]{this.leftLeg, this.rightLeg, this.leftArm, this.rightArm};
	}
}
