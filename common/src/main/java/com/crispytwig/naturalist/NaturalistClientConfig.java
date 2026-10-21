package com.crispytwig.naturalist;

import java.util.function.BooleanSupplier;

public final class NaturalistClientConfig {
    public static final String GLOW_GOOP_TOOLTIP_KEY = "glow_goop_tooltip";

    private static BooleanSupplier glowGoopTooltip = () -> true;

    private NaturalistClientConfig() {}

    public static void setGlowGoopTooltip(BooleanSupplier supplier) {
        glowGoopTooltip = supplier;
    }

    public static boolean isGlowGoopTooltipEnabled() {
        return glowGoopTooltip.getAsBoolean();
    }
}
