package com.github.debris.debrisclient.feat.enchant.preview;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EnchantPreview {
    private static final Random rand = new Random();

    private static final XpSeedCracker CRACKER = new XpSeedCracker();

    private static final String[] INFO = new String[]{"", "", ""};

    public static boolean isActive() {
        return DCConfig.EnchantPreview.getBooleanValue();
    }

    public static boolean isCracked() {
        return isActive() && CRACKER.isCracked();
    }

    public static void render(GuiContainer guiContainer) {
        if (!isCracked()) return;
        FontRenderer fontRenderer = guiContainer.mc.fontRenderer;
        int x = (guiContainer.width - guiContainer.getXSize()) / 2;
        int y = (guiContainer.height - guiContainer.getYSize()) / 2;// left above corner
        int wrapWidth = 86 - fontRenderer.getStringWidth("30");// level text
        for (int i = 0; i < 3; i++) {
            String s = INFO[i];
            if (s.isEmpty()) continue;
            fontRenderer.drawSplitString(
                    s,
                    x + 80,
                    y + 16 + 19 * i,
                    wrapWidth,
//                    16777215);
                    13061821);
        }
    }

    public static void onSeedUpdate(ContainerEnchantment container) {
        Arrays.fill(INFO, "");

        if (!isActive()) return;

        ItemStack stack = container.tableInventory.getStackInSlot(0);
        CRACKER.update(container.xpSeed, container.enchantLevels, container.enchantClue, container.worldClue);
        CRACKER.crack(stack);

        if (isCracked()) {
            int seed = CRACKER.getSeed();
            for (int i = 0; i < 3; i++) {
                int level = container.enchantLevels[i];
                if (level <= 0) continue;
                List<EnchantmentData> list = getEnchantmentList(stack, i, level, seed);
                if (list.isEmpty()) continue;
                INFO[i] = makeString(list);
            }
        }
    }

    private static String makeString(List<EnchantmentData> list) {
        return StringUtils.join(list.stream().map(EnchantPreview::makeString).collect(Collectors.toList()), ',');
    }

    private static String makeString(EnchantmentData data) {
        String s = StringUtil.translateEnchantmentNoSpace(data.enchantment, data.enchantmentLevel);
        return TextFormatting.AQUA + s;
    }

    private static List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot, int level, int seed) {
        rand.setSeed(seed + enchantSlot);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, stack, level, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(rand.nextInt(list.size()));
        }

        return list;
    }

}
