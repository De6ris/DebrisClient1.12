package com.github.debris.debrisclient.inventory.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Permutations {
    public static <T> ProductPermutation ofOptimal(T[] original, Comparator<T> comparator) {
        return ofRaw(original, comparator).optimize(original.length - 1);
    }

    /**
     * Bubble sorting, records all the transposition operations.
     */
    public static <T> ProductPermutation ofRaw(T[] original, Comparator<T> comparator) {
        int length = original.length;
        if (length <= 1) return Permutation.EMPTY;
        T[] clone = original.clone();
        List<Transposition> list = new ArrayList<>();
        for (int i = 1; i < length; i++) {
            boolean flag = true;
            for (int j = 0; j < length - i; j++) {
                T elementJ = clone[j];
                T elementJ_1 = clone[j + 1];
                int compare = comparator.compare(elementJ, elementJ_1);
                if (compare > 0) {
                    clone[j] = elementJ_1;
                    clone[j + 1] = elementJ;
                    list.add(new Transposition(j, j + 1));
                    flag = false;
                }
            }
            if (flag) break;
        }
        return new ProductPermutation(list);
    }
}
