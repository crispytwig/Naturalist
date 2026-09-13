package com.crispytwig.naturalist.client.model.animal.fish;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.animation.definitions.CatfishAnimation;
import com.crispytwig.naturalist.world.entity.animal.fish.Catfish;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;

public class CatfishModel extends NaturalistEntityModel<Catfish> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			Naturalist.location("catfish"), "main");
	private static final String[] HAT_NAMES = {"jellie", "fih", "emily"};

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart open;
	private final ModelPart hat;

	public CatfishModel(ModelPart root) {
		super(root, RenderTypes::entityCutoutCull);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		ModelPart head = this.body.getChild("head");
		this.open = head.getChild("open");
		this.hat = head.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.5F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-3.0F, -4.1667F, -2.3889F, 6.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(0.0F, -6.1667F, -2.3889F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(36, 7).addBox(0.0F, 2.8333F, -2.3889F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.6667F, -3.6111F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(36, 0).addBox(-1.0F, -2.5F, 0.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.6667F, 9.6111F));
		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create()
		.texOffs(32, 16).addBox(0.0F, -5.5F, -3.0F, 0.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));
		PartDefinition rightFrontFin = body.addOrReplaceChild("rightFrontFin", CubeListBuilder.create()
		.texOffs(0, 3).mirror().addBox(-4.0F, 0.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -0.1667F, 1.6111F, 0.3655F, 0.7119F, 0.5299F));
		PartDefinition leftFrontFin = body.addOrReplaceChild("leftFrontFin", CubeListBuilder.create()
		.texOffs(0, 3).addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.1667F, 1.6111F, 0.3655F, -0.7119F, -0.5299F));
		PartDefinition leftBackFin = body.addOrReplaceChild("leftBackFin", CubeListBuilder.create()
		.texOffs(0, 9).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.8333F, 6.6111F, 0.3053F, -1.1579F, -0.3313F));
		PartDefinition rightBackFin = body.addOrReplaceChild("rightBackFin", CubeListBuilder.create()
		.texOffs(0, 9).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.8333F, 6.6111F, 0.3053F, 1.1579F, 0.3313F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -0.6667F, -0.8889F));
		PartDefinition normal = head.addOrReplaceChild("normal", CubeListBuilder.create()
		.texOffs(0, 19).addBox(-4.0F, -4.0F, -3.0F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-4.0F, -4.0F, -3.0F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.5F))
		.texOffs(37, 7).mirror().addBox(-4.3F, -1.5F, -3.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(37, 7).addBox(4.3F, -1.5F, -3.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 2.0F, -2.5F));
		PartDefinition open = head.addOrReplaceChild("open", CubeListBuilder.create()
		.texOffs(25, 34).addBox(-6.0F, -8.5F, -3.5F, 12.0F, 8.0F, 7.0F, new CubeDeformation(0.5F))
		.texOffs(25, 49).addBox(-6.0F, -8.5F, -3.5F, 12.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(37, 7).mirror().addBox(-6.3F, -6.0F, -3.5F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(37, 7).addBox(6.3F, -6.0F, -3.5F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 4.5F, -2.0F));
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -4.7F, -3.0F));
		PartDefinition hat_r1 = hat.addOrReplaceChild("hat_r1", CubeListBuilder.create()
		.texOffs(-7, 49).addBox(-4.0F, 1.25F, -3.5F, 8.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 57).addBox(-2.0F, -1.75F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected void setupAnimations(Catfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTick, float netHeadYaw, float headPitch) {
		this.hat.visible = entity.hasCustomName() && showsHat(entity.getName().getString());
		this.open.visible = entity.biteAnimationState.factor(partialTick) > SmoothAnimationState.ACTIVE_THRESHOLD;

		this.animateSmooth(entity.flopAnimationState, CatfishAnimation.CATFISH_FLOP, ageInTicks, partialTick);
		this.animateSmooth(entity.swimAnimationState, CatfishAnimation.CATFISH_SWIM, ageInTicks, partialTick, movementAnimationSpeed(entity, limbSwingAmount, 1.0F, SMALL_SWIMMER_LIMB_SWING));
		this.animateSmooth(entity.biteAnimationState, CatfishAnimation.CATFISH_BITE, ageInTicks, partialTick);

		this.body.xRot += entity.swimTilt.getSwimPitch(partialTick) * Mth.DEG_TO_RAD;
	}

	private static boolean showsHat(String name) {
		String lower = name.toLowerCase(Locale.ROOT);
		for (String hatName : HAT_NAMES) {
			if (lower.contains(hatName)) {
				return true;
			}
		}
		return false;
	}
}
