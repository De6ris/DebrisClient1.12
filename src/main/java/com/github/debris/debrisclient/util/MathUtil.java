package com.github.debris.debrisclient.util;

import net.minecraft.util.math.MathHelper;

public class MathUtil {
    public static boolean is2Power(int n) {
        return (n & (n - 1)) == 0;
    }

    public static int asPercentage(double ratio) {
        return MathHelper.clamp((int) ratio * 100, 0, 100);
    }
}
