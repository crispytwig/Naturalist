package com.crispytwig.naturalist.client.model.animal.snake;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.SnakeAnimation;
import com.crispytwig.naturalist.world.entity.animal.snake.Snake;
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

public class SnakeModel extends NaturalistEntityModel<Snake> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("snake"), "main");
	private final ModelPart main;
    private final ModelPart skull;
	private final ModelPart awake;
	private final ModelPart asleep;
    private final ModelPart tail2;
    private final ModelPart tail4;

	public SnakeModel(ModelPart root) {
		super(root);
		this.main = root.getChild("main");
        ModelPart neck = this.main.getChild("neck");
		this.skull = neck.getChild("skull");
		this.awake = this.skull.getChild("awake");
		this.asleep = this.skull.getChild("asleep");
        ModelPart tail = this.main.getChild("tail");
		this.tail2 = tail.getChild("tail2");
        ModelPart tail3 = this.tail2.getChild("tail3");
		this.tail4 = tail3.getChild("tail4");
	}

	@Override
	protected String getRootPartName() {
		return "main";
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition neck = main.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 15).addBox(-1.5F, -5.75F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.25F, -3.5F));
		PartDefinition skull = neck.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(12, 9).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(31, 21).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 57).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(-2.5F, -1.0F, -5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.75F, 0.5F));
		PartDefinition jaw = skull.addOrReplaceChild("jaw", CubeListBuilder.create()
		.texOffs(21, 9).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(7, 55).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -1.0F, -1.0F));
		PartDefinition tongue = jaw.addOrReplaceChild("tongue", CubeListBuilder.create()
		.texOffs(21, 2).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.1F, 0.0F));
		PartDefinition awake = skull.addOrReplaceChild("awake", CubeListBuilder.create()
		.texOffs(21, 0).addBox(-2.0F, -10.0F, -8.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 3.0F));
		PartDefinition awakeWide = awake.addOrReplaceChild("awakeWide", CubeListBuilder.create()
		.texOffs(12, 46).addBox(-3.0F, -10.0F, -8.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition asleep = skull.addOrReplaceChild("asleep", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -10.0F, -8.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 3.0F));
		PartDefinition asleepWide = asleep.addOrReplaceChild("asleepWide", CubeListBuilder.create()
		.texOffs(37, 46).addBox(-3.0F, -10.0F, -8.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition tail = main.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(13, 16).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -2.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(32, 11).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 6.0F));
		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create()
		.texOffs(48, 7).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 6.0F));
		PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create()
		.texOffs(50, 17).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Snake entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		boolean sleeping = entity.isSleeping();
		this.awake.visible = !sleeping;
		this.asleep.visible = sleeping;
		this.tail4.visible = entity.isRattlesnake();
		if (!entity.getMainHandItem().isEmpty()) {
			this.tail2.xScale = 1.5F;
			this.tail2.yScale = 1.5F;
		}

		this.animateSmooth(entity.attackAnimationState, SnakeAnimation.SNAKE_ATTACK, ageInTicks, partialTick);
		this.animateSmooth(entity.tongueAnimationState, SnakeAnimation.SNAKE_TONGUE, ageInTicks, partialTick);
		this.animateSmooth(entity.rattleAnimationState, SnakeAnimation.SNAKE_RATTLE, ageInTicks, partialTick);

		this.animateSmooth(entity.sleepAnimationState, SnakeAnimation.SNAKE_SLEEP, ageInTicks, partialTick);
		this.animateSmooth(entity.climbAnimationState, SnakeAnimation.SNAKE_CLIMB, ageInTicks, partialTick);
		this.animateSmooth(entity.moveAnimationState, SnakeAnimation.SNAKE_MOVE, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 2.5F));

		if (!sleeping) {
			applyHeadLook(this.skull, netHeadYaw, headPitch);
		}
	}
}
