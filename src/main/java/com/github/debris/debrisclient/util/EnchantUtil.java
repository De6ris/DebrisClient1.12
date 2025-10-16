package com.github.debris.debrisclient.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantUtil {
    private static final Map<Enchantment, List<Enchantment>> CONFLICT_CACHE = new HashMap<>();

    public static int calculateEnchantmentCost(Map<Enchantment, Integer> map) {
        int ret = 0;
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            int rawUnitCost = getUnitCost(entry.getKey().getRarity());
            int unitCost = Math.max(1, rawUnitCost / 2);// discount for books
            ret += unitCost * entry.getValue();
        }
        return ret;
    }

    public static int getUnitCost(Enchantment.Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return 1;
            case UNCOMMON:
                return 2;
            case RARE:
                return 4;
            case VERY_RARE:
                return 8;
        }
        throw new IllegalArgumentException();
    }

    public static List<Enchantment> getConflicts(Enchantment enchantment) {
        return CONFLICT_CACHE.computeIfAbsent(enchantment, EnchantUtil::getConflictsInternal);
    }

    private static List<Enchantment> getConflictsInternal(Enchantment enchantment) {
        ImmutableList.Builder<Enchantment> builder = ImmutableList.builder();
        for (Enchantment temp : Enchantment.REGISTRY) {
            if (temp == enchantment) continue;
            if (enchantment.isCompatibleWith(temp)) continue;
            builder.add(temp);
        }
        return builder.build();
    }

    public static Enchantment getFirstEnchantment(Map<Enchantment, Integer> enchantments) {
        assert !enchantments.isEmpty();
        return enchantments.entrySet().stream().findFirst().get().getKey();
    }
}
