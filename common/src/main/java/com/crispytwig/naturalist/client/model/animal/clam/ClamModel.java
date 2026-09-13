package com.crispytwig.naturalist.client.model.animal.clam;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.ClamAnimation;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
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

public class ClamModel extends NaturalistEntityModel<Clam> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("clam"), "main");
	private final ModelPart root;
	private final ModelPart bottom;
	private final ModelPart rightArm;
	private final ModelPart rightItem;

	public ClamModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
		this.bottom = this.root.getChild("bottom");
		this.rightArm = this.bottom.getChild("rightArm");
		this.rightItem = this.rightArm.getChild("rightItem");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 10.0F));
		PartDefinition top = root.addOrReplaceChild("top", CubeListBuilder.create()
		.texOffs(0, 26).addBox(-12.5F, -5.0F, -20.0F, 25.0F, 5.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(8, 53).addBox(-9.5F, -5.0F, -25.0F, 19.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bottom = root.addOrReplaceChild("bottom", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-12.5F, 0.0F, -20.0F, 25.0F, 3.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(72, 10).addBox(-9.5F, 0.0F, -25.0F, 19.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(76, 29).addBox(-6.5F, -2.0F, -15.0F, 13.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = bottom.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -10.0F));
		PartDefinition hinge = root.addOrReplaceChild("hinge", CubeListBuilder.create()
		.texOffs(59, 53).addBox(-6.5F, -2.0F, -0.5F, 13.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected void setupAnimations(Clam entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.openAnimationState, ClamAnimation.CLAM_OPEN, ageInTicks, partialTick);
		this.animateSmooth(entity.closeAnimationState, ClamAnimation.CLAM_CLOSE, ageInTicks, partialTick);

		this.animateSmooth(entity.idleOpenAnimationState, ClamAnimation.CLAM_IDLE_OPEN, ageInTicks, partialTick);
		this.animateSmooth(entity.idleClosedAnimationState, ClamAnimation.CLAM_IDLE_CLOSED, ageInTicks, partialTick);
	}

	public void translateToItem(PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.bottom.translateAndRotate(poseStack);
		this.rightArm.translateAndRotate(poseStack);
		this.rightItem.translateAndRotate(poseStack);
	}
}
