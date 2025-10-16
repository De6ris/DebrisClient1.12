package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.DisenchanterAccess;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class DisenchanterTweak {
    public static boolean run(Minecraft client) {
        if (!ModReference.hasMod(ModReference.DISENCHANTER)) return false;
        GuiScreen screen = client.currentScreen;
        if (!DisenchanterAccess.isDisenchantmentGUI(screen)) return false;

        InventoryTweaks.makeSureNotHoldingItem(EnumSection.InventoryWhole.get());
        tryAddBook();
        tryDisenchant();
        tryRemoveResidue();

        return true;
    }

    private static void tryAddBook() {
        Slot first = EnumSection.DisenchanterFirst.get().getFirstSlot();
        Slot second = EnumSection.DisenchanterSecond.get().getFirstSlot();
        ItemStack stack = first.getStack();
        if (InventoryUtil.hasItem(stack) && readyToDisenchant(stack) && InventoryUtil.isEmpty(second)) {
            EnumSection.InventoryWhole.get().findItem(Items.BOOK)
                    .ifPresent(slot -> InventoryUtil.moveOneItem(second, slot));
        }
    }

    private static boolean readyToDisenchant(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) return true;
        if (stack.isItemEnchanted()) return true;
        return false;
    }

    private static void tryDisenchant() {
        InventoryUtil.quickMoveIfPossible(EnumSection.DisenchanterThird.get().getFirstSlot());
    }

    private static void tryRemoveResidue() {
        Slot first = EnumSection.DisenchanterFirst.get().getFirstSlot();
        ItemStack stack = first.getStack();
        if (InventoryUtil.hasItem(stack) && !readyToDisenchant(stack)) {
            InventoryUtil.quickMove(first);
        }
    }
}
