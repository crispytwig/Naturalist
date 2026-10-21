package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.clam.ClamModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.ClamHeldItemLayer;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ClamRenderer extends MobRenderer<Clam, HierarchicalModel<Clam>> {
    public ClamRenderer(EntityRendererProvider.Context context) {
        super(context, new ClamModel(context.bakeLayer(ClamModel.LAYER_LOCATION)), 0.0F);
        this.addLayer(new ClamHeldItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Clam entity) {
        return entity.getVariantTexture();
    }
}
