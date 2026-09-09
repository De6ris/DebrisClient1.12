package com.github.debris.debrisclient.feat.enchant.preview;

import java.util.Arrays;

public class EnchantingTableData {
    int[] enchantLevel;
    int[] clueId;
    int[] clueLevel;

    public EnchantingTableData(int[] enchantLevel, int[] clueId, int[] clueLevel) {
        this.enchantLevel = enchantLevel.clone();
        this.clueId = clueId.clone();
        this.clueLevel = clueLevel.clone();
    }

    public boolean equals(int[] enchantLevel, int[] clueId, int[] clueLevel) {
        return Arrays.equals(enchantLevel, this.enchantLevel)
                && Arrays.equals(clueId, this.clueId)
                && Arrays.equals(clueLevel, this.clueLevel);
    }
}
