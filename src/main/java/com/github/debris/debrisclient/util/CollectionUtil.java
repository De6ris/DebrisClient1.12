package com.github.debris.debrisclient.util;

import java.util.List;
import java.util.Map;

public class CollectionUtil {
    public static <T> boolean maybeAddToList(List<T> list, T e) {
        if (list.contains(e)) return false;
        return list.add(e);
    }

    public static <K, V> Map.Entry<K, V> getFirstEntry(Map<K, V> map) {
        assert !map.isEmpty();
        return map.entrySet().stream().findFirst().get();
    }
}
