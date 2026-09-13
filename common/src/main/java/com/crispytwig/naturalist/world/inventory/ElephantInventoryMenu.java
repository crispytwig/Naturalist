package com.crispytwig.naturalist.world.inventory;

import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import com.crispytwig.naturalist.registry.NaturalistMenus;

import java.util.function.Predicate;

public class ElephantInventoryMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 27;
    public static final int CHEST_SIZE = 25;
    public static final int SADDLE_SLOT = 25;
    public static final int BANNER_SLOT = 26;
    public static int clientOpenEntityId = -1;

    private final Container container;
    @Nullable
    private final Elephant elephant;

    public ElephantInventoryMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(CONTAINER_SIZE), resolveClientElephant(playerInventory));
    }

    public ElephantInventoryMenu(int id, Inventory playerInventory, Container container, @Nullable Elephant elephant) {
        super(NaturalistMenus.ELEPHANT.get(), id);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        this.elephant = elephant;
        container.startOpen(playerInventory.player);

        int chestIndex = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                this.addSlot(new ChestSlot(container, chestIndex++, 80 + col * 18, 18 + row * 18));
            }
        }
        this.addSlot(new RestrictedSlot(container, SADDLE_SLOT, 8, stack -> stack.is(Items.SADDLE)));
        this.addSlot(new RestrictedSlot(container, BANNER_SLOT, 26, stack -> stack.is(ItemTags.BANNERS)));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, 9 + col + row * 9, 8 + col * 18, 122 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 180));
        }
    }

    @Nullable
    private static Elephant resolveClientElephant(Inventory playerInventory) {
        Entity entity = playerInventory.player.level().getEntity(clientOpenEntityId);
        return entity instanceof Elephant value ? value : null;
    }

    @Nullable
    public Elephant getElephant() {
        return this.elephant;
    }

    public boolean isChested() {
        return this.elephant != null && this.elephant.isChested();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.elephant != null) {
            return this.elephant.isAlive() && this.elephant.isOwnedBy(player) && this.elephant.distanceTo(player) < 8.0F;
        }
        return this.container.stillValid(player);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < CONTAINER_SIZE) {
                if (!this.moveItemStackTo(stack, CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.is(Items.SADDLE) && !this.slots.get(SADDLE_SLOT).hasItem()) {
                if (!this.moveItemStackTo(stack, SADDLE_SLOT, SADDLE_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.is(ItemTags.BANNERS) && !this.slots.get(BANNER_SLOT).hasItem()) {
                if (!this.moveItemStackTo(stack, BANNER_SLOT, BANNER_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.isChested() || !this.moveItemStackTo(stack, 0, CHEST_SIZE, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    private class ChestSlot extends Slot {
        ChestSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ElephantInventoryMenu.this.isChested();
        }

        @Override
        public boolean isActive() {
            return ElephantInventoryMenu.this.isChested();
        }
    }

    private static class RestrictedSlot extends Slot {
        private final Predicate<ItemStack> filter;

        RestrictedSlot(Container container, int index, int x, Predicate<ItemStack> filter) {
            super(container, index, x, 90);
            this.filter = filter;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return this.filter.test(stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
