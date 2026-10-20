package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.dragonfly.DragonflyModel;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DragonflyRenderer extends MobRenderer<Dragonfly, HierarchicalModel<Dragonfly>> {
    public DragonflyRenderer(EntityRendererProvider.@NotNull Context context) {
        super(context, new DragonflyModel(context.bakeLayer(DragonflyModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Dragonfly entity) {
        return entity.getVariantTexture();
    }
}
