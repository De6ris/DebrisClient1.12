package com.github.debris.debrisclient.inventory.sort;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.RusticAccess;
import com.github.debris.debrisclient.util.EnchantUtil;
import com.github.debris.debrisclient.util.ItemUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemStackComparators {
    public static final Comparator<ItemStack> META = Comparator.comparing(ItemStack::getMetadata);
    public static final Comparator<ItemStack> COUNT = Comparator.comparing(ItemStack::getCount);
    public static final Comparator<ItemStack> SHULKER_BOX = ItemStackComparators::compareShulkerBox;
    public static final Comparator<ItemStack> CHARM_CRATE = ItemStackComparators::compareCharmCrate;
    public static final Comparator<ItemStack> ENCHANTMENT = ItemStackComparators::compareEnchantment;
    public static final Comparator<ItemStack> DAMAGE = Comparator.comparing(ItemStack::getItemDamage);
    public static final Comparator<ItemStack> WINE_QUALITY = ItemStackComparators::compareWineQuality;

    private static int compareShulkerBox(ItemStack c1, ItemStack c2) {
        if (ItemUtil.isShulkerBox(c1) && ItemUtil.isShulkerBox(c2)) {
            List<ItemStack> list1 = ItemUtil.filterNonEmpty(ItemUtil.readShulkerBox(c1));
            List<ItemStack> list2 = ItemUtil.filterNonEmpty(ItemUtil.readShulkerBox(c2));
            int compare = Integer.compare(list1.size(), list2.size());// comparing size
            if (compare != 0) return compare;
            if (list1.isEmpty()) return 0;
            if (isPureItemStackList(list1) && isPureItemStackList(list2)) {
                ItemStack itemStack1 = list1.get(0);
                ItemStack itemStack2 = list2.get(0);
                return SortCategory.getItemStackSorter().compare(itemStack1, itemStack2);
            }
        }
        return 0;
    }

    private static int compareCharmCrate(ItemStack c1, ItemStack c2) {// TODO not working
        if (ItemUtil.isCharmCrate(c1) && ItemUtil.isCharmCrate(c2)) {
            List<ItemStack> list1 = ItemUtil.filterNonEmpty(ItemUtil.readCharmCrate(c1));
            List<ItemStack> list2 = ItemUtil.filterNonEmpty(ItemUtil.readCharmCrate(c2));
            int compare = Integer.compare(list1.size(), list2.size());// comparing size
            if (compare != 0) return compare;
            if (list1.isEmpty()) return 0;
            if (isPureItemStackList(list1) && isPureItemStackList(list2)) {
                ItemStack itemStack1 = list1.get(0);
                ItemStack itemStack2 = list2.get(0);
                return SortCategory.getItemStackSorter().compare(itemStack1, itemStack2);
            }
        }
        return 0;
    }

    // Assuming no empty stack and non-empty list
    private static boolean isPureItemStackList(List<ItemStack> list) {
        ItemStack first = list.get(0);
        Predicate<ItemStack> predicate = ItemUtil.predicateIDNBT(first);
        return list.stream().allMatch(predicate);
    }

    private static int compareEnchantment(ItemStack c1, ItemStack c2) {
        Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(c1);
        Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(c2);
        int compare = Integer.compare(map1.size(), map2.size());
        if (compare != 0) return compare;
        if (map1.size() != 1) return 0;
        EnchantmentData data1 = EnchantUtil.getFirstEnchantmentData(map1);
        EnchantmentData data2 = EnchantUtil.getFirstEnchantmentData(map2);
        compare = Integer.compare(Enchantment.getEnchantmentID(data1.enchantment), Enchantment.getEnchantmentID(data2.enchantment));
        if (compare != 0) return compare;
        compare = Integer.compare(data1.enchantmentLevel, data2.enchantmentLevel);
        return compare;
    }

    private static int compareWineQuality(ItemStack c1, ItemStack c2) {
        if (ModReference.hasMod(ModReference.RUSTIC) && RusticAccess.isWine(c1) && RusticAccess.isWine(c2)) {
            float quality1 = RusticAccess.getWineQuality(c1);
            float quality2 = RusticAccess.getWineQuality(c2);
            return Float.compare(quality1, quality2);
        }
        return 0;
    }

}
