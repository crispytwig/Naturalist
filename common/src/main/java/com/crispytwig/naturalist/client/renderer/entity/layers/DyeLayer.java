package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;

public class DyeLayer<T extends LivingEntity & DyeableAnimal, M extends EntityModel<? super NaturalistRenderState<T>>> extends RenderLayer<NaturalistRenderState<T>, M> {
    private final String folder;
    private final EnumMap<DyeColor, Identifier> textures = new EnumMap<>(DyeColor.class);

    public DyeLayer(RenderLayerParent<NaturalistRenderState<T>, M> parent, String folder) {
        super(parent);
        this.folder = folder;
    }

    protected Identifier getDyeTexture(T entity, DyeColor color) {
        Identifier texture = this.textures.get(color);
        if (texture == null) {
            texture = this.getDyeTexture(color.getName());
            this.textures.put(color, texture);
        }
        return texture;
    }

    protected Identifier getDyeTexture(String name) {
        return Naturalist.location("textures/entity/" + this.folder + "/dye/" + name + ".png");
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<T> state, float yRot, float xRot) {
        T entity = state.entity;
        DyeColor color = entity.getDyeColor();
        if (color == null || state.isInvisible) {
            return;
        }
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(this.getDyeTexture(entity, color)),
                lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}
