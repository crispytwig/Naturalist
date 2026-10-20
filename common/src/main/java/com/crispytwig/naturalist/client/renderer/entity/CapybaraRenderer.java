package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraBabyModel;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.CapybaraDyeLayer;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CapybaraRenderer extends NaturalistMobRenderer<Capybara> {
    private static final ResourceLocation CAPYBARA = Naturalist.location("textures/entity/capybara/capybara.png");
    private static final ResourceLocation CAPYBARA_BABY = Naturalist.location("textures/entity/capybara/capybara_baby.png");

    public CapybaraRenderer(EntityRendererProvider.Context context) {
        super(context, new CapybaraModel(context.bakeLayer(CapybaraModel.LAYER_LOCATION)), new CapybaraBabyModel(context.bakeLayer(CapybaraBabyModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new CapybaraDyeLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Capybara entity) {
        if (entity.hasNonDefaultVariant()) {
            return entity.isBaby() ? entity.getVariantBabyTexture() : entity.getVariantTexture();
        }
        return entity.isBaby() ? CAPYBARA_BABY : CAPYBARA;
    }
}
