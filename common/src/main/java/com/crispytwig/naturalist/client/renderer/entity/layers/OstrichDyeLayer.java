package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;

public class OstrichDyeLayer extends DyeLayer<Ostrich, HierarchicalModel<Ostrich>> {
    private final EnumMap<DyeColor, ResourceLocation> babyTextures = new EnumMap<>(DyeColor.class);

    public OstrichDyeLayer(RenderLayerParent<Ostrich, HierarchicalModel<Ostrich>> parent) {
        super(parent, "ostrich");
    }

    @Override
    protected ResourceLocation getDyeTexture(Ostrich entity, DyeColor color) {
        if (!entity.isBaby()) {
            return super.getDyeTexture(entity, color);
        }
        ResourceLocation texture = this.babyTextures.get(color);
        if (texture == null) {
            texture = this.getDyeTexture(color.getName() + "_baby");
            this.babyTextures.put(color, texture);
        }
        return texture;
    }
}
