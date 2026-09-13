package com.crispytwig.naturalist.client.model.animal.komododragon;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.KomodoDragonAnimation;
import com.crispytwig.naturalist.world.entity.animal.komododragon.KomodoDragon;
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

public class KomodoDragonModel extends NaturalistEntityModel<KomodoDragon> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("komodo_dragon"), "main");
	private final ModelPart root;
    private final ModelPart skull;

	public KomodoDragonModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
        ModelPart neck = body.getChild("neck");
		this.skull = neck.getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-5.0F, -4.0F, -11.0F, 10.0F, 8.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 6.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(71, 71).addBox(-5.0F, -3.0F, -4.0F, 10.0F, 6.0F, 6.0F, new CubeDeformation(-0.02F)), PartPose.offset(0.0F, -1.0F, -11.0F));
		PartDefinition skull = neck.addOrReplaceChild("skull", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -2.5F, -5.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(-3.0F, -2.5F, -2.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(58, 0).addBox(-3.0F, 0.5F, -2.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(66, 0).addBox(-3.0F, -2.5F, -13.0F, 6.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(54, 30).addBox(-3.0F, 0.5F, -12.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -4.0F));
		PartDefinition lower_jaw = skull.addOrReplaceChild("lower_jaw", CubeListBuilder.create()
		.texOffs(86, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.02F))
		.texOffs(44, 82).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(-0.02F))
		.texOffs(96, 0).addBox(-3.0F, 0.0F, -11.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(58, 43).addBox(-3.0F, -1.0F, -10.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(-0.02F)), PartPose.offset(0.0F, 0.5F, -2.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 30).addBox(-3.0F, -2.0169F, -0.0949F, 6.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 11.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(65, 12).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.9831F, 11.9051F, 0.2618F, 0.0F, 0.0F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(5.0F, -5.25F, -2.0F));
		PartDefinition left_arm = legs.addOrReplaceChild("left_arm", CubeListBuilder.create()
		.texOffs(42, 57).addBox(-1.0F, -1.75F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(59, 59).addBox(-1.0F, 3.25F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition right_arm = legs.addOrReplaceChild("right_arm", CubeListBuilder.create()
		.texOffs(42, 57).mirror().addBox(-3.0F, -1.75F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(59, 59).mirror().addBox(-3.0F, 3.25F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, 0.0F, 0.0F));
		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create()
		.texOffs(59, 59).addBox(-2.0F, 3.75F, -4.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 51).addBox(-2.0F, -3.25F, -2.5F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 16.5F));
		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create()
		.texOffs(59, 59).mirror().addBox(-2.0F, 3.75F, -4.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 51).mirror().addBox(-2.0F, -3.25F, -2.5F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, -0.5F, 16.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(KomodoDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.biteAnimationState, KomodoDragonAnimation.KOMODO_DRAGON_BITE, ageInTicks, partialTick);
		this.animateSmooth(entity.babyTransformAnimationState, KomodoDragonAnimation.KOMODO_DRAGON_BABY_TRANSFORM, ageInTicks, partialTick);

		this.animateSmooth(entity.baskAnimationState, KomodoDragonAnimation.KOMODO_DRAGON_BASK, ageInTicks, partialTick);
		this.animateIdleSmooth(entity.idleAnimationState, KomodoDragonAnimation.KOMODO_DRAGON_IDLE, ageInTicks, partialTick, limbSwingAmount);
		this.animateSmooth(entity.walkAnimationState, KomodoDragonAnimation.KOMODO_DRAGON_WALK, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, entity.isSprinting() ? 2.5F : 1.5F));

		applyHeadLook(this.skull, netHeadYaw, headPitch);
	}
}
