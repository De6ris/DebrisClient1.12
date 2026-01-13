package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.config.options.ConfigEnum;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.InfoUtils;

import java.util.function.BooleanSupplier;

public class ConfigFactory {
    private static final String DEFAULT_COMMENT = "no comment";

    public static ConfigBoolean ofBoolean(String name) {
        return ofBoolean(name, false);
    }

    public static ConfigBoolean ofBoolean(String name, boolean defaultValue) {
        return ofBoolean(name, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigBoolean ofBoolean(String name, boolean defaultValue, String comment) {
        return new ConfigBoolean(name, defaultValue, comment);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue) {
        return ofInteger(name, defaultValue, minValue, maxValue, false);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        return ofInteger(name, defaultValue, minValue, maxValue, useSlider, DEFAULT_COMMENT);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue, boolean useSlider, String comment) {
        return new ConfigInteger(name, defaultValue, minValue, maxValue, useSlider, comment);
    }

    public static ConfigColor ofColor(String name, String defaultValue, String comment) {
        return new ConfigColor(name, defaultValue, comment);
    }

    public static ConfigString ofString(String name, String defaultValue) {
        return new ConfigString(name, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigStringList ofStringList(String name, ImmutableList<String> defaultValue) {
        return ofStringList(name, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigStringList ofStringList(String name, ImmutableList<String> defaultValue, String comment) {
        return new ConfigStringList(name, defaultValue, comment);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString) {
        return ofHotkey(name, defaultStorageString, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, String comment) {
        return ofHotkey(name, defaultStorageString, KeybindSettings.DEFAULT, comment);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, KeybindSettings settings) {
        return ofHotkey(name, defaultStorageString, settings, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, KeybindSettings settings, String comment) {
        return new ConfigHotkey(name, defaultStorageString, settings, comment);
    }

    public static <T extends Enum<T>> ConfigEnum<T> ofEnum(String name, T defaultValue) {
        return new ConfigEnum<>(name, defaultValue, DEFAULT_COMMENT);
    }

    public static <T extends Enum<T>> ConfigEnum<T> ofEnum(String name, T defaultValue, String comment) {
        return new ConfigEnum<>(name, defaultValue, comment);
    }

    public static void setToggleCallback(ConfigHotkey config, IHotkeyCallback rawCallback, BooleanSupplier newValue) {
        config.getKeybind().setCallback((action, key) -> {
            boolean success = rawCallback.onKeyAction(action, key);
            InfoUtils.printBooleanConfigToggleMessage(config.getPrettyName(), newValue.getAsBoolean());
            return success;
        });
    }
}