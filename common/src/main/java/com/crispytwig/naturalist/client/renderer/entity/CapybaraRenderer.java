package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraBabyModel;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.CapybaraDyeLayer;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

@SuppressWarnings("unused")
public class CapybaraRenderer extends NaturalistMobRenderer<Capybara> {
    private static final Identifier CAPYBARA = Naturalist.location("textures/entity/capybara/capybara.png");
    private static final Identifier CAPYBARA_BABY = Naturalist.location("textures/entity/capybara/capybara_baby.png");

    public CapybaraRenderer(EntityRendererProvider.Context context) {
        super(context, new CapybaraModel(context.bakeLayer(CapybaraModel.LAYER_LOCATION)), new CapybaraBabyModel(context.bakeLayer(CapybaraBabyModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new CapybaraDyeLayer(this));
    }

    @Override
    protected Identifier getTexture(Capybara entity) {
        if (entity.hasNonDefaultVariant()) {
            return super.getTexture(entity);
        }
        return entity.isBaby() ? CAPYBARA_BABY : CAPYBARA;
    }
}
