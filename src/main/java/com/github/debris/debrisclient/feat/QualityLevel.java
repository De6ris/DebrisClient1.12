package com.github.debris.debrisclient.feat;

public enum QualityLevel {
    BLUE(QualityColor.BLUE),
    AQUA(QualityColor.AQUA),
    LIGHT_PURPLE(QualityColor.LIGHT_PURPLE),
    NO_LEVEL(QualityColor.UNKNOWN),
    ;

    private final QualityColor qualityColor;

    QualityLevel(QualityColor qualityColor) {
        this.qualityColor = qualityColor;
    }

    public boolean matches(QualityColor color) {
        if (this == NO_LEVEL) throw new AssertionError();
        return color.betterOrEqual(this.qualityColor);
    }
}
