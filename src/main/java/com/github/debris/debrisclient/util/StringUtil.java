package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.config.api.IConfigEnum;
import com.github.debris.debrisclient.config.options.ConfigEnumEntryWrapper;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.IConfigBase;
import fi.dy.masa.malilib.config.options.IConfigOptionList;
import fi.dy.masa.malilib.gui.GuiBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class StringUtil {
    private static final Logger LOGGER = LogManager.getLogger(StringUtil.class);

    public static List<String> commentSearch(IConfigBase config) {
        if (config == null) {
            return Collections.emptyList();
        }
        List<String> strings = new ArrayList<>();
        String name = config.getName().toLowerCase();
        strings.add(name);
        String prettyName = config.getPrettyName().toLowerCase();
        if (!prettyName.equals(name)) strings.add(prettyName);
        String comment = config.getComment();
        if (comment != null) strings.add(comment);
        return strings;
    }

    public static String translateItem(Item item) {
        return translate(item.getTranslationKey() + ".name");
    }

    public static String translate(String key) {
        return I18n.format(key);
    }

    public static String translateFallback(String key, String fallback) {
        String format = I18n.format(key);
        if (format.equals(key)) {
            LOGGER.info("missing translation key {}", key);
            return fallback;
        }
        return format;
    }

    public static List<String> createOptionListTooltip(IConfigOptionList config) {
        IConfigOptionListEntry defaultEntry = config.getDefaultOptionListValue();
        IConfigOptionListEntry currentEntry = config.getOptionListValue();
        List<IConfigOptionListEntry> entries;

        if (config instanceof IConfigEnum) {
            IConfigEnum<?> configEnum = (IConfigEnum<?>) config;
            List<? extends ConfigEnumEntryWrapper<?>> wrappers = configEnum.getWrappers();
            entries = GenericsUtil.cast(wrappers);
        } else {
            entries = collectAllEntries(defaultEntry);
        }

        return mapTooltip(entries, defaultEntry, currentEntry);
    }

    private static List<IConfigOptionListEntry> collectAllEntries(IConfigOptionListEntry defaultEntry) {
        List<IConfigOptionListEntry> list = new ArrayList<>();
        list.add(defaultEntry);
        IConfigOptionListEntry next = defaultEntry.cycle(true);
        while (next != defaultEntry) {
            list.add(next);
            next = next.cycle(true);
        }
        return list;
    }

    private static List<String> mapTooltip(List<IConfigOptionListEntry> entries, IConfigOptionListEntry defaultEntry, IConfigOptionListEntry currentEntry) {
        List<String> hover = new ArrayList<>();
        hover.add(StringUtil.translate("config.enum.tooltip.available") + ":");
        for (IConfigOptionListEntry entry : entries) {
            if (entry == defaultEntry) {
                hover.add(GuiBase.TXT_AQUA + entry.getDisplayName() + "<--" + StringUtil.translate("config.enum.tooltip.default"));
            } else if (entry == currentEntry) {
                hover.add(GuiBase.TXT_GREEN + entry.getDisplayName() + "<--" + StringUtil.translate("config.enum.tooltip.current"));
            } else {
                hover.add(entry.getDisplayName());
            }
        }
        return hover;
    }

    public static String translateEnchantment(Enchantment enchantment) {
        return translate(enchantment.getName());
    }

    public static String translateEnchantments(Collection<Enchantment> enchantments) {
        List<String> strings = enchantments.stream().map(StringUtil::translateEnchantment).collect(Collectors.toList());
        return StringUtils.join(strings, ", ");
    }

    public static String translateEnchantmentDatas(Collection<EnchantmentData> enchantmentDatas) {
        List<String> strings = enchantmentDatas.stream().map(StringUtil::translateEnchantmentData).collect(Collectors.toList());
        return StringUtils.join(strings, ", ");
    }

    public static String translateEnchantmentData(EnchantmentData enchantmentData) {
        return enchantmentData.enchantment.getTranslatedName(enchantmentData.enchantmentLevel);
    }

    public static String translateEnchantmentMap(Map<Enchantment, Integer> map) {
        List<String> strings = map.entrySet().stream()
                .map(x -> translateEnchantmentNoSpace(x.getKey(), x.getValue()))
                .map(x -> x + TextFormatting.RESET)
                .collect(Collectors.toList());
        return "[" + StringUtils.join(strings, ", ") + "]";
    }

    public static String translateEnchantmentNoSpace(Enchantment enchantment, int level) {
        String name = translate(enchantment.getName());
        if (enchantment.isCurse()) {
            name = TextFormatting.RED + name;
        }
        return level == 1 && enchantment.getMaxLevel() == 1 ? name : name + translate("enchantment.level." + level);
    }
}
