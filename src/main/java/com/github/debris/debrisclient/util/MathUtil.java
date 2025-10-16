package com.github.debris.debrisclient.util;

public class MathUtil {
    public static boolean is2Power(int n) {
        return (n & (n - 1)) == 0;
    }
}
