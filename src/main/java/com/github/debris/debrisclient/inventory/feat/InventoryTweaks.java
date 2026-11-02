package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.inventory.section.SectionHandler;
import com.github.debris.debrisclient.util.InventoryUtil;
import com.github.debris.debrisclient.util.ItemUtil;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InventoryTweaks {
    // try to put held item to this section, if fail then drop
    public static void clearCursor(ContainerSection section) {
        boolean success = mergeCursorToSection(section);
        if (success) return;

        InventoryUtil.dropHeldItem();// just drop
    }

    public static void quickMove(Slot slot) {
        Optional<ContainerSection> destination = getMoveDestination(slot);
        destination.ifPresent(section -> quickMove(slot, section));
    }

    public static void quickMove(Slot slot, ContainerSection destination) {
        InventoryUtil.leftClick(slot);
        mergeCursorToSection(destination);
    }

    public static boolean mergeCursorToSection(ContainerSection section) {
        ItemStack heldStack = InventoryUtil.getHeldStack();
        if (InventoryUtil.isEmpty(heldStack)) return true;

        int loop = 0;
        Optional<Slot> mergeSlot = section.absorbsOneScroll(heldStack);
        while (mergeSlot.isPresent() && loop < section.size()) {
            InventoryUtil.leftClick(mergeSlot.get());
            heldStack = InventoryUtil.getHeldStack();
            if (InventoryUtil.isEmpty(heldStack)) {
                return true;// merge success
            } else {
                mergeSlot = section.absorbsOneScroll(heldStack);// try merge to other slot
            }
            loop++;
        }

        if (!InventoryUtil.isHoldingItem()) return true;

        Optional<Slot> emptySlot = section.getEmptySlot();
        if (emptySlot.isPresent()) {
            InventoryUtil.leftClick(emptySlot.get());// put held to empty
            return true;
        } else {
            return false;
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

    public static boolean tryPutAway() {
        Optional<ContainerSection> optional = SectionHandler.getSectionMouseOver();
        if (!optional.isPresent()) return false;
        ContainerSection section = optional.get();

        Optional<ContainerSection> optionalDestination = getMoveDestination(section);
        if (!optionalDestination.isPresent()) return false;
        ContainerSection destination = optionalDestination.get();

        Predicate<ItemStack> stackPredicate = x -> destination.streamNotEmpty()
                .map(Slot::getStack)
                .anyMatch(stack -> ItemUtil.compareIDNBT(stack, x));

        List<Slot> list = section.streamNotEmpty()
                .filter(x -> stackPredicate.test(x.getStack()))
                .collect(Collectors.toList());

        list.forEach(InventoryUtil::quickMove);

        return true;
    }

    public static void tryMoveSimilar() {
        InventoryUtil.getHoveredSlot().ifPresent(slot -> {
            if (InventoryUtil.hasItem(slot)) {
                ItemStack template = InventoryUtil.getStack(slot).copy();
                ContainerSection section = SectionHandler.getSection(slot);
                section = expandSectionIfPossible(section);
                section.predicateRun(ItemUtil.predicateIDNBT(template), InventoryUtil::quickMove);
            }
        });
    }

    public static boolean tryThrow(boolean ctrl) {
        Optional<Slot> optional = InventoryUtil.getHoveredSlot();
        if (optional.isPresent()) {
            Slot slot = optional.get();
            if (slot.getHasStack()) {
                InventoryUtil.drop(slot, ctrl);
                return true;
            }
        }
        return false;
    }

    public static boolean tryThrowSame() {
        Optional<Slot> optional = InventoryUtil.getHoveredSlot();
        optional.ifPresent(slot -> {
            if (InventoryUtil.hasItem(slot)) {
                ItemStack template = InventoryUtil.getStack(slot).copy();
                ContainerSection section = SectionHandler.getSection(slot);
                section = expandSectionIfPossible(section);
                section.predicateRun(ItemUtil.predicateIDNBT(template), InventoryUtil::dropStack);
            }
        });
        return optional.isPresent();
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

    public static Optional<ContainerSection> getMoveDestination(Slot slot) {
        return getMoveDestination(SectionHandler.getSection(slot));
    }

    public static Optional<ContainerSection> getMoveDestination(ContainerSection here) {
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

        ContainerSection section = EnumSection.Container.get();
        if (Collections.disjoint(here.slots(), section.slots())) {
            return Optional.of(section);
        }

        section = EnumSection.InventoryWhole.get();
        if (Collections.disjoint(here.slots(), section.slots())) {
            return Optional.of(section);
        }

        return Optional.empty();
    }
}
