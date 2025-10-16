package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Slot;

import java.util.Optional;

public class BetterQuickMoving {
    public static void run(int slotId, int mouseButton) {
        if (slotId == -1) return;// cases -1 unknown
        if (mouseButton != 0) return;
        if (!DCConfig.BetterQuickMoving.getBooleanValue()) return;

        GuiContainer guiContainer = InventoryUtil.getGuiContainer();
        Slot slot = InventoryUtil.getSlots().get(slotId);

        if (!InventoryUtil.hasItem(slot)) return;

        if (guiContainer instanceof GuiCrafting) {
            if (EnumSection.InventoryWhole.get().hasSlot(slot)) {
                ContainerSection section = EnumSection.CraftMatrix.get();
                Optional<Slot> emptySlot = section.getEmptySlot();
                if (emptySlot.isPresent()) {
                    Slot empty = emptySlot.get();
                    InventoryUtil.moveToEmpty(slot, empty);
                    section.mergeSlotToPrevious(section.getLocalIndex(empty), empty);
                }
            }
        }

    }
}
