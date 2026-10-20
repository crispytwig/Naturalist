package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.scorpion.JungleScorpionModel;
import com.crispytwig.naturalist.world.entity.animal.scorpion.JungleScorpion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class JungleScorpionRenderer extends MobRenderer<JungleScorpion, HierarchicalModel<JungleScorpion>> {
    public JungleScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new JungleScorpionModel(context.bakeLayer(JungleScorpionModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull JungleScorpion entity) {
        return entity.getVariantTexture();
    }
}
