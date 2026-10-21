package com.crispytwig.naturalist.forge.config;

import com.crispytwig.naturalist.NaturalistClientConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ForgeNaturalistClientConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.BooleanValue GLOW_GOOP_TOOLTIP;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        GLOW_GOOP_TOOLTIP = builder
                .translation("naturalist.configuration." + NaturalistClientConfig.GLOW_GOOP_TOOLTIP_KEY)
                .define(NaturalistClientConfig.GLOW_GOOP_TOOLTIP_KEY, true);

        SPEC = builder.build();
    }

    private ForgeNaturalistClientConfig() {}

    public static boolean isGlowGoopTooltipEnabled() {
        return !SPEC.isLoaded() || GLOW_GOOP_TOOLTIP.get();
    }
}
