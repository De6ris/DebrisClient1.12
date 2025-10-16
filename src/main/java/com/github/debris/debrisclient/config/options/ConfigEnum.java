package com.github.debris.debrisclient.config.options;

import com.github.debris.debrisclient.config.api.IConfigEnum;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigEnum<T extends Enum<T>> extends ConfigOptionList implements IConfigEnum<T> {
    private final List<T> VALUES;

    public ConfigEnum(String name, T defaultValue, String comment) {
        super(name, ConfigEnumEntryWrapper.of(defaultValue), comment);
        this.VALUES = Arrays.stream(defaultValue.getDeclaringClass().getEnumConstants()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getDefaultEnumValue() {
        return (T) ((ConfigEnumEntryWrapper<?>) this.getDefaultOptionListValue()).getEnumValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getEnumValue() {
        return (T) ((ConfigEnumEntryWrapper<?>) this.getOptionListValue()).getEnumValue();
    }

    @Override
    public List<T> getAllEnumValues() {
        return VALUES;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConfigEnumEntryWrapper<T>> getWrappers() {
        ConfigEnumEntryWrapper<T> entry = (ConfigEnumEntryWrapper<T>) this.getDefaultOptionListValue();
        return ConfigEnumEntryWrapper.getWrappers(entry.getEnumValue().getDeclaringClass());
    }
}
