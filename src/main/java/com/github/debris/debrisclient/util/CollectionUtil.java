package com.github.debris.debrisclient.util;

import java.util.List;

public class CollectionUtil {
    public static <T> boolean maybeAddToList(List<T> list, T e) {
        if (list.contains(e)) return false;
        return list.add(e);
    }
}
