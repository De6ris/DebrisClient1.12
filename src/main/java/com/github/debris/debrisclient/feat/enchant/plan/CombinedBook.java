package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.util.AnvilUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

class CombinedBook {
    final List<ItemStack> subBooks;
    final int operations;
    final int cacheEffectiveEnchantCost;

    CombinedBook(List<ItemStack> subBooks, int operations, int cacheEffectiveEnchantCost) {
        this.subBooks = subBooks;
        this.operations = operations;
        this.cacheEffectiveEnchantCost = cacheEffectiveEnchantCost;
    }

    int getPunishment() {
        return AnvilUtil.asPunishment(operations);
    }

    int getLevelCost(int operations) {
        return AnvilUtil.asPunishment(operations) + this.getPunishment() + this.cacheEffectiveEnchantCost;
    }
}
