package com.github.debris.debrisclient.event.forge;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.localization.TooltipText;
import com.github.debris.debrisclient.util.AnvilUtil;
import com.github.debris.debrisclient.util.EnchantUtil;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TooltipListener {
    // old
    private static final ResourceLocation XP_BOOK = new ResourceLocation("xpbook", "xp_book");
    // new
    private static final ResourceLocation XP_TOME = new ResourceLocation("xpbook", "xp_tome");

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!DCConfig.ExtraTooltip.getBooleanValue()) return;
        if (!GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) return;

        ItemStack itemStack = event.getItemStack();
        if (itemStack.isEmpty()) return;

        Item item = itemStack.getItem();
        @Nullable ResourceLocation id = Item.REGISTRY.getNameForObject(item);

        List<String> toolTip = event.getToolTip();

        if (item == Items.ENCHANTED_BOOK) {
            Map<Enchantment, Integer> enchantments = EnchantUtil.getEnchantments(itemStack);
            toolTip.add(TooltipText.ENCHANTMENT_COST.formatChinese(EnchantUtil.calculateEnchantmentCost(enchantments)));
            addConflictEnchantments(enchantments, toolTip);
        }

        addAnvilPunishTooltip(toolTip, itemStack);

        addXpTomeTooltip(id, toolTip, itemStack);
    }

    private static void addAnvilPunishTooltip(List<String> toolTip, ItemStack itemStack) {
        int punishment = AnvilUtil.getPunishment(itemStack);
        if (punishment > 0) {
            int operations = AnvilUtil.asOperations(punishment);
            toolTip.add(TooltipText.ANVIL_PUNISHMENT.formatChinese(punishment, operations));
        }
    }

    private static void addConflictEnchantments(Map<Enchantment, Integer> enchantments, List<String> toolTip) {
        if (enchantments.size() != 1) return;

        List<Enchantment> conflict = EnchantUtil.getConflicts(EnchantUtil.getFirstEnchantment(enchantments));
        if (conflict.isEmpty()) return;

        toolTip.add(TooltipText.ENCHANTMENT_CONFLICT.formatChinese(StringUtil.translateEnchantments(conflict)));
    }

    @SuppressWarnings("DataFlowIssue")
    private static void addXpTomeTooltip(@Nullable ResourceLocation id, List<String> toolTip, ItemStack itemStack) {
        int level = -1;
        if (XP_BOOK.equals(id)) {
            level = EnchantUtil.getLevelForExperience(1395 - itemStack.getItemDamage());
        }
        if (XP_TOME.equals(id) && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("xp")) {
            level = EnchantUtil.getLevelForExperience(itemStack.getTagCompound().getInteger("xp"));
        }
        if (level != -1) {
            toolTip.add(TooltipText.EQUIVALENT_LEVEL.formatChinese(level));
        }
    }
}
