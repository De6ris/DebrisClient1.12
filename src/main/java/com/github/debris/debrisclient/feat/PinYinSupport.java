package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.JechAccess;

import java.util.function.IntSupplier;

public class PinYinSupport {
    public static int compareString(String s1, String s2, IntSupplier fallback) {
        int length1 = s1.length();
        int length2 = s2.length();
        int minLength = Math.min(length1, length2);

        int compare;
        for (int i = 0; i < minLength; i++) {
            char codePoint1 = s1.charAt(i);
            char codePoint2 = s2.charAt(i);
            compare = compareChar(codePoint1, codePoint2, () -> Integer.compare(codePoint1, codePoint2));
            if (compare != 0) return compare;
        }
        compare = Integer.compare(length1, length2);
        return compare != 0 ? compare : fallback.getAsInt();
    }

    public static int compareChar(char c1, char c2, IntSupplier fallback) {
        int compareChar = JechAccess.compareChar(c1, c2);
        return compareChar == 0 ? fallback.getAsInt() : compareChar;
    }

    public static boolean matchesFilter(String entryString, String filterText) {
        return JechAccess.matchesFilter(entryString, filterText);
    }

    public static boolean available() {
        return ModReference.hasMod(ModReference.JECH);
    }
}
