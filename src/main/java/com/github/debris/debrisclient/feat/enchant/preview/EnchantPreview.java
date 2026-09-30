package com.github.debris.debrisclient.feat.enchant.preview;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.localization.EnchantPreviewText;
import com.github.debris.debrisclient.util.ComponentUtil;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantPreview {
    private static final XpSeedCracker CRACKER = new XpSeedCracker();

    private static final String[] INFO = new String[]{"", "", ""};

    public static boolean isActive() {
        return DCConfig.EnchantPreview.getBooleanValue();
    }

    public static boolean isCracked() {
        return isActive() && CRACKER.isCracked();
    }

    public static void render(GuiContainer guiContainer) {
        if (!isActive()) return;

        CrackingState state = CRACKER.getState();
        if (state == CrackingState.CRACKED) {
            int x = (guiContainer.width - guiContainer.getXSize()) / 2;
            int y = (guiContainer.height - guiContainer.getYSize()) / 2;// left above corner
            FontRenderer fontRenderer = guiContainer.mc.fontRenderer;
            int color = ComponentUtil.getColorInteger(fontRenderer, TextFormatting.AQUA);
            int wrapWidth = 86 - fontRenderer.getStringWidth("30");// level text
            for (int i = 0; i < 3; i++) {
                String s = INFO[i];
                if (s.isEmpty()) continue;
                fontRenderer.drawSplitString(
                        s,
                        x + 80,
                        y + 16 + 19 * i,
                        wrapWidth,
                        color
                );
            }
        } else {
            @Nullable String text = null;
            if (state == CrackingState.CRACKING) {
                text = EnchantPreviewText.CRACKING.translateS(CRACKER.getProgress() * 100);
            }
            if (state == CrackingState.FAIL) {
                text = EnchantPreviewText.FAIL.translateS();
            }
            if (state == CrackingState.MULTICHOICE) {
                text = EnchantPreviewText.MULTICHOICE.translateS(CRACKER.getCandidateSize());
            }
            if (text != null) {
                int x = (guiContainer.width - guiContainer.getXSize()) / 2;
                int y = (guiContainer.height - guiContainer.getYSize()) / 2;// left above corner
                FontRenderer fontRenderer = guiContainer.mc.fontRenderer;
                int color = ComponentUtil.getColorInteger(fontRenderer, TextFormatting.AQUA);
                fontRenderer.drawString(
                        text,
                        x + 40,
                        y + 72,
                        color
                );
            }
        }
    }

    public static void onSeedUpdate(ContainerEnchantment container, World world) {
        Arrays.fill(INFO, "");

        if (!isActive()) return;

        ItemStack stack = container.tableInventory.getStackInSlot(0);
        CRACKER.update(container, world, stack);

        if (isCracked()) {
            for (int i = 0; i < 3; i++) {
                int level = container.enchantLevels[i];
                if (level <= 0) continue;
                List<EnchantmentData> list = CRACKER.getEnchantmentList(stack, i, level);
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
}
