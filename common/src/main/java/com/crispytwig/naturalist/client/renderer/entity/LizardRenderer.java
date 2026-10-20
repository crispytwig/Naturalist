package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.lizard.LizardModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.lizard.Lizard;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class LizardRenderer extends MobRenderer<Lizard, HierarchicalModel<Lizard>> {
    public LizardRenderer(EntityRendererProvider.@NotNull Context context) {
        super(context, new LizardModel(context.bakeLayer(LizardModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(new DyeLayer<>(this, "lizard"));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Lizard entity) {
        return entity.getVariantTexture();
    }
}
