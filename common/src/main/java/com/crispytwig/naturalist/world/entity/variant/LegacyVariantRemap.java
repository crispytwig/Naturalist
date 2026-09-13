package com.crispytwig.naturalist.world.entity.variant;

import net.minecraft.nbt.CompoundTag;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class LegacyVariantRemap {
    private static final Map<String, String> BIRD_VARIANTS = Map.of(
            "naturalist:bluejay", "naturalist:blue_jay",
            "naturalist:cardinal", "naturalist:northern_cardinal",
            "naturalist:robin", "naturalist:american_robin",
            "naturalist:sparrow", "naturalist:white_throated_sparrow",
            "naturalist:canary", "naturalist:american_robin",
            "naturalist:finch", "naturalist:carolina_chickadee"
    );
    private static final Set<String> LEGACY_SNAKES = Set.of("naturalist:coral_snake", "naturalist:rattlesnake");

    private LegacyVariantRemap() {
    }

    public static void apply(CompoundTag tag) {
        if (tag == null) {
            return;
        }
        Optional<String> maybeId = tag.getString("id");
        if (maybeId.isEmpty()) {
            return;
        }
        String id = maybeId.get();
        if (!id.startsWith("naturalist:")) {
            return;
        }
        String birdVariant = BIRD_VARIANTS.get(id);
        if (birdVariant != null) {
            tag.putString("id", "naturalist:bird");
            tag.putString(DataDrivenVariantAnimal.VARIANT_TAG, birdVariant);
        } else if (LEGACY_SNAKES.contains(id)) {
            tag.putString("id", "naturalist:snake");
            tag.putString(DataDrivenVariantAnimal.VARIANT_TAG, id);
        }
    }
}
