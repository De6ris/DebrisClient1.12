package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.feat.enchant.EnchantSystem;
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

    int getPunishment(EnchantSystem system) {
        return system.asPunishment(operations);
    }

    int getLevelCost(EnchantSystem system, int operations) {
        return system.asPunishment(operations) + this.getPunishment(system) + this.cacheEffectiveEnchantCost;
    }
}
