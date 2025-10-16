package com.github.debris.debrisclient.unsafe.modularui;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.inventory.Slot;

public class ModularSlotHelper {
    public static boolean isPhantom(Slot slot) {
        if (!(slot instanceof ModularSlot)) return false;
        ModularSlot modularSlot = (ModularSlot) slot;
        return modularSlot.isPhantom();
    }
}
