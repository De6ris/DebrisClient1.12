package com.github.debris.debrisclient.util;

import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public class ForgeSlotHelper {
    public static boolean isForgeSlot(Slot slot) {
        return slot instanceof SlotItemHandler;
    }

    public static IItemHandler getItemHandler(Slot slot) {
        return ((SlotItemHandler) slot).getItemHandler();
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean isPlayerInventory(IItemHandler itemHandler) {
        if (itemHandler instanceof PlayerInvWrapper) return true;
        if (itemHandler instanceof PlayerMainInvWrapper) return true;
        if (itemHandler instanceof PlayerArmorInvWrapper) return true;
        if (itemHandler instanceof PlayerOffhandInvWrapper) return true;
        return false;
    }
}
