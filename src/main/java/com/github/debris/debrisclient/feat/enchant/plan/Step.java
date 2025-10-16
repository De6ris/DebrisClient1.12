package com.github.debris.debrisclient.feat.enchant.plan;

import net.minecraft.item.ItemStack;

import java.util.List;

class Step {
    final List<ItemStack> books;
    final int levelCost;

    Step(List<ItemStack> books, int levelCost) {
        this.books = books;
        this.levelCost = levelCost;
    }
}
