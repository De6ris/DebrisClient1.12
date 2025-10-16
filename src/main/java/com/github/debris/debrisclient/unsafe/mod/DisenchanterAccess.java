package com.github.debris.debrisclient.unsafe.mod;

import de.impelon.disenchanter.gui.GuiDisenchantment;
import de.impelon.disenchanter.inventory.ContainerDisenchantmentManual;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

public class DisenchanterAccess {
    public static boolean isDisenchantmentGUI(GuiScreen screen) {
        return screen instanceof GuiDisenchantment;
    }

    public static boolean isDisenchantmentContainer(Container container) {
        return container instanceof ContainerDisenchantmentManual;
    }
}
