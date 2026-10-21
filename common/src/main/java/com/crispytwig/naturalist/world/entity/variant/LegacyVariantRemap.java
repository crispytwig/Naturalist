package com.crispytwig.naturalist.world.entity.variant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Map;
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
    private static final String[] LEGACY_BUTTERFLY = {
            "naturalist:monarch", "naturalist:monarch", "naturalist:clouded_yellow", "naturalist:swallowtail", "naturalist:blue_morpho"
    };
    public static final String BUTTERFLY_ID = "naturalist:butterfly";

    private LegacyVariantRemap() {
    }

    public static void apply(CompoundTag tag) {
        if (tag == null || !tag.contains("id", Tag.TAG_STRING)) {
            return;
        }
        String id = tag.getString("id");
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
        } else if (BUTTERFLY_ID.equals(id)) {
            remapButterflyVariant(tag);
        }
    }

    public static void remapButterflyVariant(CompoundTag tag) {
        if (tag != null && tag.contains(DataDrivenVariantAnimal.VARIANT_TAG, Tag.TAG_ANY_NUMERIC)) {
            tag.putString(DataDrivenVariantAnimal.VARIANT_TAG, legacyButterflyVariant(tag.getInt(DataDrivenVariantAnimal.VARIANT_TAG)));
        }
    }

    public static String legacyButterflyVariant(int index) {
        return LEGACY_BUTTERFLY[Math.floorMod(index, LEGACY_BUTTERFLY.length)];
    }
}
