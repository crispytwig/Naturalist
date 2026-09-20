package com.crispytwig.naturalist.client.renderer.blockentity;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;

import java.util.Map;
import java.util.HashMap;
import com.crispytwig.naturalist.world.level.block.SnailShellBlock;
import com.crispytwig.naturalist.world.level.block.entity.SnailShellBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SnailShellRenderer implements BlockEntityRenderer<SnailShellBlockEntity, SnailShellRenderer.SnailShellRenderState> {
    private static final BlockDisplayContext DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final Set<Identifier> POT_SPRITES = Set.of(
            Identifier.withDefaultNamespace("block/flower_pot"),
            Identifier.withDefaultNamespace("block/dirt"),
            Identifier.withDefaultNamespace("block/potted_azalea_bush_side"),
            Identifier.withDefaultNamespace("block/potted_azalea_bush_top"),
            Identifier.withDefaultNamespace("block/potted_flowering_azalea_bush_side"),
            Identifier.withDefaultNamespace("block/potted_flowering_azalea_bush_top"));

    private final BlockModelResolver blockModelResolver;
    private final RandomSource random = RandomSource.create();
    private final Map<BlockState, CachedPlantParts> plantPartsCache = new HashMap<>();

    public SnailShellRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public SnailShellRenderState createRenderState() {
        return new SnailShellRenderState();
    }

    @Override
    public void extractRenderState(SnailShellBlockEntity blockEntity, SnailShellRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.valid = blockState.getBlock() instanceof SnailShellBlock;
        state.plantParts.clear();
        if (!state.valid) {
            state.shell.clear();
            return;
        }
        state.rotationDegrees = RotationSegment.convertToDegrees(blockState.getValue(SnailShellBlock.ROTATION));
        this.blockModelResolver.update(state.shell, blockState, DISPLAY_CONTEXT);

        Block potted = SnailShellBlock.getPottedBlock(blockEntity.getFlower());
        if (potted != null) {
            state.plantParts.addAll(this.plantParts(potted.defaultBlockState()));
        }
    }

    @Override
    public void submit(SnailShellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.valid) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.rotateDegrees(Axis.YP, -state.rotationDegrees);
        poseStack.translate(0.0D, -0.28125D, 0.15625D);
        poseStack.rotateDegrees(Axis.XP, 90.0F);
        poseStack.translate(-0.5D, -0.21875D, -0.65625D);
        state.shell.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        if (!state.plantParts.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.15D, 0.0D);
            submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), new ArrayList<>(state.plantParts),
                    BlockModelRenderState.EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    private List<BlockStateModelPart> plantParts(BlockState pottedState) {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(pottedState);
        CachedPlantParts cached = this.plantPartsCache.get(pottedState);
        if (cached == null || cached.model() != model) {
            List<BlockStateModelPart> parts = new ArrayList<>();
            this.random.setSeed(42L);
            model.collectParts(this.random, parts);
            List<BlockStateModelPart> plantParts = new ArrayList<>(parts.size());
            for (BlockStateModelPart part : parts) {
                plantParts.add(PlantOnlyPart.of(part));
            }
            cached = new CachedPlantParts(model, List.copyOf(plantParts));
            this.plantPartsCache.put(pottedState, cached);
        }
        return cached.parts();
    }

    private record CachedPlantParts(BlockStateModel model, List<BlockStateModelPart> parts) {
    }

    public static class SnailShellRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState shell = new BlockModelRenderState();
        public final List<BlockStateModelPart> plantParts = new ArrayList<>();
        public float rotationDegrees;
        public boolean valid;
    }

    private record PlantOnlyPart(BlockStateModelPart parent, List<List<BakedQuad>> quadsByDirection) implements BlockStateModelPart {
        private static final Direction[] DIRECTIONS = Direction.values();

        static PlantOnlyPart of(BlockStateModelPart parent) {
            List<List<BakedQuad>> quadsByDirection = new ArrayList<>(DIRECTIONS.length + 1);
            for (Direction direction : DIRECTIONS) {
                quadsByDirection.add(filter(parent.getQuads(direction)));
            }
            quadsByDirection.add(filter(parent.getQuads(null)));
            return new PlantOnlyPart(parent, List.copyOf(quadsByDirection));
        }

        private static List<BakedQuad> filter(List<BakedQuad> quads) {
            List<BakedQuad> filtered = new ArrayList<>(quads.size());
            for (BakedQuad quad : quads) {
                if (!POT_SPRITES.contains(quad.materialInfo().sprite().contents().name())) {
                    filtered.add(quad);
                }
            }
            return List.copyOf(filtered);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable Direction direction) {
            return this.quadsByDirection.get(direction == null ? DIRECTIONS.length : direction.ordinal());
        }

        @Override
        public boolean useAmbientOcclusion() {
            return this.parent.useAmbientOcclusion();
        }

        @Override
        public Material.Baked particleMaterial() {
            return this.parent.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return this.parent.materialFlags();
        }
    }
}
