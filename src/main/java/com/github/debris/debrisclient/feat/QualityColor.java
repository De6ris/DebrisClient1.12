package com.github.debris.debrisclient.feat;

import com.google.common.collect.ImmutableList;

import java.util.List;

// the enum is ordered
public enum QualityColor {
    DARK_RED,
    DARK_GRAY,
    RED,
    YELLOW,
    BLUE,
    AQUA,
    LIGHT_PURPLE,
    GOLD,
    UNKNOWN,
    ;

    public static final List<QualityColor> VALUES = ImmutableList.copyOf(values());

    public boolean betterOrEqual(QualityColor other) {
        return this.ordinal() >= other.ordinal();
    }

    public static QualityColor fromString(String color) {
        for (QualityColor value : VALUES) {
            if (value.name().toLowerCase().equals(color)) return value;
        }
        return UNKNOWN;
    }

}
