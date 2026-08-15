package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.localization.EnchantPlanText;
import com.github.debris.debrisclient.util.AnvilUtil;
import com.github.debris.debrisclient.util.EnchantUtil;
import com.github.debris.debrisclient.util.InventoryUtil;
import com.github.debris.debrisclient.util.StringUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

class EnchantCheck {
    static boolean hasUseless(ItemStack stack, List<Slot> bookSlots) {
        List<Slot> useless = filterUseless(stack, bookSlots);
        if (!useless.isEmpty()) {
            useless.forEach(InventoryUtil::dropStack);
            EnchantPlan.actionBar(EnchantPlanText.INVALID_BOOKS);
            return true;
        }
        return false;
    }

    static boolean hasPunishment(List<Slot> bookSlots) {
        List<Slot> punished = filterPunished(bookSlots);
        if (!punished.isEmpty()) {
            punished.forEach(InventoryUtil::dropStack);
            EnchantPlan.actionBar(EnchantPlanText.PUNISHED_BOOKS);
            return true;
        }
        return false;
    }

    static boolean hasDuplicate(List<Enchantment> effective, List<Slot> bookSlots) {
        Set<Enchantment> duplicate = findDuplicate(effective);
        if (!duplicate.isEmpty()) {
            bookSlots.forEach(slot -> {
                if (EnchantUtil.getEnchantments(slot.getStack()).keySet().stream().anyMatch(duplicate::contains)) {
                    InventoryUtil.dropStack(slot);
                }
            });
            EnchantPlan.actionBar(EnchantPlanText.ENCHANTMENT_DUPLICATE.formatChinese(StringUtil.translateEnchantments(duplicate)));
            return true;
        }
        return false;
    }

    static boolean hasConflict(List<Enchantment> effective, List<Slot> bookSlots) {
        List<Enchantment> conflicts = simulateConflict(effective);
        if (!conflicts.isEmpty()) {
            bookSlots.forEach(slot -> {
                if (EnchantUtil.getEnchantments(slot.getStack()).keySet().stream().anyMatch(conflicts::contains))
                    InventoryUtil.dropStack(slot);
            });
            EnchantPlan.actionBar(EnchantPlanText.ENCHANTMENT_CONFLICT.formatChinese(StringUtil.translateEnchantments(conflicts)));
            return true;
        }
        return false;
    }

    private static List<Slot> filterUseless(ItemStack stack, List<Slot> bookSlots) {
        return bookSlots.stream().filter(x -> {
            Map<Enchantment, Integer> enchantments = EnchantUtil.getEnchantments(x.getStack());
            return enchantments.keySet().stream().noneMatch(e -> e.canApply(stack));
        }).collect(Collectors.toList());
    }

    private static List<Slot> filterPunished(List<Slot> bookSlots) {
        return bookSlots.stream().filter(x -> AnvilUtil.getPunishment(x.getStack()) > 0).collect(Collectors.toList());
    }

    private static Set<Enchantment> findDuplicate(List<Enchantment> effective) {
        Set<Enchantment> duplicate = new HashSet<>();
        for (Enchantment enchantment : effective) {
            int frequency = Collections.frequency(effective, enchantment);
            if (frequency > 1) duplicate.add(enchantment);
        }
        return duplicate;
    }

    private static List<Enchantment> simulateConflict(List<Enchantment> enchantments) {
        int size = enchantments.size();
        if (size <= 1) return ImmutableList.of();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Enchantment e1 = enchantments.get(i);
                Enchantment e2 = enchantments.get(j);
                if (!e1.isCompatibleWith(e2)) return ImmutableList.of(e1, e2);
            }
        }
        return ImmutableList.of();
    }
}
