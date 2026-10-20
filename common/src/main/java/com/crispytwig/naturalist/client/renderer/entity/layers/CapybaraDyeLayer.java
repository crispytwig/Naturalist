package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class CapybaraDyeLayer extends DyeLayer<Capybara, HierarchicalModel<Capybara>> {
    private static final Set<DyeColor> BABY_COLORS = EnumSet.of(
            DyeColor.WHITE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.LIME, DyeColor.GRAY,
            DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK);

    private final EnumMap<DyeColor, ResourceLocation> babyTextures = new EnumMap<>(DyeColor.class);

    public CapybaraDyeLayer(RenderLayerParent<Capybara, HierarchicalModel<Capybara>> parent) {
        super(parent, "capybara");
    }

    @Override
    protected ResourceLocation getDyeTexture(Capybara entity, DyeColor color) {
        if (!entity.isBaby() || !BABY_COLORS.contains(color)) {
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
