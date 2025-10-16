package com.github.debris.debrisclient.inventory.sort;

import java.util.Objects;
import java.util.function.BiConsumer;

public final class Transposition {
    private final int first;
    private final int second;

    public Transposition(int first, int second) {
        if (first < 0 || second < 0) throw new IllegalArgumentException();
        this.first = first;
        this.second = second;
    }

    public <T> void swap(T[] array) {
        T temp = array[second];
        array[second] = array[first];
        array[first] = temp;
    }

    public void swap(int[] data) {
        int temp = data[second];
        data[second] = data[first];
        data[first] = temp;
    }

    public <T> void operate(T[] array, BiConsumer<T, T> operation) {
        operation.accept(array[first], array[second]);
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Transposition that = (Transposition) obj;
        return this.first == that.first &&
                this.second == that.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Transposition[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

}
