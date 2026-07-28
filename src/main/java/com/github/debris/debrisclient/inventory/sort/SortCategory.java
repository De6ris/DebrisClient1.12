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
    TRANSLATION_KEY(Comparator.comparing(ItemStack::getTranslationKey)),
    TRANSLATION_RESULT(Comparator.comparing(StringUtil::translateItemStack)),
    PINYIN(SortCategory::compareByPinyin);

    private final Comparator<ItemStack> order;// this assumes they are distinct

    SortCategory(Comparator<ItemStack> order) {
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
        Comparator<ItemStack> itemOrderByConfig = getCategory().order;

        return itemOrderByConfig
                .thenComparing(ItemStack::getDisplayName)
                .thenComparing(ItemStackComparators.META)
                .thenComparing(ItemStackComparators.SHULKER_BOX)
                .thenComparing(ItemStackComparators.CHARM_CRATE)
                .thenComparing(ItemStackComparators.ENCHANTMENT.reversed())// more enchantments come first
                .thenComparing(ItemStackComparators.WINE_QUALITY.reversed())// better quality come first
                // 1.12: damage is meta
//                .thenComparing(ItemStackComparators.DAMAGE)// here damage is lost durability, so lossless items come first
                .thenComparing(ItemStackComparators.COUNT.reversed())// large stacks come first
                ;
    }

    private static int compareByCreativeInventory(ItemStack c1, ItemStack c2) {
        @Nullable CreativeTabs tab1 = c1.getItem().getCreativeTab();
        @Nullable CreativeTabs tab2 = c2.getItem().getCreativeTab();
        if (tab1 == null && tab2 == null) return 0;
        if (tab1 == null) return 1;
        if (tab2 == null) return -1;
        int compare = Integer.compare(tab1.getIndex(), tab2.getIndex());
        if (compare != 0) return compare;
        int id1 = Item.REGISTRY.getIDForObject(c1.getItem());
        int id2 = Item.REGISTRY.getIDForObject(c2.getItem());
        return Integer.compare(id1, id2);
    }

    private static int compareByPinyin(ItemStack c1, ItemStack c2) {
        if (PinYinSupport.available()) {
            String translate1 = StringUtil.translateItemStack(c1);
            String translate2 = StringUtil.translateItemStack(c2);
            return PinYinSupport.compareString(translate1, translate2, () -> TRANSLATION_KEY.order.compare(c1, c2));
        }

        return TRANSLATION_KEY.order.compare(c1, c2);
    }
}
