package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.scorpion.DesertScorpionModel;
import com.crispytwig.naturalist.world.entity.animal.scorpion.DesertScorpion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DesertScorpionRenderer extends MobRenderer<DesertScorpion, HierarchicalModel<DesertScorpion>> {
    public DesertScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new DesertScorpionModel(context.bakeLayer(DesertScorpionModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DesertScorpion entity) {
        return entity.getVariantTexture();
    }
}
