package com.crispytwig.naturalist.client.model.animal.boar;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.BoarBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.boar.Boar;
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

public class BoarBabyModel extends NaturalistEntityModel<Boar> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("boar_baby"), "main");
	private final ModelPart root;
	private final ModelPart neck;

	public BoarBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.neck = this.root.getChild("body").getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 1.5F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.5F, -3.0F, -4.5F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(15, 22).addBox(0.0F, -9.0F, -1.5F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 28).addBox(0.0F, -7.2008F, -1.8663F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-3.5F, -3.2008F, -3.8663F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.02F))
		.texOffs(3, 29).addBox(-2.5F, -0.2008F, -4.8663F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.7992F, -3.6337F));
		PartDefinition rightEar = neck.addOrReplaceChild("rightEar", CubeListBuilder.create()
		.texOffs(0, 3).mirror().addBox(-3.5F, -3.9979F, -0.5843F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -1.2008F, 0.1337F));
		PartDefinition leftEar = neck.addOrReplaceChild("leftEar", CubeListBuilder.create()
		.texOffs(0, 3).addBox(0.5F, -3.9979F, -0.5843F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.2008F, 0.1337F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));
		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create()
		.texOffs(20, 15).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 3.5F));
		PartDefinition leftArm = legs.addOrReplaceChild("leftArm", CubeListBuilder.create()
		.texOffs(20, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, -3.5F));
		PartDefinition rightArm = legs.addOrReplaceChild("rightArm", CubeListBuilder.create()
		.texOffs(20, 15).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 0.0F, -3.5F));
		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create()
		.texOffs(20, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 3.5F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	protected void setupAnimations(Boar entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateIdleSmooth(entity.idleAnimationState, BoarBabyAnimation.BOAR_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, BoarBabyAnimation.BOAR_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 3.5F));
		this.animateSmooth(entity.runAnimationState, BoarBabyAnimation.BOAR_RUN, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F));

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
