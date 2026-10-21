package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.snake.SnakeModel;
import com.crispytwig.naturalist.world.entity.animal.snake.Snake;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SnakeRenderer extends MobRenderer<Snake, HierarchicalModel<Snake>> {
    public SnakeRenderer(EntityRendererProvider.@NotNull Context context) {
        super(context, new SnakeModel(context.bakeLayer(SnakeModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Snake entity) {
        return entity.getVariantTexture();
    }
}
