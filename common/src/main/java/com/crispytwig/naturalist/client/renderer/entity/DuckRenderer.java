package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.duck.DuckBabyModel;
import com.crispytwig.naturalist.client.model.duck.DuckModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.common.entity.duck.Duck;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

@SuppressWarnings("unused")
public class DuckRenderer extends NaturalistMobRenderer<Duck> {
    private static final Identifier DUCK = Naturalist.location("textures/entity/duck/duck.png");
    private static final Identifier DUCK_BABY = Naturalist.location("textures/entity/duck/duck_baby.png");
    private static final Identifier QUESO = Naturalist.location("textures/entity/duck/queso.png");

    public DuckRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckModel(context.bakeLayer(DuckModel.LAYER_LOCATION)), new DuckBabyModel(context.bakeLayer(DuckBabyModel.LAYER_LOCATION)), 0.3F);
        this.addLayer(new DyeLayer<>(this, "duck"));
    }

    @Override
    protected Identifier getTexture(Duck entity) {
        if (entity.hasNonDefaultVariant()) {
            return super.getTexture(entity);
        }
        if (entity.isBaby()) {
            return DUCK_BABY;
        }
        if (entity.hasCustomName() && entity.getName().getString().equalsIgnoreCase("Queso")) {
            return QUESO;
        }
        return DUCK;
    }
}
