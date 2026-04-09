package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.gui.button.GuiBetterButton;
import com.github.debris.debrisclient.unsafe.mod.XaeroMiniMapAccess;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FastWaypoint {
    /**
     * 1616901092
     */
    public static final int BUTTON_ID = "create_waypoint".hashCode();

    public static GuiButton createButton() {
        return new GuiBetterButton(BUTTON_ID,
                0,
                0,
                50,
                16,
                "创建路径点");
    }

    public static void createWayPoint(int dimensionId, BlockPos pos, String name) {
        if (ModReference.hasMod(ModReference.XAERO_MINI_MAP)) {
            XaeroMiniMapAccess.createWayPoint(dimensionId, pos, name);
        }
    }

    @Nullable
    public static String ofMerchantRecipes(@Nullable MerchantRecipeList recipes) {
        if (recipes == null) return null;
        List<EnchantmentData> list = recipes.stream()
                .map(MerchantRecipe::getItemToSell)
                .filter(x -> x.getItem() == Items.ENCHANTED_BOOK)
                .map(EnchantmentHelper::getEnchantments)
                .flatMap(x -> x.entrySet().stream())
                .map(x -> new EnchantmentData(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
        if (list.isEmpty()) return null;
        return StringUtil.translateEnchantmentData(list);
    }
}
