package com.crispytwig.naturalist.client.model.animal.vulture;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.VultureBabyAnimation;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
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

public class VultureBabyModel extends NaturalistEntityModel<Vulture> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("vulture_baby"), "main");
	private final ModelPart root;
    private final ModelPart neck;

	public VultureBabyModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
		this.neck = body.getChild("neck");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 8).addBox(-2.5F, -4.0F, -2.75F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-2.5F, -4.0F, -2.75F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -3.0F, 0.75F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create()
		.texOffs(0, 0).addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -2.0F, -1.75F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create()
		.texOffs(0, 0).mirror().addBox(-5.0F, 0.0F, -1.0F, 5.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -2.0F, -1.75F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
		.texOffs(20, 14).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(20, 21).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.25F))
		.texOffs(20, 8).addBox(-1.5F, -5.0F, -2.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-0.5F, -5.0F, -4.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -2.25F));
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.5F));
		PartDefinition rightLegs = legs.addOrReplaceChild("rightLegs", CubeListBuilder.create()
		.texOffs(20, 28).mirror().addBox(-3.0F, 0.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.5F, 0.0F));
		PartDefinition leftLegs = legs.addOrReplaceChild("leftLegs", CubeListBuilder.create()
		.texOffs(20, 28).addBox(0.0F, 0.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Vulture entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {

		this.animateSmooth(entity.sitAnimationState, VultureBabyAnimation.VULTURE_SIT_IDLE, ageInTicks, partialTick);
		this.animateSmooth(entity.flyAnimationState, VultureBabyAnimation.VULTURE_IDLE, ageInTicks, partialTick);

		applyHeadLook(this.neck, netHeadYaw, headPitch);
	}
}
