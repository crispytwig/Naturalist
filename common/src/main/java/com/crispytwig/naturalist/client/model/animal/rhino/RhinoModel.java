package com.crispytwig.naturalist.client.model.animal.rhino;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.RhinoAnimation;
import com.crispytwig.naturalist.world.entity.animal.rhino.Rhino;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class RhinoModel extends NaturalistEntityModel<Rhino> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("rhino"), "main");
	private final ModelPart modelRoot;
    private final ModelPart neck;
	private final ModelPart baby_horn;
	private final ModelPart big_horn;
	private final ModelPart small_horn;
	private final ModelPart right_ear;
	private final ModelPart left_ear;

	public RhinoModel(ModelPart root) {
		super(root);
		this.modelRoot = root;
		this.neck = root.getChild("root").getChild("body").getChild("attack").getChild("skullRot").getChild("neck");
		this.baby_horn = this.neck.getChild("baby_horn");
		this.big_horn = this.neck.getChild("big_horn");
		this.small_horn = this.neck.getChild("small_horn");
		this.right_ear = this.neck.getChild("right_ear");
		this.left_ear = this.neck.getChild("left_ear");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -8.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-12.0F, -15.5F, -24.0F, 24.0F, 29.0F, 27.0F, new CubeDeformation(0.0F))
		.texOffs(0, 57).addBox(-10.0F, -11.5F, 3.0F, 20.0F, 25.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -27.5F, 11.0F));
		PartDefinition attack = body.addOrReplaceChild("attack", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, -24.5F));
		PartDefinition skullRot = attack.addOrReplaceChild("skullRot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck = skullRot.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));
		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create()
		.texOffs(76, 0).addBox(-5.0F, -30.0F, -43.0F, 10.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(71, 57).addBox(-8.0F, -38.0F, -31.0F, 16.0F, 18.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.0671F, 25.5648F, 0.2618F, 0.0F, 0.0F));
		PartDefinition baby_horn = neck.addOrReplaceChild("baby_horn", CubeListBuilder.create()
		.texOffs(1, 100).addBox(-2.5F, -32.0F, -37.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.7222F, 18.9928F, 0.2618F, 0.0F, 0.0F));
		PartDefinition big_horn = neck.addOrReplaceChild("big_horn", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.5F, -32.0F, -37.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 15.8245F, 18.2164F, 0.2618F, 0.0F, 0.0F));
		PartDefinition small_horn = neck.addOrReplaceChild("small_horn", CubeListBuilder.create()
		.texOffs(0, 57).addBox(-1.5F, -29.0F, -30.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.8245F, 18.2164F, 0.2618F, 0.0F, 0.0F));
		PartDefinition right_ear = neck.addOrReplaceChild("right_ear", CubeListBuilder.create()
		.texOffs(0, 14).mirror().addBox(-3.5F, -8.0F, -1.0F, 5.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -10.6852F, -6.4866F, 0.2618F, 0.0F, 0.0F));
		PartDefinition left_ear = neck.addOrReplaceChild("left_ear", CubeListBuilder.create()
		.texOffs(0, 14).addBox(-1.5F, -8.0F, -1.0F, 5.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -10.6852F, -6.4866F, 0.2618F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm = front_legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(60, 88).addBox(-4.0F, 0.0F, -5.5F, 8.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -14.0F, -5.5F));
		PartDefinition right_arm = front_legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(60, 88).addBox(-4.0F, 0.0F, -5.5F, 8.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -14.0F, -5.5F));
		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, 27.0F));
		PartDefinition left_leg = back_legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(60, 88).addBox(-4.0F, 0.0F, -5.5F, 8.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -14.0F, -5.5F));
		PartDefinition right_leg = back_legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(60, 88).addBox(-4.0F, 0.0F, -5.5F, 8.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -14.0F, -5.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Rhino entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		boolean baby = entity.isBaby();
		float headScale = baby ? 1.4F : 1.0F;
		this.neck.xScale = headScale;
		this.neck.yScale = headScale;
		this.neck.zScale = headScale;

		float earScale = baby ? 1.1F : 1.0F;
		this.left_ear.xScale = earScale;
		this.left_ear.yScale = earScale;
		this.left_ear.zScale = earScale;
		this.right_ear.xScale = earScale;
		this.right_ear.yScale = earScale;
		this.right_ear.zScale = earScale;

		this.big_horn.visible = !baby;
		this.small_horn.visible = !baby;
		this.baby_horn.visible = baby;

		this.animateSmooth(entity.attackAnimationState, RhinoAnimation.RHINO_ATTACK, ageInTicks, partialTick, 1.3F);
		this.animateSmooth(entity.stunnedAnimationState, RhinoAnimation.RHINO_STUNNED, ageInTicks, partialTick);
		this.animateSmooth(entity.footAnimationState, RhinoAnimation.RHINO_FOOT, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, RhinoAnimation.RHINO_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, RhinoAnimation.RHINO_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.2F));
		this.animateSmooth(entity.runAnimationState, RhinoAnimation.RHINO_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.6F));

		if (!entity.isSprinting()) {
			this.neck.yRot += netHeadYaw * Mth.DEG_TO_RAD;
		}
	}
}
