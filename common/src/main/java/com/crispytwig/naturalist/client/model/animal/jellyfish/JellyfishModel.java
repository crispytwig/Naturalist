package com.crispytwig.naturalist.client.model.animal.jellyfish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.JellyfishAnimation;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;
import net.minecraft.client.renderer.rendertype.RenderTypes;
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

public class JellyfishModel extends NaturalistEntityModel<Jellyfish> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("jellyfish"), "main");
	private final ModelPart root;
	private final ModelPart body;

	public JellyfishModel(ModelPart root) {
		super(root, RenderTypes::entityTranslucent);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition inflation = body.addOrReplaceChild("inflation", CubeListBuilder.create()
		.texOffs(0, 20).addBox(-6.0F, -15.5F, -6.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));
		PartDefinition tentacles = body.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 2.5F));
		PartDefinition frontTentacle = tentacles.addOrReplaceChild("frontTentacle", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));
		PartDefinition leftTentacle = tentacles.addOrReplaceChild("leftTentacle", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, -2.5F));
		PartDefinition leftTentacle_r1 = leftTentacle.addOrReplaceChild("leftTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition leftMidFrontTentacle = tentacles.addOrReplaceChild("leftMidFrontTentacle", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -4.5F));
		PartDefinition leftMidFrontTentacle_r1 = leftMidFrontTentacle.addOrReplaceChild("leftMidFrontTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition leftMidBackTentacle = tentacles.addOrReplaceChild("leftMidBackTentacle", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -0.5F));
		PartDefinition leftMidBackTentacle_r1 = leftMidBackTentacle.addOrReplaceChild("leftMidBackTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition rightMidFrontTentacle = tentacles.addOrReplaceChild("rightMidFrontTentacle", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -4.5F));
		PartDefinition rightMidFrontTentacle_r1 = rightMidFrontTentacle.addOrReplaceChild("rightMidFrontTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition rightMidBackTentacle = tentacles.addOrReplaceChild("rightMidBackTentacle", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -0.5F));
		PartDefinition rightMidBackTentacle_r1 = rightMidBackTentacle.addOrReplaceChild("rightMidBackTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition backTentacle = tentacles.addOrReplaceChild("backTentacle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition backTentacle_r1 = backTentacle.addOrReplaceChild("backTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition rightTentacle = tentacles.addOrReplaceChild("rightTentacle", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, -2.5F));
		PartDefinition rightTentacle_r1 = rightTentacle.addOrReplaceChild("rightTentacle_r1", CubeListBuilder.create()
		.texOffs(3, 2).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 4.0F));
		PartDefinition bone4 = skirt.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bone4_r1 = bone4.addOrReplaceChild("bone4_r1", CubeListBuilder.create()
		.texOffs(37, 20).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition bone3 = skirt.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, -4.0F));
		PartDefinition bone3_r1 = bone3.addOrReplaceChild("bone3_r1", CubeListBuilder.create()
		.texOffs(37, 10).addBox(0.0F, 0.0F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
		PartDefinition bone2 = skirt.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -8.0F));
		PartDefinition bone2_r1 = bone2.addOrReplaceChild("bone2_r1", CubeListBuilder.create()
		.texOffs(37, 20).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition bone = skirt.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, -4.0F));
		PartDefinition bone_r1 = bone.addOrReplaceChild("bone_r1", CubeListBuilder.create()
		.texOffs(37, 10).addBox(0.0F, 0.0F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Jellyfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.landAnimationState, JellyfishAnimation.JELLYFISH_LAND, ageInTicks, partialTick);
		this.animateSmooth(entity.idleAnimationState, JellyfishAnimation.JELLYFISH_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, JellyfishAnimation.JELLYFISH_SWIM, ageInTicks, partialTick);

		this.body.xRot -= entity.getXBodyRot(partialTick) * Mth.DEG_TO_RAD;
	}
}
