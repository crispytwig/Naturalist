package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.ant.AntModel;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AntRenderer extends MobRenderer<Ant, HierarchicalModel<Ant>> {
    public AntRenderer(EntityRendererProvider.Context context) {
        super(context, new AntModel(context.bakeLayer(AntModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Ant entity) {
        return entity.getVariantTexture();
    }
}
