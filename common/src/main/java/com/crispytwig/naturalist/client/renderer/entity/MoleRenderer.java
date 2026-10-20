package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.mole.MoleModel;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class MoleRenderer extends MobRenderer<Mole, HierarchicalModel<Mole>> {
    public MoleRenderer(EntityRendererProvider.Context context) {
        super(context, new MoleModel(context.bakeLayer(MoleModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Mole entity) {
        return entity.getVariantTexture();
    }

    @Override
    public void render(@NotNull Mole entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
        this.shadowRadius = entity.isRolledUp() ? 0.0F : entity.isBaby() ? 0.25F : 0.4F;
        this.model.root().getChild("mound").visible = entity.isRolledUp();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
