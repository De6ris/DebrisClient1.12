package com.github.debris.debrisclient.event;

import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import net.minecraft.client.gui.GuiScreen;

public class Hooks {
    public static void onMouseClicked(GuiScreen screen, int mouseX, int mouseY, int mouseButton) {
        AutoReforging.onMouseClicked(screen, mouseX, mouseY, mouseButton);
    }
}
