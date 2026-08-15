package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.localization.EnchantPlanText;
import com.github.debris.debrisclient.util.*;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantPlan {
    @Nullable
    static final Enchantment UPGRADED_POTENTIALS = findUpgrade();
    private static final Logger LOGGER = LogManager.getLogger(EnchantPlan.class);

    public static boolean run(Minecraft client) {
        try {
            return runInternal(client);
        } catch (Exception e) {
            actionBar(EnchantPlanText.ERROR);
            LOGGER.info(e);
            return false;
        }
    }

    private static boolean runInternal(Minecraft client) {
        if (Predicates.notInGame(client)) return false;

        EntityPlayerSP player = client.player;

        ItemStack stack = player.getHeldItemMainhand();

        Map<Enchantment, Integer> existingEnchantments = ImmutableMap.of();

        if (stack.isItemEnchanted()) {
            existingEnchantments = EnchantUtil.getEnchantments(stack);
        } else {
            if (!stack.isItemEnchantable()) {
                actionBar(EnchantPlanText.NOT_ENCHANTABLE);
                return false;
            }
        }

        ContainerSection section = EnumSection.InventoryWhole.get();

        List<Slot> bookSlots = getBookSlots(section);

        boolean upgradePresent = false;
        List<Slot> upgrades = bookSlots.stream().filter(x -> isUpgrade(x.getStack())).collect(Collectors.toList());
        if (!upgrades.isEmpty()) {
            upgradePresent = true;
            bookSlots = new ArrayList<>(bookSlots);
            bookSlots.removeAll(upgrades);
        }

        if (bookSlots.isEmpty()) {
            actionBar(EnchantPlanText.NORMAL_BOOKS_NOT_FOUND);
            return false;
        }

        // those books won't contribute to the stack
        if (EnchantCheck.hasUseless(stack, bookSlots)) return false;

        if (EnchantCheck.hasPunishment(bookSlots)) return false;

        List<ItemStack> books = bookSlots.stream().map(Slot::getStack).collect(Collectors.toList());

        List<Enchantment> effective = collectEffectiveEnchantments(stack, existingEnchantments, books);// handle combined books

        if (EnchantCheck.hasDuplicate(effective, bookSlots)) return false;

        if (EnchantCheck.hasConflict(effective, bookSlots)) return false;

        if (upgradePresent && existingEnchantments.keySet().stream().anyMatch(EnchantPlan::isUpgrade)) {
            upgradePresent = false;
        }

        List<Step> steps = new EnchantRouter(stack, existingEnchantments, books).plan(upgradePresent);
        infoResults(client, steps);

        return true;
    }

    private static List<Slot> getBookSlots(ContainerSection section) {
        return section.predicate(x -> x.getItem() == Items.ENCHANTED_BOOK).collect(Collectors.toList());
    }

    private static List<Enchantment> collectEffectiveEnchantments(ItemStack stack, Map<Enchantment, Integer> existingEnchantments, List<ItemStack> books) {
        return Stream.concat(
                        existingEnchantments.keySet().stream(),
                        books.stream().flatMap(
                                x -> EnchantUtil.getEnchantments(x).keySet().stream()).filter(x -> x.canApply(stack)
                        )
                ).collect(Collectors.toList());
    }

    static void actionBar(EnchantPlanText text) {
        ChatUtil.setActionBar(EnchantPlanText.PREFIX.formatChinese(text.getChinese()));
    }

    static void actionBar(String message) {
        ChatUtil.setActionBar(EnchantPlanText.PREFIX.formatChinese(message));
    }

    @SuppressWarnings("RedundantIfStatement")
    @Nullable
    private static Enchantment findUpgrade() {
        Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation("somanyenchantments", "upgradedpotentials"));
        if (enchantment != null) return enchantment;// sme 1.0.0+
        enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation("somanyenchantments", "upgrade"));
        if (enchantment != null) return enchantment;// sme 1.0.0-
        return null;
    }

    private static boolean isUpgrade(ItemStack book) {
        Enchantment enchantment = EnchantUtil.getFirstEnchantment(EnchantUtil.getEnchantments(book));
        return isUpgrade(enchantment);
    }

    private static boolean isUpgrade(Enchantment enchantment) {
        return UPGRADED_POTENTIALS == enchantment;
    }

    private static void infoResults(Minecraft client, List<Step> steps) {
        ChatUtil.addLocalChat(client, EnchantPlanText.INFO_START.getChinese());
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            List<ItemStack> stepBooks = step.books;
            List<String> strings = stepBooks.stream().map(EnchantUtil::getEnchantments).map(StringUtil::translateEnchantmentMap).collect(Collectors.toList());
            String message = EnchantPlanText.INFO_STEP.formatChinese(
                    i + 1,
                    step.levelCost,
                    StringUtils.join(strings, ", "),
                    MathUtil.is2Power(stepBooks.size()) ? "" : EnchantPlanText.INFO_STEP_PUNISH.getChinese()
            );
            ChatUtil.addLocalChat(client, message);
        }
    }
}
