package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.QuarkAccess;
import com.github.debris.debrisclient.util.InteractionUtil;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Optional;

public class BetterSwapHands {
    public static void run(GuiContainer guiContainer) {
        Optional<Slot> optional = InventoryUtil.getSlotMouseOver();
        if (!optional.isPresent()) return;
        Slot slot = optional.get();

        if (guiContainer instanceof GuiInventory) {
            if (ModReference.hasMod(ModReference.QUARK) && QuarkAccess.isBetterSwapHands()) return;
            Slot offHand = EnumSection.OffHand.get().getFirstSlot();
            InventoryUtil.swapSlots(slot, offHand);
            return;
        }

        // set items client side
        Minecraft client = guiContainer.mc;
        EntityPlayerSP player = client.player;

        ItemStack itemstack = player.getHeldItem(EnumHand.OFF_HAND);
        player.setHeldItem(EnumHand.OFF_HAND, slot.getStack());
        slot.putStack(itemstack);

        // sync to server
        int hotBar = InteractionUtil.getHotBar(client);
        if (slot.getSlotIndex() == hotBar) {
            InteractionUtil.swapHands();
        } else {
            InventoryUtil.swapHotBar(slot, hotBar);
            InteractionUtil.swapHands();
            InventoryUtil.swapHotBar(slot, hotBar);
        }
    }
}
