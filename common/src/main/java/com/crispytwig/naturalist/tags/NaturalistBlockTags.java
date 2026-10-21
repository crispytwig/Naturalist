package com.crispytwig.naturalist.tags;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class NaturalistBlockTags {
    public static final TagKey<Block> FIREFLIES_SPAWNABLE_ON = tag("fireflies_spawnable_on");
    public static final TagKey<Block> DRAGONFLIES_SPAWNABLE_ON = tag("dragonflies_spawnable_on");
    public static final TagKey<Block> BUTTERFLIES_SPAWNABLE_ON = tag("butterflies_spawnable_on");
    public static final TagKey<Block> VULTURES_SPAWNABLE_ON = tag("vultures_spawnable_on");
    public static final TagKey<Block> DUCKS_SPAWNABLE_ON = tag("ducks_spawnable_on");
    public static final TagKey<Block> RHINO_CHARGE_BREAKABLE = tag("rhino_charge_breakable");
    public static final TagKey<Block> VULTURE_PERCH_BLOCKS = tag("vulture_perch_blocks");
    public static final TagKey<Block> CATTAIL_PLACEABLE = tag("cattail_placeable");
    public static final TagKey<Block> ALLIGATOR_EGG_LAYABLE_ON = tag("alligator_egg_layable_on");
    public static final TagKey<Block> TORTOISE_EGG_LAYABLE_ON = tag("tortoise_egg_layable_on");
    public static final TagKey<Block> SHELLSTONE = tag("shellstone");
    public static final TagKey<Block> MOTHS_ATTRACTED_TO = tag("moths_attracted_to");
    public static final TagKey<Block> KOMODO_DRAGONS_SPAWNABLE_ON = tag("komodo_dragons_spawnable_on");
    public static final TagKey<Block> OSTRICH_EGG_LAYABLE_ON = tag("ostrich_egg_layable_on");
    public static final TagKey<Block> SCORPIONS_SPAWNABLE_ON = tag("scorpions_spawnable_on");
    public static final TagKey<Block> SNAIL_SHELL_BLACKLIST = tag("snail_shell_blacklist");

    private static TagKey<Block> tag(@NotNull String name) {
        return TagKey.create(Registries.BLOCK, Naturalist.location(name));
    }
}
