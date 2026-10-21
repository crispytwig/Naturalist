package com.crispytwig.naturalist.platform;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.services.IConfigHelper;
import com.crispytwig.naturalist.platform.services.IPlatformHelper;
import com.crispytwig.naturalist.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public final class Services {
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final IConfigHelper CONFIG = load(IConfigHelper.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    private Services() {}

    public static <T> T load(Class<T> clazz) {
        final T service = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Naturalist.LOGGER.debug("Loaded {} for service {}", service, clazz);
        return service;
    }
}
