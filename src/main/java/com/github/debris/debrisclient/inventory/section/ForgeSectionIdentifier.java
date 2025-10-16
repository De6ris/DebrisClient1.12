package com.github.debris.debrisclient.inventory.section;

import baubles.api.cap.IBaublesItemHandler;
import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ForgeSectionIdentifier extends SectionIdentifier {

    public ForgeSectionIdentifier(BiConsumer<EnumSection, ContainerSection> sectionReceiver, Consumer<ContainerSection> unidentifiedHandler) {
        super(sectionReceiver, unidentifiedHandler);
    }

    public void identifyForge(@Nullable GuiContainer guiContainer, Container container, IItemHandler itemHandler, List<SlotItemHandler> slotListForge) {
        List<Slot> slotList = slotListForge.stream().map(x -> ((Slot) x)).collect(Collectors.toList());
        try {
            this.identifyForgeInternal(guiContainer, container, itemHandler, slotList);
        } catch (Exception e) {
            LOGGER.warn("Error identifying container {}", container);
            LOGGER.warn("Stack trace", e);
            this.handleUnidentified(createSection(slotList));
        }
    }

    public void identifyForgeInternal(@Nullable GuiContainer guiContainer, Container container, IItemHandler itemHandler, List<Slot> slotList) {
        ContainerSection theWholeSection = createSection(slotList);

        if (itemHandler instanceof PlayerMainInvWrapper) {
            if (ModReference.hasMod(ModReference.RETRO_SOPHISTICATED_BACKPACKS) && RSBackpacksAccess.isBackpackContainer(container)) {
                boolean occursInHotBar = false;
                for (int i = 0; i < 9; i++) {
                    if (slotList.get(i).getSlotIndex() == i) continue;
                    occursInHotBar = true;
                }
                int border = occursInHotBar ? 8 : 9;
                putSection(EnumSection.InventoryHotBar, slotList.subList(0, border));
                putSection(EnumSection.InventoryStorage, slotList.subList(border, 35));
            } else {
                putSection(EnumSection.InventoryHotBar, slotList.subList(0, 9));
                putSection(EnumSection.InventoryStorage, slotList.subList(9, 36));
            }
            return;
        }

        if (itemHandler instanceof PlayerInvWrapper) {
            if (ModReference.hasMod(ModReference.RETRO_SOPHISTICATED_BACKPACKS) && RSBackpacksAccess.isBackpackContainer(container)) {
                putSection(EnumSection.RetroSophisticatedBackpacksLocked, slotList);
            } else {
                putSection(EnumSection.InventoryHotBar, slotList.subList(0, 9));
                putSection(EnumSection.InventoryStorage, slotList.subList(9, 36));
                putSection(EnumSection.Armor, slotList.subList(36, 40));
                putSection(EnumSection.OffHand, slotList.subList(40, 41));
            }
            return;
        }

        if (ModReference.hasMod(ModReference.RUSTIC) && RusticAccess.isBrewingBarrelContainer(container)) {
            putSection(EnumSection.BrewingBarrelInputUp, slotList.subList(0, 1));
            putSection(EnumSection.BrewingBarrelOutputUp, slotList.subList(1, 2));
            putSection(EnumSection.BrewingBarrelAuxiliaryUp, slotList.subList(2, 3));
            putSection(EnumSection.BrewingBarrelInputDown, slotList.subList(3, 4));
            putSection(EnumSection.BrewingBarrelOutputDown, slotList.subList(4, 5));
            putSection(EnumSection.BrewingBarrelAuxiliaryDown, slotList.subList(5, 6));
            return;
        }

        if (ModReference.hasMod(ModReference.DISENCHANTER) && DisenchanterAccess.isDisenchantmentContainer(container)) {
            putSection(EnumSection.DisenchanterFirst, slotList.subList(0, 1));
            putSection(EnumSection.DisenchanterSecond, slotList.subList(1, 2));
            putSection(EnumSection.DisenchanterThird, slotList.subList(2, 3));
            return;
        }

        if (ModReference.hasMod(ModReference.BAUBLES) && BaublesAccess.isBaubleContainer(container)) {
            putSection(EnumSection.Baubles, theWholeSection);
            return;
        }

        if (ModReference.hasMod(ModReference.BOUNTIFULBAUBLES) && BountifulBaublesAccess.isReforgingContainer(container)) {
            if (itemHandler.getSlots() == 1) {
                putSection(EnumSection.BountifulBaublesReforging, theWholeSection);
            } else if (itemHandler instanceof IBaublesItemHandler) {
                putSection(EnumSection.Baubles, theWholeSection);
            }
            return;
        }

        // TODO sophi backpacks

        this.handleUnidentified(theWholeSection);
    }
}
