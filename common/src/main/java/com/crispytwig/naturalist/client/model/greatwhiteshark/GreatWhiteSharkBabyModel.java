package com.crispytwig.naturalist.client.model.greatwhiteshark;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.common.entity.greatwhiteshark.GreatWhiteShark;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class GreatWhiteSharkBabyModel extends NaturalistEntityModel<GreatWhiteShark> {
	private static final float HEAD_ROLL_STABILIZE = 0.35F;
	private static final float TAIL_ROLL_FOLLOW = 0.4F;
	private static final float SWIM_SPEED = 0.45F;
	private static final float FLOP_SPEED = 1.1F;
	private static final float SWIM_SWAY = 0.12F;
	private static final float SWIM_SWAY_MOVING = 0.3F;
	private static final float FLOP_SWAY = 0.6F;
	private static final float FIN_FLAP = 0.12F;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("great_white_shark_baby"), "main");
	private final ModelPart root;
	private final ModelPart skull;
	private final ModelPart tail;
	private final ModelPart leftFin;
	private final ModelPart rightFin;

	public GreatWhiteSharkBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		ModelPart body = this.root.getChild("main").getChild("body");
		this.skull = body.getChild("skull");
		this.tail = body.getChild("tail");
		this.leftFin = body.getChild("leftFin");
		this.rightFin = body.getChild("rightFin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));
		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		body.addOrReplaceChild("dorsalFin", CubeListBuilder.create()
		.texOffs(44, 0).addBox(0.0F, -11.0F, -3.0F, 0.0F, 11.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.75F, 0.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition skull = body.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(30, 46).addBox(-4.0F, -0.0833F, -4.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(19, 26).addBox(-4.0F, 3.9167F, -4.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(57, 46).addBox(-2.0F, -0.0833F, -6.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 55).addBox(-4.0F, 3.9167F, -4.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.9167F, -8.0F));
		skull.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(0, 52).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(19, 22).addBox(-3.5F, 1.0F, -4.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.9167F, 0.0F));
		body.addOrReplaceChild("leftFin", CubeListBuilder.create()
		.texOffs(30, 22).addBox(-1.0F, 0.0F, -1.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 2.0F, -4.0F));
		body.addOrReplaceChild("rightFin", CubeListBuilder.create()
		.texOffs(30, 22).mirror().addBox(-9.0F, 0.0F, -1.0F, 10.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 2.0F, -4.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(59, 20).addBox(-2.0F, -3.4714F, -1.6381F, 4.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.4714F, 5.6381F));
		tail.addOrReplaceChild("caudalFin", CubeListBuilder.create()
		.texOffs(5, 28).addBox(0.0F, -11.0F, -3.0F, 0.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4714F, 8.3619F, -0.7854F, 0.0F, 0.0F));
		tail.addOrReplaceChild("analFin", CubeListBuilder.create()
		.texOffs(25, 23).addBox(0.0F, 3.0F, -6.0F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4714F, 7.3619F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(GreatWhiteShark entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		boolean inWater = entity.isInWater();
		float cycle = ageInTicks * (inWater ? SWIM_SPEED : FLOP_SPEED);
		float sway = Mth.sin(cycle) * (inWater ? SWIM_SWAY + SWIM_SWAY_MOVING * Math.min(limbSwingAmount, 1.0F) : FLOP_SWAY);
		float flap = Mth.sin(cycle * 0.5F) * FIN_FLAP;
		float roll = entity.getZBodyRot(partialTick) * Mth.DEG_TO_RAD;

		this.rotatePart(this.root, entity.getXBodyRot(partialTick) * Mth.DEG_TO_RAD, 0.0F, roll);
		this.bend(this.skull, entity, 0, partialTick);
		this.bend(this.tail, entity, 1, partialTick);
		this.rotatePart(this.skull, 0.0F, sway * -0.3F, -roll * HEAD_ROLL_STABILIZE);
		this.rotatePart(this.tail, 0.0F, sway, roll * TAIL_ROLL_FOLLOW);
		this.rotatePart(this.leftFin, 0.0F, 0.0F, flap);
		this.rotatePart(this.rightFin, 0.0F, 0.0F, -flap);
	}
}
