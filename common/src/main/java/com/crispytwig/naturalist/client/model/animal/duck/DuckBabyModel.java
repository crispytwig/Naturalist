package com.crispytwig.naturalist.client.model.animal.duck;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.DuckBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.duck.Duck;
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

public class DuckBabyModel extends NaturalistEntityModel<Duck> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("duck_baby"), "main");
	private final ModelPart root;
	private final ModelPart neck;

	public DuckBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.75F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 6).addBox(-1.5F, -2.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.5F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -1.5F));
		PartDefinition bowtie = body.addOrReplaceChild("bowtie", CubeListBuilder.create()
		.texOffs(0, 19).addBox(-3.0F, -1.5F, -0.1F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -2.5F));
		PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(8, 11).mirror().addBox(-0.5F, 0.5F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -1.5F, 1.5F));
		PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(8, 11).addBox(-1.5F, 0.5F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.5F, 1.5F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected void setupAnimations(Duck entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.sitAnimationState, DuckBabyAnimation.DUCK_SIT_IDLE, ageInTicks, partialTick);

		this.animateIdleSmooth(entity.idleAnimationState, DuckBabyAnimation.DUCK_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.swimAnimationState, DuckBabyAnimation.DUCK_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.walkAnimationState, DuckBabyAnimation.DUCK_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.5F));
		this.animateSmooth(entity.runAnimationState, DuckBabyAnimation.DUCK_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
