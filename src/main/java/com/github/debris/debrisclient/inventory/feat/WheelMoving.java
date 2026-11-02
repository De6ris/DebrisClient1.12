package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.InventoryConfig;
import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.unsafe.modularui.ModularSlotHelper;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.stream.Stream;

public class WheelMoving {
    public static void scrollSlot(Slot slot, boolean increase) {
        if (InventoryUtil.isEmpty(slot)) return;
        Optional<ContainerSection> opposite = InventoryTweaks.getMoveDestination(slot);
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

    public static boolean handleScroll(boolean up, int amount) {
        WheelMovingMode mode = InventoryConfig.WheelMoving.getEnumValue();
        if (mode == WheelMovingMode.NONE) return false;
        boolean increase = up;
        if (mode == WheelMovingMode.INVERT) increase = !increase;
        Optional<Slot> optional = InventoryUtil.getHoveredSlot();
        if (optional.isPresent()) {
            scrollSlot(optional.get(), increase);
            return true;
        }
        return false;
    }
}
