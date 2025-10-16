package com.github.debris.debrisclient.event.forge;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.AnvilUtil;
import com.github.debris.debrisclient.util.EnchantUtil;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class TooltipListener {
    private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!DCConfig.ExtraTooltip.getBooleanValue()) return;
        if (!GameSettings.isKeyDown(SNEAK)) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.isEmpty()) return;

        Item item = itemStack.getItem();
        List<String> toolTip = event.getToolTip();

        if (item == Items.ENCHANTED_BOOK) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
            toolTip.add("附魔成本: " + EnchantUtil.calculateEnchantmentCost(enchantments) + "级");
            addConflictEnchantments(enchantments, toolTip);
        }

        addAnvilPunishTooltip(toolTip, itemStack);
    }

    private static void addAnvilPunishTooltip(List<String> toolTip, ItemStack itemStack) {
        int punishment = AnvilUtil.getPunishment(itemStack);
        if (punishment > 0) {
            int operations = AnvilUtil.asOperations(punishment);
            toolTip.add(String.format("铁砧惩罚: %d级(%d次操作)", punishment, operations));
        }
    }

    private static void addConflictEnchantments(Map<Enchantment, Integer> enchantments, List<String> toolTip) {
        if (enchantments.size() != 1) return;

        List<Enchantment> conflict = EnchantUtil.getConflicts(EnchantUtil.getFirstEnchantment(enchantments));
        if (conflict.isEmpty()) return;

        toolTip.add("冲突附魔: " + StringUtil.translateEnchantments(conflict));
    }
}
