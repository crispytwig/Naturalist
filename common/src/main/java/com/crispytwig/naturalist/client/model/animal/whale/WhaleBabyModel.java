package com.crispytwig.naturalist.client.model.animal.whale;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.WhaleBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
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

public class WhaleBabyModel extends NaturalistEntityModel<Whale> {
	private static final float FIN_COUNTER_GAIN = 1.6F;
	private static final float HEAD_ROLL_STABILIZE = 0.35F;
	private static final float FLUKE_ROLL_FOLLOW = 0.4F;
	private static final float HEAD_DROOP_ANGLE = (float) Math.toRadians(-45.0D);
	private static final float TAIL_DROOP_ANGLE = (float) Math.toRadians(90.0D);

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("whale_baby"), "main");
	private final ModelPart root;
    private final ModelPart leftFin;
	private final ModelPart rightFin;
	private final ModelPart skull;
	private final ModelPart tail;
	private final ModelPart flip;

	public WhaleBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.leftFin = body.getChild("leftFin");
		this.rightFin = body.getChild("rightFin");
		this.skull = body.getChild("skull");
		this.tail = body.getChild("tail");
		this.flip = this.tail.getChild("flip");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-8.0F, -10.0F, -14.0F, 16.0F, 15.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));
		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(0, 79).addBox(1.0F, -1.0F, -1.0F, 17.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, -9.0F));
		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(0, 79).mirror().addBox(-18.0F, -1.0F, -1.0F, 17.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 0.0F, -9.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(58, 50).addBox(-6.0F, -4.0F, -19.0F, 12.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-6.0F, 2.0F, -7.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.026F))
		.texOffs(0, 89).addBox(-5.0F, 0.0F, -18.0F, 10.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -13.0F));
		PartDefinition jaw = skull.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(58, 74).addBox(-6.0F, 0.0F, -12.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -7.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 50).addBox(-5.0F, -5.0F, 1.0F, 10.0F, 10.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 8.0F));
		PartDefinition flip = tail.addOrReplaceChild("flip", CubeListBuilder.create()
		.texOffs(0, 39).addBox(-16.0F, -1.0F, -2.0F, 32.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 19.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Whale entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.flopAnimationState, WhaleBabyAnimation.WHALE_BABY_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.idleAnimationState, WhaleBabyAnimation.WHALE_BABY_SWIM_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, WhaleBabyAnimation.WHALE_BABY_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.0F, LARGE_SWIMMER_LIMB_SWING));

		float bodyPitch = entity.getXBodyRot(partialTick);
		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;
		float finCounter = (bodyPitch - entity.getFinLag(partialTick)) * Mth.DEG_TO_RAD * FIN_COUNTER_GAIN;
		float droopF = entity.getFrontDroop(partialTick) * HEAD_DROOP_ANGLE;
		float droopB = entity.getBackDroop(partialTick) * TAIL_DROOP_ANGLE;

		this.rotatePart(this.root, bodyPitch * Mth.DEG_TO_RAD, 0.0F, roll);
		this.bend(this.skull, entity, 0, partialTick);
		this.bend(this.tail, entity, 1, partialTick);
		this.bend(this.flip, entity, 2, partialTick);
		this.rotatePart(this.leftFin, -finCounter, 0.0F, 0.0F);
		this.rotatePart(this.rightFin, -finCounter, 0.0F, 0.0F);
		this.rotatePart(this.skull, droopF, 0.0F, -roll * HEAD_ROLL_STABILIZE);
		this.rotatePart(this.tail, droopB, 0.0F, 0.0F);
		this.rotatePart(this.flip, droopB, 0.0F, roll * FLUKE_ROLL_FOLLOW);
	}
}
