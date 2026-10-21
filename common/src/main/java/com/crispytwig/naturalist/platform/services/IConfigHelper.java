package com.crispytwig.naturalist.platform.services;

public interface IConfigHelper {
    boolean isMobRemoved(String mobName);

    boolean areAllBugsRemoved();

    boolean isSnailCrushingEnabled();

    boolean isBirdHeadSlowFallingEnabled();

    boolean isParrotFlightEnabled();
}
