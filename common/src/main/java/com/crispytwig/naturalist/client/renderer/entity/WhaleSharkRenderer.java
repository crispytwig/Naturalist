package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.whaleshark.WhaleSharkBabyModel;
import com.crispytwig.naturalist.client.model.whaleshark.WhaleSharkModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.WhaleSharkInkLayer;
import com.crispytwig.naturalist.common.entity.whaleshark.WhaleShark;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class WhaleSharkRenderer extends NaturalistMobRenderer<WhaleShark> {
    public WhaleSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new WhaleSharkModel(context.bakeLayer(WhaleSharkModel.LAYER_LOCATION)), new WhaleSharkBabyModel(context.bakeLayer(WhaleSharkBabyModel.LAYER_LOCATION)), 0.0F, 0.0F);
        this.addLayer(new WhaleSharkInkLayer<>(this));
    }

    @Override
    protected @NotNull AABB getBoundingBoxForCulling(@NotNull WhaleShark entity, float partialTick) {
        return super.getBoundingBoxForCulling(entity, partialTick).inflate(entity.isBaby() ? 1.0D : 3.0D);
    }
}
