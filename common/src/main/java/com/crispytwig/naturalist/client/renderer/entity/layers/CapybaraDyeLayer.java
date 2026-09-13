package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.NaturalistEntityModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class CapybaraDyeLayer extends DyeLayer<Capybara, NaturalistEntityModel<Capybara>> {
    private static final Set<DyeColor> BABY_COLORS = EnumSet.of(
            DyeColor.WHITE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.LIME, DyeColor.GRAY,
            DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK);

    private final EnumMap<DyeColor, Identifier> babyTextures = new EnumMap<>(DyeColor.class);

    public CapybaraDyeLayer(RenderLayerParent<NaturalistRenderState<Capybara>, NaturalistEntityModel<Capybara>> parent) {
        super(parent, "capybara");
    }

    @Override
    protected Identifier getDyeTexture(Capybara entity, DyeColor color) {
        if (!entity.isBaby() || !BABY_COLORS.contains(color)) {
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
