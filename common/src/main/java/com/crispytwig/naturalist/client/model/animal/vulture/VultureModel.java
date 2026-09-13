package com.crispytwig.naturalist.client.model.animal.vulture;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.VultureAnimation;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class VultureModel extends NaturalistEntityModel<Vulture> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("vulture"), "main");
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart held_item;

	public VultureModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.held_item = this.neck.getChild("held_item");
	}

	@Override
	protected String getRootPartName() {
		return "body";
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(21, 14).mirror().addBox(-3.5F, -2.0F, -4.5F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0.0F, 19.0F, -3.5F));
		PartDefinition rightFoot = body.addOrReplaceChild("rightFoot", CubeListBuilder.create()
		.texOffs(46, 15).addBox(-1.5F, 0.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, 2.25F, 3.0F));
		PartDefinition leftFoot = body.addOrReplaceChild("leftFoot", CubeListBuilder.create()
		.texOffs(46, 15).mirror().addBox(-1.5F, 0.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(2.0F, 2.25F, 3.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 23).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -2.0F, 4.5F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(0, 35).addBox(-2.0F, -1.25F, -4.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(27, 27).addBox(-2.5F, -4.25F, -8.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(17, 34).addBox(-1.0F, -3.25F, -11.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.25F, -4.5F));
		PartDefinition held_item = neck.addOrReplaceChild("held_item", CubeListBuilder.create()
		.texOffs(20, 37).addBox(0.5F, -3.0F, -19.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 2.75F, 6.0F));
		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create()
		.texOffs(0, 11).addBox(-6.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, -4.5F));
		PartDefinition right_wing_tip = right_wing.addOrReplaceChild("right_wing_tip", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-13.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.05F));
		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create()
		.texOffs(0, 11).mirror().addBox(0.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.5F, 0.0F, -4.5F));
		PartDefinition left_wing_tip = left_wing.addOrReplaceChild("left_wing_tip", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 0.0F, 0.1F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Vulture entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.sitAnimationState, VultureAnimation.VULTURE_SIT, ageInTicks, partialTick);
		this.animateSmooth(entity.flyAnimationState, VultureAnimation.VULTURE_FLY, ageInTicks, partialTick);

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}

	public void translateToHeldItem(PoseStack poseStack) {
		this.body.translateAndRotate(poseStack);
		this.neck.translateAndRotate(poseStack);
		this.held_item.translateAndRotate(poseStack);
	}
}
