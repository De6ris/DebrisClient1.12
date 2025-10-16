package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.inventory.section.SectionHandler;
import com.github.debris.debrisclient.util.InventoryUtil;
import com.github.debris.debrisclient.util.ItemUtil;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.util.Optional;

public class InventoryTweaks {
    // try to put held item to this section, if fail then drop
    public static void makeSureNotHoldingItem(ContainerSection section) {
        ItemStack heldStack = InventoryUtil.getHeldStack();
        if (InventoryUtil.isEmpty(heldStack)) return;
        Optional<Slot> mergeSlot = section.absorbsOneScroll(heldStack);
        while (mergeSlot.isPresent()) {
            InventoryUtil.leftClick(mergeSlot.get());
            heldStack = InventoryUtil.getHeldStack();
            if (InventoryUtil.isEmpty(heldStack)) {
                return;// merge success
            } else {
                mergeSlot = section.absorbsOneScroll(heldStack);// try merge to other slot
            }
        }
        if (InventoryUtil.isHoldingItem()) {// if still
            Optional<Slot> emptySlot = section.getEmptySlot();
            if (emptySlot.isPresent()) {
                InventoryUtil.leftClick(emptySlot.get());// put held to empty
            } else {
                InventoryUtil.dropHeldItem();// just drop
            }
        }
    }

    public static boolean trySpreading(boolean rightClick) {
        if (!InventoryUtil.isHoldingItem()) return false;

        Optional<ContainerSection> sectionOptional = SectionHandler.getSectionMouseOver();
        if (!sectionOptional.isPresent()) return false;
        ContainerSection section = sectionOptional.get();

        InventoryUtil.startSpreading(rightClick);
        section.allRun(x -> InventoryUtil.addToSpreading(x, rightClick));
        InventoryUtil.finishSpreading(rightClick);

        return true;
    }

    public static void tryMoveSimilar() {
        InventoryUtil.getSlotMouseOver().ifPresent(slot -> {
            if (InventoryUtil.hasItem(slot)) {
                ItemStack template = InventoryUtil.getStack(slot).copy();
                ContainerSection section = SectionHandler.getSection(slot);
                section = expandSectionIfPossible(section);
                section.predicateRun(ItemUtil.predicateIDMeta(template), InventoryUtil::quickMove);
            }
        });
    }

    public static void tryThrowSimilar() {
        InventoryUtil.getSlotMouseOver().ifPresent(slot -> {
            if (InventoryUtil.hasItem(slot)) {
                ItemStack template = InventoryUtil.getStack(slot).copy();
                ContainerSection section = SectionHandler.getSection(slot);
                section = expandSectionIfPossible(section);
                section.predicateRun(ItemUtil.predicateIDMeta(template), InventoryUtil::dropStack);
            }
        });
    }

    public static boolean tryThrowSection() {
        Optional<ContainerSection> section = SectionHandler.getSectionMouseOver();
        if (section.isPresent()) {
            section.get().notEmptyRun(InventoryUtil::dropStack);
            return true;
        }
        return false;
    }

    private static ContainerSection expandSectionIfPossible(ContainerSection section) {
        if (GuiUtils.getCurrentScreen() instanceof GuiInventory) return section;
        if (section.isOf(EnumSection.InventoryHotBar) || section.isOf(EnumSection.InventoryStorage))
            return EnumSection.InventoryWhole.get();
        return section;
    }

    public static void onRenderTick(GuiContainer guiContainer) {
    }

}
