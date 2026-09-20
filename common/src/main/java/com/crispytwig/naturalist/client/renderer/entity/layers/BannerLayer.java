package com.crispytwig.naturalist.client.renderer.entity.layers;

import org.jspecify.annotations.Nullable;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class BannerLayer<T extends Elephant> extends RenderLayer<NaturalistRenderState<T>, NaturalistEntityModel<T>> {
    private final SpriteGetter sprites;
    private final Model.Simple bar;
    private final Model.Simple mirroredBar;
    private final FlagModel flag;
    private final FlagModel mirroredFlag;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float scale;
    private @Nullable NaturalistEntityModel<T> cachedModel;
    private @Nullable ModelPart cachedRoot;
    private @Nullable ModelPart cachedBody;

    public BannerLayer(RenderLayerParent<NaturalistRenderState<T>, NaturalistEntityModel<T>> parent, EntityRendererProvider.Context context, float offsetX, float offsetY, float offsetZ, float scale) {
        super(parent);
        this.sprites = context.getSprites();
        this.bar = new Model.Simple(bakeBar(false), RenderTypes::entitySolid);
        this.mirroredBar = new Model.Simple(bakeBar(true), RenderTypes::entitySolid);
        this.flag = new FlagModel(bakeFlag(false));
        this.mirroredFlag = new FlagModel(bakeFlag(true));
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.scale = scale;
    }

    private static ModelPart bakeFlag(boolean mirror) {
        MeshDefinition mesh = new MeshDefinition();
        CubeListBuilder cubes = CubeListBuilder.create().texOffs(0, 0);
        if (mirror) {
            cubes.mirror();
        }
        mesh.getRoot().addOrReplaceChild("flag", cubes.addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }

    private static ModelPart bakeBar(boolean mirror) {
        MeshDefinition mesh = new MeshDefinition();
        CubeListBuilder cubes = CubeListBuilder.create().texOffs(0, 42);
        if (mirror) {
            cubes.mirror();
        }
        mesh.getRoot().addOrReplaceChild("bar", cubes.addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 32.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<T> state, float yRot, float xRot) {
        T entity = state.entity;
        if (entity.isBaby() || state.isInvisible) {
            return;
        }
        ItemStack stack = entity.getBanner();
        if (!(stack.getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        DyeColor color = bannerItem.getColor();
        BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        float partialTick = state.partialTick;
        float phase = (Mth.positiveModulo(entity.tickCount + entity.getId() * 13, 100) + partialTick) / 100.0F;
        float sway = (-0.0125F + 0.01F * Mth.cos(Mth.TWO_PI * phase)) * Mth.PI;
        float tilt = Mth.lerp(partialTick, entity.bannerSwingO, entity.bannerSwing) * Mth.DEG_TO_RAD + entity.getRenderRoll();
        float lift = Mth.lerp(partialTick, entity.bannerLiftO, entity.bannerLift) * Mth.DEG_TO_RAD;
        NaturalistEntityModel<T> model = this.getParentModel();
        if (model != this.cachedModel) {
            this.cachedModel = model;
            this.cachedRoot = model.root().getChild("root");
            this.cachedBody = this.cachedRoot.getChild("body");
        }
        poseStack.pushPose();
        this.cachedRoot.translateAndRotate(poseStack);
        this.cachedBody.translateAndRotate(poseStack);
        for (int side = 1; side >= -1; side -= 2) {
            poseStack.pushPose();
            poseStack.translate(side * this.offsetX / 16.0F, this.offsetY / 16.0F, this.offsetZ / 16.0F);
            poseStack.rotateDegrees(Axis.YP, -90.0F * side);
            poseStack.scale(this.scale, this.scale, this.scale);
            FlagModel sideFlag = side > 0 ? this.flag : this.mirroredFlag;
            submitNodeCollector.submitModel(side > 0 ? this.bar : this.mirroredBar, Unit.INSTANCE, poseStack,
                    lightCoords, OverlayTexture.NO_OVERLAY, -1, Sheets.BANNER_BASE, this.sprites, state.outlineColor);
            FlagPose flagPose = new FlagPose(Math.min(sway - side * tilt, 0.0F), side * lift);
            submitNodeCollector.submitModel(sideFlag, flagPose, poseStack,
                    lightCoords, OverlayTexture.NO_OVERLAY, -1, Sheets.BANNER_BASE, this.sprites, state.outlineColor);
            BannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY,
                    sideFlag, flagPose, true, color, patterns);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private record FlagPose(float xRot, float zRot) {
    }

    private static final class FlagModel extends Model<FlagPose> {
        private final ModelPart flagPart;

        FlagModel(ModelPart root) {
            super(root, RenderTypes::entitySolid);
            this.flagPart = root.getChild("flag");
        }

        @Override
        public void setupAnim(FlagPose pose) {
            super.setupAnim(pose);
            this.flagPart.xRot = pose.xRot();
            this.flagPart.zRot = pose.zRot();
        }
    }
}
