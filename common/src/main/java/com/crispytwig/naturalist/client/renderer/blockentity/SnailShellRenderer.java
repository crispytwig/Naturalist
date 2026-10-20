package com.crispytwig.naturalist.client.renderer.blockentity;

import com.crispytwig.naturalist.world.level.block.SnailShellBlock;
import com.crispytwig.naturalist.world.level.block.entity.SnailShellBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class SnailShellRenderer implements BlockEntityRenderer<SnailShellBlockEntity> {
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS);
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Set<ResourceLocation> POT_SPRITES = Set.of(
            ResourceLocation.withDefaultNamespace("block/flower_pot"),
            ResourceLocation.withDefaultNamespace("block/dirt"),
            ResourceLocation.withDefaultNamespace("block/potted_azalea_bush_side"),
            ResourceLocation.withDefaultNamespace("block/potted_azalea_bush_top"),
            ResourceLocation.withDefaultNamespace("block/potted_flowering_azalea_bush_side"),
            ResourceLocation.withDefaultNamespace("block/potted_flowering_azalea_bush_top"));

    private final BlockRenderDispatcher dispatcher;
    private final RandomSource random = RandomSource.create();

    public SnailShellRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull SnailShellBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        if (!(state.getBlock() instanceof SnailShellBlock)) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(state.getValue(SnailShellBlock.ROTATION))));
        poseStack.translate(0.0D, -0.28125D, 0.15625D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(-0.5D, -0.21875D, -0.65625D);
        this.dispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(RENDER_TYPE), state, this.dispatcher.getBlockModel(state), 1.0F, 1.0F, 1.0F, packedLight, packedOverlay);
        poseStack.popPose();

        Block potted = SnailShellBlock.getPottedBlock(blockEntity.getFlower());
        if (potted != null) {
            BlockState pottedState = potted.defaultBlockState();
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.15D, 0.0D);
            renderPlant(this.dispatcher.getBlockModel(pottedState), pottedState, poseStack.last(), bufferSource.getBuffer(RENDER_TYPE), packedLight, packedOverlay);
            poseStack.popPose();
        }
    }

    private void renderPlant(BakedModel model, BlockState state, PoseStack.Pose pose, VertexConsumer consumer, int packedLight, int packedOverlay) {
        for (Direction direction : DIRECTIONS) {
            this.random.setSeed(42L);
            renderQuads(model.getQuads(state, direction, this.random), pose, consumer, packedLight, packedOverlay);
        }
        this.random.setSeed(42L);
        renderQuads(model.getQuads(state, null, this.random), pose, consumer, packedLight, packedOverlay);
    }

    private static void renderQuads(List<BakedQuad> quads, PoseStack.Pose pose, VertexConsumer consumer, int packedLight, int packedOverlay) {
        for (BakedQuad quad : quads) {
            if (!POT_SPRITES.contains(quad.getSprite().contents().name())) {
                consumer.putBulkData(pose, quad, 1.0F, 1.0F, 1.0F, 1.0F, packedLight, packedOverlay);
            }
        }
    }
}
