package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class TortoiseMaskLayer<M extends EntityModel<? super NaturalistRenderState<Tortoise>>> extends RenderLayer<NaturalistRenderState<Tortoise>, M> {
    private static final Identifier DONATELLO = Naturalist.location("textures/entity/tortoise/donatello.png");
    private static final Identifier LEONARDO = Naturalist.location("textures/entity/tortoise/leonardo.png");
    private static final Identifier MICHELANGELO = Naturalist.location("textures/entity/tortoise/michelangelo.png");
    private static final Identifier RAPHAEL = Naturalist.location("textures/entity/tortoise/raphael.png");

    public TortoiseMaskLayer(RenderLayerParent<NaturalistRenderState<Tortoise>, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<Tortoise> state, float yRot, float xRot) {
        Tortoise entity = state.entity;
        if (state.isInvisible || !entity.hasCustomName()) {
            return;
        }
        Identifier skin = switch (entity.getName().getString()) {
            case "Donatello" -> DONATELLO;
            case "Leonardo" -> LEONARDO;
            case "Michelangelo" -> MICHELANGELO;
            case "Raphael" -> RAPHAEL;
            default -> null;
        };
        if (skin == null) {
            return;
        }
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(skin),
                lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}
