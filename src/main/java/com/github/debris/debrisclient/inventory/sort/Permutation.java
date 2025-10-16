package com.github.debris.debrisclient.inventory.sort;

import com.google.common.collect.ImmutableList;

import java.util.function.BiConsumer;

public interface Permutation {
    ProductPermutation EMPTY = new ProductPermutation(ImmutableList.of());

    boolean isEmpty();

    <T> void map(T[] array);

    void map(int[] data);

    <T> void operate(T[] array, BiConsumer<T, T> operation);
}
