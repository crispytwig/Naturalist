package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.block.SnailShellBlock;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import net.minecraft.world.item.DyeColor;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;

public class NaturalistCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Naturalist.MOD_ID);

    private static final String[] STARFISH_TAB_ORDER = {"red", "orange", "blue", "purple"};

    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register("item_group",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(NaturalistRegistry.CAPTURE_NET.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.naturalist.tab"))
                    .displayItems((params, output) -> NaturalistRegistry.ITEMS.getEntries().forEach(entry -> {
                        Item item = entry.get();
                        if (item == NaturalistRegistry.SNAIL.get()) {
                            acceptSnailColors(output, item);
                        } else if (item == NaturalistRegistry.SNAIL_SHELL.get()) {
                            acceptShellColors(output);
                        } else if (item == NaturalistRegistry.STARFISH_BUCKET.get()) {
                            acceptVariants(output, item, STARFISH_TAB_ORDER);
                        } else if (item instanceof NaturalistBucketItem bucketItem && bucketItem.getLegacyVariantNames() != null) {
                            acceptVariants(output, item, bucketItem.getLegacyVariantNames());
                        } else {
                            output.accept(item);
                        }
                    }))
                    .build()
    );

    private static void acceptVariants(CreativeModeTab.Output output, Item item, String[] variantNames) {
        for (String name : variantNames) {
            CompoundTag tag = new CompoundTag();
            tag.putString(DataDrivenVariantAnimal.VARIANT_TAG, Naturalist.location(name).toString());
            acceptWithTag(output, item, tag);
        }
    }

    private static void acceptSnailColors(CreativeModeTab.Output output, Item item) {
        for (Snail.Color color : Snail.Color.BY_ID) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Color", color.getId());
            acceptWithTag(output, item, tag);
        }
    }

    private static void acceptShellColors(CreativeModeTab.Output output) {
        for (DyeColor color : DyeColor.values()) {
            output.accept(SnailShellBlock.getShellStack(color));
        }
    }

    private static void acceptWithTag(CreativeModeTab.Output output, Item item, CompoundTag tag) {
        ItemStack stack = new ItemStack(item);
        CustomData data = CustomData.of(tag);
        stack.set(DataComponents.CUSTOM_DATA, data);
        stack.set(DataComponents.BUCKET_ENTITY_DATA, data);
        output.accept(stack);
    }
}
