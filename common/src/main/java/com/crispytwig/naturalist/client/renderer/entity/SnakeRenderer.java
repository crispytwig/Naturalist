package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.snake.SnakeModel;
import com.crispytwig.naturalist.world.entity.animal.snake.Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SnakeRenderer extends NaturalistMobRenderer<Snake> {
    public SnakeRenderer(EntityRendererProvider.Context context) {
        super(context, new SnakeModel(context.bakeLayer(SnakeModel.LAYER_LOCATION)), 0.4F);
    }
}
