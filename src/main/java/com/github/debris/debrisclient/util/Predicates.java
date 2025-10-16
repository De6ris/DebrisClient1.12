package com.github.debris.debrisclient.util;

import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;

public class Predicates {
    public static boolean notInGame(Minecraft client) {
        return client.world == null || client.player == null;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean notInGuiContainer(Minecraft client) {
        if (notInGame(client)) return true;
        if (client.player.isSpectator()) return true;
        GuiScreen currentScreen = GuiUtils.getCurrentScreen();
        if (currentScreen instanceof GuiContainer) {// container screen
            if (currentScreen instanceof GuiContainerCreative && ((GuiContainerCreative) currentScreen).getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex()) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static boolean inGameNoGui(Minecraft client) {
        if (notInGame(client)) return false;
        return client.currentScreen == null;
    }

}
