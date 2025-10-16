package com.github.debris.debrisclient.inventory.sort;

import com.github.debris.debrisclient.util.ItemUtil;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ItemStackComparators {
    public static final Comparator<ItemStack> META = Comparator.comparing(ItemStack::getMetadata);
    public static final Comparator<ItemStack> COUNT = Comparator.comparing(ItemStack::getCount);
    public static final Comparator<ItemStack> SHULKER_BOX = ItemStackComparators::compareShulkerBox;
    public static final Comparator<ItemStack> CHARM_CRATE = ItemStackComparators::compareCharmCrate;
    public static final Comparator<ItemStack> ENCHANTMENT = Comparator.comparing(x -> x.getEnchantmentTagList().tagCount());
    public static final Comparator<ItemStack> DAMAGE = Comparator.comparing(ItemStack::getItemDamage);

    public static int compareShulkerBox(ItemStack c1, ItemStack c2) {
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

    public static int compareCharmCrate(ItemStack c1, ItemStack c2) {
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
        Predicate<ItemStack> predicate = ItemUtil.predicateIDMeta(first);
        return list.stream().allMatch(predicate);
    }
}
