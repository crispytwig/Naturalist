package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.hippo.HippoBabyModel;
import com.crispytwig.naturalist.client.model.animal.hippo.HippoModel;
import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class HippoRenderer extends NaturalistMobRenderer<Hippo> {
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;

    public HippoRenderer(EntityRendererProvider.Context context) {
        super(context, new HippoModel(context.bakeLayer(HippoModel.LAYER_LOCATION)), new HippoBabyModel(context.bakeLayer(HippoBabyModel.LAYER_LOCATION)), 1.1F);
        this.blockModelResolver = context.getBlockModelResolver();
        this.addLayer(new HippoJawBlockLayer(this));
    }

    @Override
    public NaturalistRenderState<Hippo> createRenderState() {
        return new HippoRenderState();
    }

    @Override
    public void extractRenderState(Hippo entity, NaturalistRenderState<Hippo> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        HippoRenderState hippoState = (HippoRenderState) state;
        if (entity.getMainHandItem().getItem() instanceof BlockItem blockItem) {
            this.blockModelResolver.update(hippoState.jawBlock, blockItem.getBlock().defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
        } else {
            hippoState.jawBlock.clear();
        }
    }

    public static class HippoRenderState extends NaturalistRenderState<Hippo> {
        public final BlockModelRenderState jawBlock = new BlockModelRenderState();
    }

    private static class HippoJawBlockLayer extends RenderLayer<NaturalistRenderState<Hippo>, NaturalistEntityModel<Hippo>> {
        HippoJawBlockLayer(RenderLayerParent<NaturalistRenderState<Hippo>, NaturalistEntityModel<Hippo>> parent) {
            super(parent);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Hippo> state, float yRot, float xRot) {
            HippoRenderState hippoState = (HippoRenderState) state;
            if (hippoState.jawBlock.isEmpty()) {
                return;
            }
            if (!(this.getParentModel() instanceof HippoModel hippoModel)) {
                return;
            }
            poseStack.pushPose();
            hippoModel.translateToBotJaw(poseStack);
            poseStack.translate(-0.4D, 0.76D, -1.8D);
            poseStack.scale(0.675F, 0.675F, 0.675F);
            hippoState.jawBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
