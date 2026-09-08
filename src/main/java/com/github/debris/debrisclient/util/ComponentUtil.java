package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.localization.GeneralText;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ComponentUtil {
    public static ITextComponent successOrFail(boolean b) {
        return b ? GeneralText.SUCCESS.translate() : GeneralText.FAIL.translate();
    }

    public static int getColorInteger(FontRenderer fontRenderer, TextFormatting formatting) {
        return fontRenderer.getColorCode(formatting.toString().charAt(1));
    }
}
