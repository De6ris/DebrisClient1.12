package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.util.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnvilEnchantPlan {
    static final Enchantment UPGRADED_POTENTIALS = Enchantment.REGISTRY.getObject(
            new ResourceLocation("somanyenchantments", "upgradedpotentials")
    );
    private static final Logger LOGGER = LogManager.getLogger(AnvilEnchantPlan.class);

    public static boolean run(Minecraft client) {
        try {
            return runInternal(client);
        } catch (Exception e) {
            actionBar("出错, 请查看日志");
            LOGGER.info(e);
            return false;
        }
    }

    private static boolean runInternal(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return false;

        EntityPlayerSP player = client.player;

        ItemStack stack = player.getHeldItemMainhand();
        if (!stack.isItemEnchantable()) {
            actionBar("物品不可附魔");
            return false;
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
            actionBar("物品栏无常规附魔书");
            return false;
        }

        List<Slot> noUsable = filterNoUsable(stack, bookSlots);
        if (!noUsable.isEmpty()) {
            noUsable.forEach(InventoryUtil::dropStack);
            actionBar("已丢出无效附魔书");
            return false;
        }

        List<Slot> punished = filterPunished(bookSlots);
        if (!punished.isEmpty()) {
            punished.forEach(InventoryUtil::dropStack);
            actionBar("已丢出有惩罚附魔书, 使用袪魔台重置之");
            return false;
        }

        List<ItemStack> books = bookSlots.stream().map(Slot::getStack).collect(Collectors.toList());

        List<Enchantment> enchantments = collectEffectiveEnchantments(stack, books);// filter those unusable

        List<Enchantment> duplicate = findDuplicate(enchantments);
        if (!duplicate.isEmpty()) {
            actionBar("附魔重复出现: " + StringUtil.translateEnchantments(duplicate));
            return false;
        }

        List<Enchantment> conflicts = simulateConflict(enchantments);
        if (!conflicts.isEmpty()) {
            actionBar("附魔冲突: " + StringUtil.translateEnchantments(conflicts));
            return false;
        }

        if (upgradePresent &&
                EnchantmentHelper.getEnchantments(stack)
                        .keySet().stream()
                        .anyMatch(AnvilEnchantPlan::isUpgrade)
        ) {
            upgradePresent = false;
        }

        List<Step> steps = new EnchantRouter(stack, books).plan(upgradePresent);
        infoResults(client, steps);

        return true;
    }

    private static List<Slot> getBookSlots(ContainerSection section) {
        return section.predicate(x -> x.getItem() == Items.ENCHANTED_BOOK).collect(Collectors.toList());
    }

    private static List<Slot> filterNoUsable(ItemStack stack, List<Slot> bookSlots) {
        return bookSlots.stream().filter(x -> {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(x.getStack());
            return enchantments.keySet().stream().noneMatch(e -> e.canApply(stack));
        }).collect(Collectors.toList());
    }

    private static List<Slot> filterPunished(List<Slot> bookSlots) {
        return bookSlots.stream().filter(x -> AnvilUtil.getPunishment(x.getStack()) > 0).collect(Collectors.toList());
    }

    private static List<Enchantment> collectEffectiveEnchantments(ItemStack stack, List<ItemStack> books) {
        return Stream.concat(
                EnchantmentHelper.getEnchantments(stack).keySet().stream(),
                books.stream()
                        .flatMap(x -> EnchantmentHelper.getEnchantments(x).keySet().stream())
                        .filter(x -> x.canApply(stack))
        ).collect(Collectors.toList());
    }

    private static List<Enchantment> findDuplicate(List<Enchantment> enchantments) {
        List<Enchantment> duplicate = new ArrayList<>();
        for (Enchantment enchantment : enchantments) {
            int frequency = Collections.frequency(enchantments, enchantment);
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

    private static void actionBar(String message) {
        ChatUtil.setActionBar("铁砧附魔规划: " + message);
    }

    private static boolean isUpgrade(ItemStack book) {
        Enchantment enchantment = EnchantUtil.getFirstEnchantment(EnchantmentHelper.getEnchantments(book));
        return isUpgrade(enchantment);
    }

    private static boolean isUpgrade(Enchantment enchantment) {
        return UPGRADED_POTENTIALS == enchantment;
    }

    private static void infoResults(Minecraft client, List<Step> steps) {
        ChatUtil.addLocalChat(client, "铁砧附魔规划: 结果如下, 每步提供了附魔书列表, 以及合并到物品上时消耗的等级(合并书的消耗忽略)");
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            List<ItemStack> stepBooks = step.books;
            List<String> strings = stepBooks.stream()
                    .map(EnchantmentHelper::getEnchantments)
                    .map(StringUtil::translateEnchantmentMap)
                    .collect(Collectors.toList());
            String message = String.format("第%d步, 消耗%d级: %s%s",
                    i + 1,
                    step.levelCost,
                    StringUtils.join(strings, ", "),
                    MathUtil.is2Power(stepBooks.size()) ? "" : ", 有惩罚亏损, 建议增减附魔书后重新规划"
            );
            ChatUtil.addLocalChat(client, message);
        }
    }
}
