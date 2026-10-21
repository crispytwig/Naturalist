package com.crispytwig.naturalist.fabric.config;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.platform.services.IConfigHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class FabricNaturalistConfig implements IConfigHelper {
    private static final Map<String, Boolean> VALUES = new HashMap<>();
    private static boolean snailCrushing = false;
    private static boolean removeAllBugs = false;
    private static boolean birdHeadSlowFalling = true;
    private static boolean parrotFlight = true;

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("naturalist-server.properties");
        Properties properties = new Properties();
        for (String key : NaturalistConfig.MOB_KEYS) {
            properties.setProperty(NaturalistConfig.configKey(key), "false");
        }
        properties.setProperty(NaturalistConfig.REMOVE_ALL_BUGS_KEY, "false");
        properties.setProperty(NaturalistConfig.SNAIL_CRUSHING_KEY, "false");
        properties.setProperty(NaturalistConfig.BIRD_HEAD_SLOW_FALLING_KEY, "true");
        properties.setProperty(NaturalistConfig.PARROT_FLIGHT_KEY, "true");

        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                properties.load(reader);
            } catch (IOException e) {
                Naturalist.LOGGER.error("Failed to read Naturalist config, using defaults", e);
            }
        }

        for (String key : NaturalistConfig.MOB_KEYS) {
            VALUES.put(key, Boolean.parseBoolean(properties.getProperty(NaturalistConfig.configKey(key), "false")));
        }
        removeAllBugs = Boolean.parseBoolean(properties.getProperty(NaturalistConfig.REMOVE_ALL_BUGS_KEY, "false"));
        snailCrushing = Boolean.parseBoolean(properties.getProperty(NaturalistConfig.SNAIL_CRUSHING_KEY, "false"));
        birdHeadSlowFalling = Boolean.parseBoolean(properties.getProperty(NaturalistConfig.BIRD_HEAD_SLOW_FALLING_KEY, "true"));
        parrotFlight = Boolean.parseBoolean(properties.getProperty(NaturalistConfig.PARROT_FLIGHT_KEY, "true"));

        try (Writer writer = Files.newBufferedWriter(path)) {
            properties.store(writer, "Naturalist server config. Set (mob)_removed=true to disable a mob.");
        } catch (IOException e) {
            Naturalist.LOGGER.error("Failed to write Naturalist config", e);
        }
    }

    @Override
    public boolean isMobRemoved(String canonicalKey) {
        return VALUES.getOrDefault(canonicalKey, false);
    }

    @Override
    public boolean areAllBugsRemoved() {
        return removeAllBugs;
    }

    @Override
    public boolean isSnailCrushingEnabled() {
        return snailCrushing;
    }

    @Override
    public boolean isBirdHeadSlowFallingEnabled() {
        return birdHeadSlowFalling;
    }

    @Override
    public boolean isParrotFlightEnabled() {
        return parrotFlight;
    }
}
