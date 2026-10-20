package com.crispytwig.naturalist.client.gui.screens.inventory;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.crispytwig.naturalist.world.inventory.ElephantInventoryMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class ElephantInventoryScreen extends AbstractContainerScreen<ElephantInventoryMenu> {
    private static final ResourceLocation TEXTURE = Naturalist.location("textures/gui/container/elephant.png");

    private static final int TEXTURE_SIZE = 288;

    public ElephantInventoryScreen(ElephantInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 204;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, TEXTURE_SIZE, TEXTURE_SIZE);
        if (this.menu.isChested()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 17, 176.0F, 17.0F, 90, 90, TEXTURE_SIZE, TEXTURE_SIZE);
        }
        Elephant elephant = this.menu.getElephant();
        if (elephant != null) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics,
                    this.leftPos + 8, this.topPos + 18, this.leftPos + 77, this.topPos + 87,
                    16, 0.0625F, mouseX, mouseY, elephant);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
