package com.github.debris.debrisclient.feat.lock;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.EnumDyeColor;

public class LockHint {
    public static void renderHint(GuiContainer gui, double[] posX, int[] password) {
        for (int index = 0; index < password.length; index++) {
            // order: in this index, you should pin this
            int order = password[index];
            if (order == -1) continue;
            double x = posX[order];
            gui.drawString(gui.mc.fontRenderer, String.valueOf(index + 1), (int) x, 60, EnumDyeColor.MAGENTA.getColorValue());
        }
    }
}
