package com.github.debris.debrisclient.inventory.sort;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.PinYinSupport;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Comparator;

public enum SortCategory {
    CREATIVE_INVENTORY(SortCategory::compareByCreativeInventory),
    TRANSLATION_KEY(Comparator.comparing(Item::getTranslationKey)),
    TRANSLATION_RESULT(Comparator.comparing(StringUtil::translateItem)),
    PINYIN(SortCategory::compareByPinyin);

    private final Comparator<Item> order;// this assumes they are distinct

    SortCategory(Comparator<Item> order) {
        this.order = order;
    }

    public static SortCategory getCategory() {
        return DCConfig.ItemSortingOrder.getEnumValue();
    }

    /*
     * When using, if result > 0, I will swap.
     * Thus, if you want a comes before b, you should let a be smaller than b in the comparator.
     * */
    public static Comparator<ItemStack> getItemStackSorter() {
        Comparator<Item> itemOrderByConfig = getCategory().order;
        Comparator<ItemStack> itemTypeComparator = (c1, c2) -> {
            if (ItemStack.areItemsEqual(c1, c2)) {
                return 0;
            }
            return itemOrderByConfig.compare(c1.getItem(), c2.getItem());
        };

        return itemTypeComparator
                .thenComparing(ItemStackComparators.META)
                .thenComparing(ItemStackComparators.COUNT.reversed())// large stacks come first
                .thenComparing(ItemStackComparators.SHULKER_BOX)
                .thenComparing(ItemStackComparators.CHARM_CRATE)
                .thenComparing(ItemStackComparators.ENCHANTMENT.reversed())// more enchantments come first
                .thenComparing(ItemStackComparators.DAMAGE)// here damage is lost durability, so lossless items come first
                .thenComparing(ItemStack::getDisplayName)
                ;
    }

    private static int compareByCreativeInventory(Item c1, Item c2) {
        @Nullable CreativeTabs tab1 = c1.getCreativeTab();
        @Nullable CreativeTabs tab2 = c2.getCreativeTab();
        if (tab1 == null) return 1;
        if (tab2 == null) return -1;
        return Integer.compare(tab1.getIndex(), tab2.getIndex());
    }

    private static int compareByPinyin(Item c1, Item c2) {
        if (PinYinSupport.available()) {
            String translate1 = StringUtil.translateItem(c1);
            String translate2 = StringUtil.translateItem(c2);
            return PinYinSupport.compareString(translate1, translate2, () -> TRANSLATION_KEY.order.compare(c1, c2));
        }

        return TRANSLATION_KEY.order.compare(c1, c2);
    }
}
