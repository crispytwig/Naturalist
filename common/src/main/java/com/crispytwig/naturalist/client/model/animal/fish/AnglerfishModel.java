package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.AnglerfishAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
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

public class AnglerfishModel extends NaturalistEntityModel<Anglerfish> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("anglerfish"), "main");
	private final ModelPart modelRoot;
    private final ModelPart body;

	public AnglerfishModel(ModelPart root) {
		super(root);
		this.modelRoot = root;
		this.body = root.getChild("root").getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.0F, -6.0F, -5.0F, 8.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(44, 48).addBox(-3.5F, -6.0F, -7.0F, 7.0F, 12.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -6.0F, -1.0F));
		PartDefinition dangly = body.addOrReplaceChild("dangly", CubeListBuilder.create()
		.texOffs(0, 30).addBox(0.0F, -9.0F, -8.5F, 0.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -3.5F, 0.3927F, 0.0F, 0.0F));
		PartDefinition bulb = dangly.addOrReplaceChild("bulb", CubeListBuilder.create()
		.texOffs(30, 49).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.231F, -7.4043F, -0.3927F, 0.0F, 0.0F));
		PartDefinition jaw = body.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(0, 23).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(45, 24).addBox(-3.5F, -12.0F, 0.0F, 7.0F, 12.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 6.0F, -5.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(27, 0).addBox(0.0F, -3.5F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.5F, 1.0F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(27, 0).mirror().addBox(-7.0F, -3.5F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.5F, 1.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(32, 35).addBox(-2.0F, -4.0F, 0.125F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(37, 2).addBox(0.0F, -7.0F, 0.125F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(31, 17).addBox(0.0F, 4.0F, 0.125F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.875F));
		PartDefinition tailFin = tail.addOrReplaceChild("tailFin", CubeListBuilder.create()
		.texOffs(21, 23).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.125F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Anglerfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.attackAnimationState, AnglerfishAnimation.ANGLERFISH_ATTACK, ageInTicks, partialTick);

		this.animateSmooth(entity.flopAnimationState, AnglerfishAnimation.ANGLERFISH_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, AnglerfishAnimation.ANGLERFISH_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, SMALL_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.swimFastAnimationState, AnglerfishAnimation.ANGLERFISH_SWIM_FAST, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, LARGE_SWIMMER_LIMB_SWING));

		this.body.xRot += entity.swimTilt.getSwimPitch(partialTick) * Mth.DEG_TO_RAD;
	}
}
