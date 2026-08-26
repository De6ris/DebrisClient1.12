package com.github.debris.debrisclient.config.gui;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.config.api.RequiresMod;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.IConfigBase;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HideConfig {
    private static final Set<IConfigBase> HIDDEN_CONFIG = buildSet();

    public static List<IConfigBase> filter(List<? extends IConfigBase> list) {
        return list.stream()
                .filter(config -> !HIDDEN_CONFIG.contains(config))
                .collect(Collectors.toList());
    }

    private static Set<IConfigBase> buildSet() {
        Set<IConfigBase> set = new HashSet<>();
        for (Field field : DCConfig.class.getFields()) {
            RequiresMod annotation = field.getAnnotation(RequiresMod.class);
            if (annotation == null) continue;
            if (matches(annotation)) continue;
            try {
                set.add((ConfigBase<?>) field.get(null));
            } catch (IllegalAccessException e) {
                DebrisClient.LOGGER.info(e);
            }
        }
        return set;
    }

    private static boolean matches(RequiresMod annotation) {
        switch (annotation.matchType()) {
            case ALL: {
                return Arrays.stream(annotation.value()).allMatch(ModReference::hasMod);
            }
            case ANY: {
                return Arrays.stream(annotation.value()).anyMatch(ModReference::hasMod);
            }
            case NONE: {
                return Arrays.stream(annotation.value()).noneMatch(ModReference::hasMod);
            }
            default: {
                return true;// never happen
            }
        }
    }
}
