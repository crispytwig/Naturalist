package com.crispytwig.naturalist.client.model.item;

import net.minecraft.nbt.CompoundTag;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.world.entity.variant.MobVariantUtil;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class VariantItemModels {
    public static final String MODEL_FOLDER = "item/variant";

    private VariantItemModels() {
    }

    public static Map<ResourceLocation, NaturalistBucketItem> collectVariantItems() {
        Map<ResourceLocation, NaturalistBucketItem> items = new HashMap<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof NaturalistBucketItem bucketItem) {
                items.put(BuiltInRegistries.ITEM.getKey(item), bucketItem);
            }
        }
        return items;
    }

    public static List<ResourceLocation> scanExtraModels(ResourceManager resourceManager) {
        return resourceManager.listResources("models/" + MODEL_FOLDER, path -> path.getPath().endsWith(".json")).keySet().stream()
                .map(file -> new ResourceLocation(file.getNamespace(),
                        file.getPath().substring("models/".length(), file.getPath().length() - ".json".length())))
                .toList();
    }

    public static Optional<ResourceLocation> resolveModelId(NaturalistBucketItem item, ItemStack stack, @Nullable Level level) {
        CompoundTag customData = stack.getTag();
        if (customData == null) {
            return Optional.empty();
        }
        Optional<ResourceLocation> id = item.readVariantId(customData);
        if (id.isEmpty()) {
            return Optional.empty();
        }
        Level lookupLevel = level != null ? level : Minecraft.getInstance().level;
        if (lookupLevel == null) {
            return Optional.empty();
        }
        return NaturalistMobVariants.findRegistry(item.getVariantEntityType())
                .flatMap(registry -> MobVariantUtil.byId(lookupLevel.registryAccess(), registry, id.get()))
                .flatMap(holder -> holder.value().itemModel());
    }
}
