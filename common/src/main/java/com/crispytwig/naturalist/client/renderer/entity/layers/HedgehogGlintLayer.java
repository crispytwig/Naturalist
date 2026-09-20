package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class HedgehogGlintLayer extends RenderLayer<NaturalistRenderState<Hedgehog>, NaturalistEntityModel<Hedgehog>> {
    public HedgehogGlintLayer(RenderLayerParent<NaturalistRenderState<Hedgehog>, NaturalistEntityModel<Hedgehog>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Hedgehog> state, float yRot, float xRot) {
        if (state.isInvisible || !state.entity.hasThrowEnchantments()) {
            return;
        }
        submitNodeCollector.order(2).submitModel(this.getParentModel(), state, poseStack, RenderTypes.patternedShieldGlint(),
                lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
    }
}
