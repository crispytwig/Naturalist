package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.animal.crab.CrabModel;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CrabItemLayer extends RenderLayer<Crab, HierarchicalModel<Crab>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public CrabItemLayer(RenderLayerParent<Crab, HierarchicalModel<Crab>> parent, ItemInHandRenderer itemInHandRenderer) {
        super(parent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Crab crab, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack held = crab.getMainHandItem();
        if (held.isEmpty() || !(this.getParentModel() instanceof CrabModel model)) {
            return;
        }

        poseStack.pushPose();
        model.translateToItem(poseStack);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.itemInHandRenderer.renderItem(crab, held, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
