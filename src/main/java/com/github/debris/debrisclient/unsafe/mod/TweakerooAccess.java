package com.github.debris.debrisclient.unsafe.mod;

import fi.dy.masa.tweakeroo.config.Configs;
import fi.dy.masa.tweakeroo.config.FeatureToggle;

import java.util.OptionalDouble;

public class TweakerooAccess {
    public static OptionalDouble getFlySpeed() {
        if (FeatureToggle.TWEAK_FLY_SPEED.getBooleanValue()) {
            return OptionalDouble.of(Configs.getActiveFlySpeedConfig().getDoubleValue());
        }
        return OptionalDouble.empty();
    }
}
