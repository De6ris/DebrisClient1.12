package com.github.debris.debrisclient.inventory.sort;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public final class ProductPermutation implements Permutation {
    private final List<Transposition> transpositions;

    public ProductPermutation(List<Transposition> transpositions) {
        this.transpositions = transpositions;
    }

    public List<Transposition> transpositions() {
        return transpositions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ProductPermutation that = (ProductPermutation) obj;
        return Objects.equals(this.transpositions, that.transpositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transpositions);
    }

    @Override
    public String toString() {
        return "ProductPermutation[" +
                "transpositions=" + transpositions + ']';
    }

    @Override
    public boolean isEmpty() {
        return this == Permutation.EMPTY || this.transpositions.isEmpty();
    }

    @Override
    public <T> void map(T[] array) {
        this.transpositions.forEach(x -> x.swap(array));
    }

    @Override
    public void map(int[] data) {
        this.transpositions.forEach(x -> x.swap(data));
    }

    @Override
    public <T> void operate(T[] array, BiConsumer<T, T> operation) {
        this.transpositions.forEach(x -> x.operate(array, operation));
    }

    public ProductPermutation optimize() {
        if (this.isEmpty()) return Permutation.EMPTY;
        int bound = this.transpositions.stream().flatMapToInt(transposition -> IntStream.of(transposition.first(), transposition.second())).max().orElseThrow(AssertionError::new);
        return this.optimize(bound);
    }

    /**
     * @param bound the biggest index in transpositions, allow higher.
     */
    public ProductPermutation optimize(int bound) {
        if (this.isEmpty()) return Permutation.EMPTY;
        int[] data = generateAuxiliaryArray(bound);
        this.map(data);
        List<Transposition> minimal = getMinimalTransposition(data);
        return new ProductPermutation(minimal);
    }

    /**
     * An array of 0,1,...,bound
     */
    private static int[] generateAuxiliaryArray(int bound) {
        int[] data = new int[bound + 1];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
        return data;
    }

    /**
     * We receive a shuffled array, and return a minimal transposition list.
     * <br>
     * This list tell us how to shuffle(from ordered to shuffled), so we sort it(R2S) and reverse(S2R).
     */
    private static List<Transposition> getMinimalTransposition(int[] data) {
        int length = data.length;
        List<Transposition> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int index = 0;

            for (int j = i; j < length; j++) {
                if (data[j] == i) {
                    index = j;
                    break;
                }
            }

            if (index == i) continue;// right position skip transposition

            Transposition e = new Transposition(i, index);
            list.add(e);
            e.swap(data);
        }
        return Lists.reverse(list);
    }
}
