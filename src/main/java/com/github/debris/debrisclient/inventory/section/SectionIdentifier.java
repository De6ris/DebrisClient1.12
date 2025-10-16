package com.github.debris.debrisclient.inventory.section;


import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.QualityToolsAccess;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.inventory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SectionIdentifier {
    protected static final Logger LOGGER = LogManager.getLogger(SectionIdentifier.class);

    private final BiConsumer<EnumSection, ContainerSection> sectionReceiver;
    private final Consumer<ContainerSection> unidentifiedHandler;

    public SectionIdentifier(BiConsumer<EnumSection, ContainerSection> sectionReceiver, Consumer<ContainerSection> unidentifiedHandler) {
        this.sectionReceiver = sectionReceiver;
        this.unidentifiedHandler = unidentifiedHandler;
    }

    public void identify(@Nullable GuiContainer guiContainer, Container container, IInventory iInventory, List<Slot> slotList) {
        try {
            this.identifyInternal(guiContainer, container, iInventory, slotList);
        } catch (Exception e) {
            LOGGER.warn("Error identifying container {}", container);
            LOGGER.warn("Stack trace", e);
            this.handleUnidentified(createSection(slotList));
        }
    }

    private void identifyInternal(@Nullable GuiContainer guiContainer, Container container, IInventory iInventory, List<Slot> slotList) {

        ContainerSection theWholeSection = createSection(slotList);

        if (InventoryUtil.isPlayerInventory(iInventory)) {
            int size = slotList.size();
            ContainerSection hotBar, playerStorage;
            if (size == 9) {// only hotBar, happens in creative screen
                putSection(EnumSection.InventoryHotBar, theWholeSection);
            } else if (size == 36) {// only hotBar and storage
                Slot sample = slotList.get(0);
                if (InventoryUtil.getSlotId(sample) == 0) {// this means hotBar then storage
                    hotBar = createSection(slotList.subList(0, 9));
                    playerStorage = createSection(slotList.subList(9, 36));
                } else {// this means storage then hotBar
                    playerStorage = createSection(slotList.subList(0, 27));
                    hotBar = createSection(slotList.subList(27, 36));
                }
                putSection(EnumSection.InventoryHotBar, hotBar);
                putSection(EnumSection.InventoryStorage, playerStorage);
            } else if (size == 41) {// armor, storage, hotBar, offHand
                ContainerSection armor = createSection(slotList.subList(0, 4));
                playerStorage = createSection(slotList.subList(4, 31));
                hotBar = createSection(slotList.subList(31, 40));
                ContainerSection offHand = createSection(slotList.subList(40, 41));
                putSection(EnumSection.InventoryHotBar, hotBar);
                putSection(EnumSection.InventoryStorage, playerStorage);
                putSection(EnumSection.Armor, armor);
                putSection(EnumSection.OffHand, offHand);
            }
            return;
        }

        if (container instanceof ContainerFurnace) {
            putSection(EnumSection.FurnaceIn, createSection(slotList.subList(0, 1)));
            putSection(EnumSection.FurnaceFuel, createSection(slotList.subList(1, 2)));
            putSection(EnumSection.FurnaceOut, createSection(slotList.subList(2, 3)));
            return;
        }

        if (iInventory instanceof ContainerMerchant) {
            putSection(EnumSection.MerchantIn, createSection(slotList.subList(0, 2)));
            putSection(EnumSection.MerchantOut, createSection(slotList.subList(2, 3)));
            return;
        }

        if (container instanceof ContainerBrewingStand) {
            putSection(EnumSection.BrewingBottles, createSection(slotList.subList(0, 3)));
            putSection(EnumSection.BrewingIngredient, createSection(slotList.subList(3, 4)));
            putSection(EnumSection.BrewingFuel, createSection(slotList.subList(4, 5)));
            return;
        }

        if (iInventory instanceof InventoryCrafting) {
            putSection(EnumSection.CraftMatrix, theWholeSection);
            return;
        }

        if (iInventory instanceof InventoryCraftResult) {
            putSection(EnumSection.CraftResult, theWholeSection);
            return;
        }

//        if (container instanceof StonecutterContainer) {
//            putSection(EnumSection.StoneCutterIn, theWholeSection);
//            return;
//        }

//        if (container instanceof CartographyTableContainer) {
//            putSection(EnumSection.CartographyIn, createSection(slotList.subList(0, 1)));
//            putSection(EnumSection.CartographyIn2, createSection(slotList.subList(1, 2)));
//            return;
//        }

        if (container instanceof ContainerRepair) {
            putSection(EnumSection.AnvilIn1, createSection(slotList.subList(0, 1)));
            putSection(EnumSection.AnvilIn2, createSection(slotList.subList(1, 2)));
            return;
        }

//        if (container instanceof SmithingContainer) {
//            putSection(EnumSection.SmithIn1, createSection(slotList.subList(0, 1)));
//            putSection(EnumSection.SmithIn2, createSection(slotList.subList(1, 2)));
//            putSection(EnumSection.SmithIn3, createSection(slotList.subList(2, 3)));
//            return;
//        }

//        if (container instanceof GrindstoneContainer) {
//            putSection(EnumSection.GrindstoneIn, theWholeSection);
//            return;
//        }

//        if (title.getContent() instanceof TranslatableTextContent translatable) {
//            if (translatable.getKey().equals("gca.player.inventory")) {
//                putSection(EnumSection.FakePlayerActions, createSection(createFakePlayerActions(slotList)));
//                putSection(EnumSection.FakePlayerArmor, createSection(slotList.subList(1, 5)));
//                putSection(EnumSection.FakePlayerOffHand, createSection(slotList.subList(7, 8)));
//                putSection(EnumSection.FakePlayerInventoryStorage, createSection(slotList.subList(18, 45)));
//                putSection(EnumSection.FakePlayerInventoryHotBar, createSection(slotList.subList(45, 54)));
//                return;
//            }
//            if (translatable.getKey().equals("gca.player.ender_chest")) {
//                putSection(EnumSection.FakePlayerEnderChestActions, createSection(slotList.subList(0, 27)));
//                putSection(EnumSection.FakePlayerEnderChestInventory, createSection(slotList.subList(27, 54)));
//                return;
//            }
//        }

        if (container instanceof GuiContainerCreative.ContainerCreative) {
            putSection(EnumSection.CreativeTab, theWholeSection);
            return;
        }

        // it uses vanilla slot
        if (ModReference.hasMod(ModReference.QUALITYTOOLS) && QualityToolsAccess.isReforgingContainer(container)) {
            putSection(EnumSection.QualityToolsReforgingTool, slotList.subList(0, 1));
            putSection(EnumSection.QualityToolsReforgingMaterial, slotList.subList(1, 2));
            return;
        }

        this.handleUnidentified(theWholeSection);
    }

    void handleUnidentified(ContainerSection section) {
        this.unidentifiedHandler.accept(section);
    }

    protected ContainerSection createSection(List<Slot> slots) {
        return new ContainerSection(slots);
    }

    protected void putSection(EnumSection key, List<Slot> slots) {
        putSection(key, createSection(slots));
    }

    protected void putSection(EnumSection key, ContainerSection section) {
        this.sectionReceiver.accept(key, section);
    }


//    private static List<Slot> createFakePlayerActions(List<Slot> total) {
//        ArrayList<Slot> slots = new ArrayList<>(total.subList(8, 18));
//        slots.add(total.getFirst());
//        slots.add(total.get(5));
//        slots.add(total.get(6));
//        return slots;
//    }
}
