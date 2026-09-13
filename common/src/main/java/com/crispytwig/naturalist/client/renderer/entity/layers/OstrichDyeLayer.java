package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;

public class OstrichDyeLayer extends DyeLayer<Ostrich, NaturalistEntityModel<Ostrich>> {
    private final EnumMap<DyeColor, Identifier> babyTextures = new EnumMap<>(DyeColor.class);

    public OstrichDyeLayer(RenderLayerParent<NaturalistRenderState<Ostrich>, NaturalistEntityModel<Ostrich>> parent) {
        super(parent, "ostrich");
    }

    @Override
    protected Identifier getDyeTexture(Ostrich entity, DyeColor color) {
        if (!entity.isBaby()) {
            return super.getDyeTexture(entity, color);
        }
        Identifier texture = this.babyTextures.get(color);
        if (texture == null) {
            texture = this.getDyeTexture(color.getName() + "_baby");
            this.babyTextures.put(color, texture);
        }
        return texture;
    }
}
