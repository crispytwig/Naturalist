package com.crispytwig.naturalist;

import com.crispytwig.naturalist.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class NaturalistConfig {
    public static final List<String> AQUATIC_MOBS = List.of(
            "anglerfish", "bass", "blobfish", "catfish", "clam", "giantIsopod", "greatWhiteShark",
            "jellyfish", "piranha", "ray", "starfish", "whale"
    );

    public static final List<String> LAND_MOBS = List.of(
            "alligator", "bear", "bird", "blackBear", "boar", "capybara", "crab", "deer", "duck",
            "elephant", "forestFox", "forestRabbit", "giraffe", "hedgehog", "hippo", "komodoDragon",
            "lion", "lizard", "mammoth", "mole", "ostrich", "rat", "rhino", "snake", "tiger",
            "tortoise", "turkey", "vulture", "zebra"
    );

    public static final List<String> BUGS = List.of(
            "ant", "butterfly", "desertScorpion", "dragonfly", "firefly", "jungleScorpion", "snail"
    );

    public static final List<String> MOB_KEYS = Stream.of(AQUATIC_MOBS, LAND_MOBS, BUGS)
            .flatMap(List::stream)
            .sorted()
            .toList();

    private static final Map<String, String> ALIASES = Map.ofEntries(
            Map.entry("bluejay", "bird"),
            Map.entry("canary", "bird"),
            Map.entry("cardinal", "bird"),
            Map.entry("finch", "bird"),
            Map.entry("robin", "bird"),
            Map.entry("sparrow", "bird"),
            Map.entry("coralSnake", "snake"),
            Map.entry("rattlesnake", "snake"),
            Map.entry("caterpillar", "butterfly"),
            Map.entry("fox", "forestFox"),
            Map.entry("rabbit", "forestRabbit")
    );

    public static final String SNAIL_CRUSHING_KEY = "snail_crushing";
    public static final String REMOVE_ALL_BUGS_KEY = "remove_all_bugs";
    public static final String BIRD_HEAD_SLOW_FALLING_KEY = "bird_head_slow_falling";
    public static final String PARROT_FLIGHT_KEY = "parrot_flight";

    private NaturalistConfig() {}

    public static String canonicalKey(String mobName) {
        return ALIASES.getOrDefault(mobName, mobName);
    }

    public static String configKey(String mobKey) {
        StringBuilder sb = new StringBuilder();
        for (char c : mobKey.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.append("_removed").toString();
    }

    public static String mobName(EntityType<?> type) {
        String path = BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath();
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : path.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                sb.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }
        return sb.toString();
    }

    public static boolean isRemoved(EntityType<?> type) {
        return isRemoved(mobName(type));
    }

    public static boolean isRemoved(String mobName) {
        String key = canonicalKey(mobName);
        if (BUGS.contains(key) && Services.CONFIG.areAllBugsRemoved()) {
            return true;
        }
        return Services.CONFIG.isMobRemoved(key);
    }

    public static boolean isSnailCrushingEnabled() {
        return Services.CONFIG.isSnailCrushingEnabled();
    }

    public static boolean isBirdHeadSlowFallingEnabled() {
        return Services.CONFIG.isBirdHeadSlowFallingEnabled();
    }

    public static boolean isParrotFlightEnabled() {
        return Services.CONFIG.isParrotFlightEnabled();
    }
}
