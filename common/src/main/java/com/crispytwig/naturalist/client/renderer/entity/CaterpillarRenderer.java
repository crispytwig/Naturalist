package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.butterfly.CaterpillarModel;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Caterpillar;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CaterpillarRenderer extends MobRenderer<Caterpillar, HierarchicalModel<Caterpillar>> {
    public CaterpillarRenderer(EntityRendererProvider.Context context) {
        super(context, new CaterpillarModel(context.bakeLayer(CaterpillarModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Caterpillar entity) {
        return entity.getVariantTexture();
    }
}
