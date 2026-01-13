package com.github.debris.debrisclient.feat;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum QualityColor {
    BLUE,
    AQUA,
    LIGHT_PURPLE,
    NO_COLOR,
    ;

    public static final List<QualityColor> VALUES = ImmutableList.copyOf(values());

    public boolean betterOrEqual(QualityColor other) {
        return this.ordinal() >= other.ordinal();
    }

    public static String transform(String color) {
        if ("gold".equals(color)) return "light_purple";
        return color;
    }

    public static QualityColor fromString(String color) {
        for (QualityColor value : VALUES) {
            if (value.name().toLowerCase().equals(color)) return value;
        }
        return BLUE;
    }

    public static QualityColor parse(String color) {
        return fromString(transform(color));
    }
}
