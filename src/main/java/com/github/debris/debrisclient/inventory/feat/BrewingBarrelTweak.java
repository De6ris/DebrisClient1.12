package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.RusticAccess;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class BrewingBarrelTweak {
    public static boolean run(Minecraft client) {
        if (!ModReference.hasMod(ModReference.RUSTIC)) return false;
        GuiScreen screen = client.currentScreen;
        if (!RusticAccess.isBrewingBarrelGui(screen)) return false;
        if (RusticAccess.isOnBrewingProcess(screen)) return false;

        ContainerSection inventory = EnumSection.InventoryWhole.get();
        InventoryTweaks.makeSureNotHoldingItem(inventory);

        takeAuxiliary(screen, inventory);
        takeWine(screen, inventory);
        putJuice(screen, inventory);

        return true;
    }

    private static void takeAuxiliary(GuiScreen screen, ContainerSection inventory) {
        Slot up = EnumSection.BrewingBarrelAuxiliaryUp.get().getFirstSlot();
        InventoryUtil.quickMoveIfPossible(up);

        Slot down = EnumSection.BrewingBarrelAuxiliaryDown.get().getFirstSlot();
        if (down.getHasStack()) {
            if (down.getStack().getItem() == Items.GLASS_BOTTLE) {
                InventoryUtil.leftClick(down);
                InventoryUtil.leftClick(up);
            } else {
                InventoryUtil.quickMove(down);
            }
            return;
        }

        Optional<Float> optional = getAuxiliaryQuality(screen);
        if (!optional.isPresent()) return;
        if (optional.get() >= 1.0F) return;

        inventory.findItem(Items.GLASS_BOTTLE).ifPresent(slot -> InventoryUtil.moveOneItem(up, slot));
    }

    private static void takeWine(GuiScreen screen, ContainerSection inventory) {
        Slot outputUp = EnumSection.BrewingBarrelOutputUp.get().getFirstSlot();
        Slot outputDown = EnumSection.BrewingBarrelOutputDown.get().getFirstSlot();
        InventoryUtil.quickMoveIfPossible(outputUp);
        InventoryUtil.quickMoveIfPossible(outputDown);

        FluidStack output = RusticAccess.getOutputFluid(screen);
        if (output == null) return;
        if (output.getFluid() == null) return;

        Slot inputDown = EnumSection.BrewingBarrelInputDown.get().getFirstSlot();
        if (inputDown.getStack().getItem() == Items.GLASS_BOTTLE) {
            InventoryUtil.leftClick(inputDown);
            InventoryUtil.leftClick(outputUp);
            return;
        }

        inventory.findItem(Items.GLASS_BOTTLE).ifPresent(slot -> {
            InventoryUtil.leftClick(slot);
            for (int i = 0; i < 8; i++) {
                InventoryUtil.rightClick(outputUp);
            }
            InventoryUtil.leftClick(slot);
        });
    }

    private static void putJuice(GuiScreen screen, ContainerSection inventory) {
        if (!hasMaxAuxiliary(screen)) return;
        if (EnumSection.BrewingBarrelInputUp.get().getFirstSlot().getHasStack()) return;

        inventory.findItem(RusticAccess::isGrapeJuice).ifPresent(InventoryUtil::quickMove);
    }

    private static Optional<Float> getAuxiliaryQuality(GuiScreen screen) {
        FluidStack aux = RusticAccess.getAuxiliaryFluid(screen);
        if (aux == null) return Optional.empty();
        if (aux.getFluid() == null) return Optional.empty();

        if (aux.tag == null) return Optional.empty();
        if (!aux.tag.hasKey("Quality")) return Optional.empty();

        float quality = aux.tag.getFloat("Quality");
        return Optional.of(quality);
    }

    private static boolean hasMaxAuxiliary(GuiScreen screen) {
        Optional<Float> optional = getAuxiliaryQuality(screen);
        return optional.isPresent() && optional.get() >= 1.0F;
    }
}
