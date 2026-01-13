package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.inventory.section.SectionHandler;
import com.github.debris.debrisclient.unsafe.modularui.ModularSlotHelper;
import com.github.debris.debrisclient.util.ForgeSlotHelper;
import com.github.debris.debrisclient.util.InventoryUtil;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.stream.Stream;

public class WheelMoving {
    public static void scrollSlot(Slot slot, boolean increase) {
        if (InventoryUtil.isEmpty(slot)) return;
        Optional<ContainerSection> opposite = getOppositeSection(slot);
        if (!opposite.isPresent()) return;
        ContainerSection there = opposite.get();
        ItemStack stack = InventoryUtil.getStack(slot);
        if (increase) {
            Optional<Slot> optionalProvider = there.providesOneScroll(stack);
            optionalProvider.ifPresent(x -> InventoryUtil.moveOneItem(slot, x));
        } else {
            Optional<Slot> optionalConsumer = there.absorbsOneScroll(stack);
            if (optionalConsumer.isPresent()) {
                InventoryUtil.moveOneItem(optionalConsumer.get(), slot);
            } else {
                Stream<Slot> slotStream = there.slots().stream()
                        .filter(InventoryUtil::isEmpty)
                        .filter(x -> x.isItemValid(stack));
                if (ModReference.hasMod(ModReference.MODULARUI)) {
                    slotStream = slotStream.filter(x -> !ModularSlotHelper.isPhantom(x));
                }
                slotStream.findFirst()
                        .ifPresent(x -> InventoryUtil.moveOneItem(x, slot));
            }
        }
    }

    private static Optional<ContainerSection> getOppositeSection(Slot slot) {
        ContainerSection here = SectionHandler.getSection(slot);
        ContainerSection there;

        GuiScreen screen = GuiUtils.getCurrentScreen();

        if (screen instanceof GuiInventory) {
            if (here.isOf(EnumSection.InventoryStorage)) {
                there = EnumSection.InventoryHotBar.get();
            } else if (here.isOf(EnumSection.InventoryHotBar)) {
                there = EnumSection.InventoryStorage.get();
            } else {
                return Optional.empty();
            }
            return Optional.of(there);
        }

        boolean isPlayerInventory = false;

        if (ForgeSlotHelper.isForgeSlot(slot)) {
            if (ForgeSlotHelper.isPlayerInventory(ForgeSlotHelper.getItemHandler(slot))) {
                isPlayerInventory = true;
            }
        } else {
            if (InventoryUtil.isPlayerInventory(slot.inventory)) {
                isPlayerInventory = true;
            }
        }

        if (isPlayerInventory) {
            return Optional.of(EnumSection.Container.get());
        } else {
            return Optional.of(EnumSection.InventoryWhole.get());
        }
    }

    public static boolean handleScroll(boolean up, int amount) {
        WheelMovingMode mode = DCConfig.WheelMoving.getEnumValue();
        if (mode == WheelMovingMode.NONE) return false;
        boolean increase = up;
        if (mode == WheelMovingMode.INVERT) increase = !increase;
        Optional<Slot> optional = InventoryUtil.getSlotMouseOver();
        if (optional.isPresent()) {
            scrollSlot(optional.get(), increase);
            return true;
        }
        return false;
    }
}
