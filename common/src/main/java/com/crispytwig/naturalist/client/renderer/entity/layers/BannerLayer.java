package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BannerLayer extends RenderLayer<Elephant, HierarchicalModel<Elephant>> {
    private final ModelPart flag;
    private final ModelPart bar;
    private final ModelPart mirroredFlag;
    private final ModelPart mirroredBar;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float scale;

    public BannerLayer(RenderLayerParent<Elephant, HierarchicalModel<Elephant>> parent, EntityRendererProvider.Context context, float offsetX, float offsetY, float offsetZ, float scale) {
        super(parent);
        ModelPart bannerRoot = context.bakeLayer(ModelLayers.BANNER);
        ModelPart mirroredRoot = bakeMirroredBanner();
        this.flag = bannerRoot.getChild("flag");
        this.bar = bannerRoot.getChild("bar");
        this.mirroredFlag = mirroredRoot.getChild("flag");
        this.mirroredBar = mirroredRoot.getChild("bar");
        this.bar.y = 32.0F;
        this.mirroredBar.y = 32.0F;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.scale = scale;
    }

    private static ModelPart bakeMirroredBanner() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(0, 0).mirror()
                .addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), PartPose.ZERO);
        root.addOrReplaceChild("bar", CubeListBuilder.create().texOffs(0, 42).mirror()
                .addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Elephant entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isBaby() || entity.isInvisible()) {
            return;
        }
        ItemStack stack = entity.getBanner();
        if (!(stack.getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        DyeColor color = bannerItem.getColor();
        BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        VertexConsumer barConsumer = ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid);
        float phase = (Mth.positiveModulo(entity.tickCount + entity.getId() * 13, 100) + partialTick) / 100.0F;
        float sway = (-0.0125F + 0.01F * Mth.cos(Mth.TWO_PI * phase)) * Mth.PI;
        float tilt = Mth.lerp(partialTick, entity.bannerSwingO, entity.bannerSwing) * Mth.DEG_TO_RAD + entity.getRenderRoll();
        float lift = Mth.lerp(partialTick, entity.bannerLiftO, entity.bannerLift) * Mth.DEG_TO_RAD;

        ModelPart root = this.getParentModel().root();
        poseStack.pushPose();
        root.translateAndRotate(poseStack);
        root.getChild("body").translateAndRotate(poseStack);
        for (int side = 1; side >= -1; side -= 2) {
            poseStack.pushPose();
            poseStack.translate(side * this.offsetX / 16.0F, this.offsetY / 16.0F, this.offsetZ / 16.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F * side));
            poseStack.scale(this.scale, this.scale, this.scale);
            ModelPart sideFlag = side > 0 ? this.flag : this.mirroredFlag;
            (side > 0 ? this.bar : this.mirroredBar).render(poseStack, barConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            sideFlag.xRot = Math.min(sway - side * tilt, 0.0F);
            sideFlag.zRot = side * lift;
            BannerRenderer.renderPatterns(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, sideFlag, ModelBakery.BANNER_BASE, true, color, patterns);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
