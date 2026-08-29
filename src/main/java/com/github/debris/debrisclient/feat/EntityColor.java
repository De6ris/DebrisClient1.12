package com.github.debris.debrisclient.feat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.text.TextFormatting;

import java.util.EnumMap;

public class EntityColor {
    private static final EnumMap<EnumCreatureType, TextFormatting> COLOR_MAP = new EnumMap<>(EnumCreatureType.class);

    public static TextFormatting getColor(Entity entity) {
        for (EnumCreatureType type : COLOR_MAP.keySet()) {
            if (type.getCreatureClass().isInstance(entity)) return COLOR_MAP.get(type);
        }
        return TextFormatting.WHITE;
    }

    static {
        COLOR_MAP.put(EnumCreatureType.MONSTER, TextFormatting.RED);
        COLOR_MAP.put(EnumCreatureType.CREATURE, TextFormatting.GREEN);
        COLOR_MAP.put(EnumCreatureType.AMBIENT, TextFormatting.BLACK);
        COLOR_MAP.put(EnumCreatureType.WATER_CREATURE, TextFormatting.AQUA);
    }
}
