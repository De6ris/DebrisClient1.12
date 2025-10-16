package com.github.debris.debrisclient.config.options;

import com.github.debris.debrisclient.util.GenericsUtil;
import com.github.debris.debrisclient.util.StringUtil;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigEnumEntryWrapper<T extends Enum<T>> implements IConfigOptionListEntry {
    private final T value;

    private static final Map<Class<?>, List<ConfigEnumEntryWrapper<?>>> InstanceMap = new HashMap<>();

    private ConfigEnumEntryWrapper(T value) {
        this.value = value;
    }

    public T getEnumValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.value.name();
    }

    @Override
    public String getDisplayName() {
        String name = this.getStringValue();
        String key = String.format("config.enum.%s.%s.name", this.value.getDeclaringClass().getSimpleName().toLowerCase(), name.toLowerCase());
        return StringUtil.translateFallback(key, name);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        List<ConfigEnumEntryWrapper<?>> list = InstanceMap.get(this.value.getDeclaringClass());
        int id = this.value.ordinal();
        int size = list.size();
        if (forward) {
            if (++id >= size) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = size - 1;
            }
        }
        return list.get(id % size);
    }

    @Override
    public IConfigOptionListEntry fromString(String s) {
        List<ConfigEnumEntryWrapper<?>> list = InstanceMap.get(this.value.getDeclaringClass());
        return list.stream().filter(x -> x.getStringValue().equals(s)).findFirst().orElse(this);
    }

    public static <T extends Enum<T>> ConfigEnumEntryWrapper<?> of(T value) {
        Class<T> aClass = value.getDeclaringClass();
        InstanceMap.computeIfAbsent(aClass, key -> createInstances(value));
        return InstanceMap.get(aClass).get(value.ordinal());
    }

    private static <T extends Enum<T>> List<ConfigEnumEntryWrapper<?>> createInstances(T value) {
        Class<T> aClass = value.getDeclaringClass();
        T[] instances = aClass.getEnumConstants();
        return Arrays.stream(instances).map(ConfigEnumEntryWrapper::new).collect(Collectors.toList());
    }

    public static <T extends Enum<T>> List<ConfigEnumEntryWrapper<T>> getWrappers(Class<T> clazz) {
        return GenericsUtil.cast(InstanceMap.get(clazz));
    }
}
