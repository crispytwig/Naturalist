package com.crispytwig.naturalist.world.level;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class LegacyIds {
    public static final Map<String, String> BLOCKS = Map.of(
            "naturalist:teddy_bear", "naturalist:plush_bear"
    );
    public static final Map<String, String> ITEMS = Map.of(
            "naturalist:teddy_bear", "naturalist:plush_bear",
            "naturalist:bug_net", "naturalist:capture_net",
            "naturalist:snail_bucket", "naturalist:snail"
    );

    private static final ResourceLocation BLOCK_REGISTRY = new ResourceLocation("minecraft", "block");
    private static final ResourceLocation ITEM_REGISTRY = new ResourceLocation("minecraft", "item");

    public static @Nullable ResourceLocation renamedTarget(ResourceLocation registry, ResourceLocation id) {
        Map<String, String> renames = BLOCK_REGISTRY.equals(registry) ? BLOCKS : ITEM_REGISTRY.equals(registry) ? ITEMS : null;
        String target = renames == null ? null : renames.get(id.toString());
        return target == null ? null : new ResourceLocation(target);
    }

    private LegacyIds() {
    }
}
