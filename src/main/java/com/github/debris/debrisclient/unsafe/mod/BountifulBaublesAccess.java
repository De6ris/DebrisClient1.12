package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.mixins.client.gui.IGuiScreenMixin;
import cursedflames.bountifulbaubles.block.ContainerReforger;
import cursedflames.bountifulbaubles.block.GuiReforger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class BountifulBaublesAccess {
    public static boolean isReforgingGUI(GuiScreen screen) {
        return screen instanceof GuiReforger;
    }

    public static boolean isReforgingContainer(Container container) {
        return container instanceof ContainerReforger;
    }

    public static boolean canReforge(GuiScreen screen) {
        if (!isReforgingGUI(screen)) return false;
        ItemStack bauble = EnumSection.BountifulBaublesReforging.get().getFirstSlot().getStack();
        if (bauble.isEmpty()) return false;
        return ((IGuiScreenMixin) screen).getButtonList().get(0).enabled;
    }
}
