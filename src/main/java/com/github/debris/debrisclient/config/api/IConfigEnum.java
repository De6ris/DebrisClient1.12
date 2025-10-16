package com.github.debris.debrisclient.config.api;

import com.github.debris.debrisclient.config.options.ConfigEnumEntryWrapper;

import java.util.List;

public interface IConfigEnum<T extends Enum<T>> {
    T getDefaultEnumValue();

    T getEnumValue();

    List<T> getAllEnumValues();

    List<ConfigEnumEntryWrapper<T>> getWrappers();
}
