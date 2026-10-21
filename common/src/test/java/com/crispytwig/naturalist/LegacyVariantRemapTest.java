package com.crispytwig.naturalist;

import com.crispytwig.naturalist.world.entity.variant.LegacyVariantRemap;
import com.crispytwig.naturalist.world.level.LegacyIds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LegacyVariantRemapTest {
    private static CompoundTag butterfly(int variant) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "naturalist:butterfly");
        tag.putInt("Variant", variant);
        return tag;
    }

    @Test
    void oldMonarchStaysMonarch() {
        CompoundTag tag = butterfly(1);
        LegacyVariantRemap.apply(tag);
        assertEquals("naturalist:monarch", tag.getString("Variant"));
    }

    @Test
    void oldBlueMorphoStaysBlueMorpho() {
        CompoundTag tag = butterfly(4);
        LegacyVariantRemap.apply(tag);
        assertEquals("naturalist:blue_morpho", tag.getString("Variant"));
    }

    @Test
    void cabbageWhiteBecomesMonarch() {
        CompoundTag tag = butterfly(0);
        LegacyVariantRemap.apply(tag);
        assertEquals("naturalist:monarch", tag.getString("Variant"));
    }

    @Test
    void stringVariantIsUntouched() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "naturalist:butterfly");
        tag.putString("Variant", "naturalist:red_admiral");
        LegacyVariantRemap.apply(tag);
        assertEquals("naturalist:red_admiral", tag.getString("Variant"));
    }

    @Test
    void renamedItemIds() {
        assertEquals(Map.of(
                "naturalist:teddy_bear", "naturalist:plush_bear",
                "naturalist:bug_net", "naturalist:capture_net",
                "naturalist:snail_bucket", "naturalist:snail"), LegacyIds.ITEMS);
        assertEquals(Map.of("naturalist:teddy_bear", "naturalist:plush_bear"), LegacyIds.BLOCKS);
    }

    @Test
    void registryLookupRemapsTeddyBearBlockAndItem() {
        ResourceLocation block = new ResourceLocation("minecraft", "block");
        ResourceLocation item = new ResourceLocation("minecraft", "item");
        assertEquals(new ResourceLocation("naturalist", "plush_bear"), LegacyIds.renamedTarget(block, new ResourceLocation("naturalist", "teddy_bear")));
        assertEquals(new ResourceLocation("naturalist", "capture_net"), LegacyIds.renamedTarget(item, new ResourceLocation("naturalist", "bug_net")));
        assertEquals(new ResourceLocation("naturalist", "snail"), LegacyIds.renamedTarget(item, new ResourceLocation("naturalist", "snail_bucket")));
    }

    @Test
    void registryLookupLeavesOtherIdsAndRegistriesAlone() {
        assertNull(LegacyIds.renamedTarget(new ResourceLocation("minecraft", "block"), new ResourceLocation("naturalist", "bug_net")));
        assertNull(LegacyIds.renamedTarget(new ResourceLocation("minecraft", "entity_type"), new ResourceLocation("naturalist", "teddy_bear")));
        assertNull(LegacyIds.renamedTarget(new ResourceLocation("minecraft", "item"), new ResourceLocation("naturalist", "boar_spawn_egg")));
    }
}
