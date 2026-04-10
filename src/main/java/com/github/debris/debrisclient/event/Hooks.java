package com.github.debris.debrisclient.event;

import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Hooks {
    public static void onMouseClicked(Minecraft client, GuiScreen screen, int mouseX, int mouseY, int mouseButton) {
        AutoReforging.onMouseClicked(client, screen, mouseX, mouseY, mouseButton);
    }
}
