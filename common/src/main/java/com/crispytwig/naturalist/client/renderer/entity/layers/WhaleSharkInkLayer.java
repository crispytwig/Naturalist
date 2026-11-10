package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.common.entity.whaleshark.WhaleShark;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class WhaleSharkInkLayer<M extends EntityModel<? super NaturalistRenderState<WhaleShark>>> extends RenderLayer<NaturalistRenderState<WhaleShark>, M> {
    private static final Identifier TEXTURE = Naturalist.location("textures/entity/whale_shark/whale_shark_ink.png");

    public WhaleSharkInkLayer(RenderLayerParent<NaturalistRenderState<WhaleShark>, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<WhaleShark> state, float yRot, float xRot) {
        if (state.isInvisible || state.isBaby || !state.entity.isMouthInked()) {
            return;
        }
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(TEXTURE),
                lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);
    }
}
