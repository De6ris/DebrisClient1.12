package com.github.debris.debrisclient.util;

import net.minecraft.item.ItemStack;

public class AnvilUtil {
    public static int getPunishment(ItemStack itemStack) {
        return itemStack.getRepairCost();
    }

    public static int getOperations(ItemStack itemStack) {
        return asOperations(getPunishment(itemStack));
    }

    public static int asOperations(int punishment) {
        int cursor = 0;
        int loop = 0;
        while (cursor != punishment && loop < 31) {
            cursor = 2 * cursor + 1;
            loop++;
        }
        return loop;
    }

    public static int asPunishment(int operations) {
        int cursor = 0;
        for (int i = 0; i < operations; i++) {
            cursor = 2 * cursor + 1;
        }
        return cursor;
    }
}
