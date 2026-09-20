package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.mod.QuarkAccess;
import com.github.debris.debrisclient.util.InteractionUtil;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Optional;

public class BetterSwapHands {
    public static boolean shouldCancel(GuiContainer guiContainer, int keyCode) {
        if (!DCConfig.BetterSwapHandsKey.getBooleanValue()) return false;
        Minecraft client = guiContainer.mc;
        if (client.gameSettings.keyBindSwapHands.getKeyCode() != keyCode) return false;
        return run(client);
    }

    public static boolean run(Minecraft client) {
        Optional<Slot> optional = InventoryUtil.getHoveredSlot();
        if (!optional.isPresent()) return false;
        Slot slot = optional.get();

        EntityPlayerSP player = client.player;


        if (isQuarkActive(slot)) {
            // set items client side, and wait for quark to swap items
            player.setHeldItem(EnumHand.OFF_HAND, slot.getStack());
            return false;
        }

        // sync to server
        int hotBar = InteractionUtil.getHotBar(client);
        if (slot.getSlotIndex() == hotBar) {
            InteractionUtil.swapHands();
        } else {
            InventoryUtil.swapHotBar(slot, hotBar);
            InteractionUtil.swapHands();
            InventoryUtil.swapHotBar(slot, hotBar);
        }

        // set items client side
        ItemStack itemstack = player.getHeldItem(EnumHand.OFF_HAND);
        player.setHeldItem(EnumHand.OFF_HAND, slot.getStack());
        slot.putStack(itemstack);

        return true;
    }

    private static boolean isQuarkActive(Slot slot) {
        return slot.inventory instanceof InventoryPlayer
                && slot.getSlotIndex() < 36
                && ModReference.hasMod(ModReference.QUARK)
                && QuarkAccess.isBetterSwapHands();
    }
}
