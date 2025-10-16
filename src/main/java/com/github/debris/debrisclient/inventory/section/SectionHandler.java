package com.github.debris.debrisclient.inventory.section;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.ForgeSlotHelper;
import com.github.debris.debrisclient.util.InventoryUtil;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SectionHandler {
    private static final Logger LOGGER = LogManager.getLogger(SectionHandler.class);

    private final List<ContainerSection> unIdentifiedSections = new ArrayList<>();
    private final Map<EnumSection, ContainerSection> sectionMap = new EnumMap<>(EnumSection.class);

    public SectionHandler(GuiContainer guiContainer) {
        this.identifyContainer(guiContainer, InventoryUtil.getContainer(guiContainer));
    }

    public SectionHandler(Container container) {
        this.identifyContainer(null, container);
    }

    private void identifyContainer(@Nullable GuiContainer guiContainer, Container container) {
        if (container == null) {
            LOGGER.warn("null container while identifying screen {} ", guiContainer);
            return;
        }

        List<Slot> slots = InventoryUtil.getSlots(container);

        // true forge, false vanilla
        Map<Boolean, List<Slot>> forgeOrVanilla = slots.stream().collect(Collectors.partitioningBy(ForgeSlotHelper::isForgeSlot));

        Map<IItemHandler, List<SlotItemHandler>> groupedByItemHandler = forgeOrVanilla.get(true).stream()
                .map(x -> ((SlotItemHandler) x))
                .collect(Collectors.groupingBy(ForgeSlotHelper::getItemHandler));
        ForgeSectionIdentifier forgeIdentifier = new ForgeSectionIdentifier(this::putSection, this::handleUnidentified);
        groupedByItemHandler.forEach((itemHandler, partSlots) -> forgeIdentifier.identifyForge(guiContainer, container, itemHandler, partSlots));

        Map<Boolean, List<Slot>> invNullMap = forgeOrVanilla.get(false).stream().collect(Collectors.partitioningBy(x -> x.inventory == null));

        List<Slot> invNullSlots = invNullMap.get(true);
        handleUnidentified(new ContainerSection(invNullSlots));

        Map<IInventory, List<Slot>> groupedByInventory = invNullMap.get(false).stream().collect(Collectors.groupingBy(x -> x.inventory));
        SectionIdentifier identifier = new SectionIdentifier(this::putSection, this::handleUnidentified);
        groupedByInventory.forEach((iInventory, partSlots) -> identifier.identify(guiContainer, container, iInventory, partSlots));
    }

    private void putSection(EnumSection key, ContainerSection section) {
        Map<EnumSection, ContainerSection> sectionMap = this.sectionMap;
        if (sectionMap.containsKey(key)) {
            LOGGER.warn("duplicate section for key {}: {} replacing {}", key, sectionMap.get(key), section);
        }
        sectionMap.put(key, section);
    }

    private void handleUnidentified(ContainerSection section) {
        this.sectionMap.putIfAbsent(EnumSection.Unidentified, section);
        this.unIdentifiedSections.add(section);
        if (DCConfig.Debug.getBooleanValue()) LOGGER.warn("unidentified section, slots: {}", section.slots());
    }

    public static void onClientPlayerInit(Container playerContainer) {
        SectionHandler sectionHandler = new SectionHandler(playerContainer);
        ((IContainer) playerContainer).dc$setSectionHandler(sectionHandler);
    }

    public static void updateSection(GuiContainer guiContainer) {
        Container container = InventoryUtil.getContainer(guiContainer);
        ((IContainer) container).dc$setSectionHandler(new SectionHandler(guiContainer));
    }

    public static SectionHandler getSectionHandler() {
        Container container = InventoryUtil.getCurrentContainer();
        SectionHandler sectionHandler = ((IContainer) container).dc$getSectionHandler();
        if (sectionHandler != null) return sectionHandler;

        GuiScreen screen = GuiUtils.getCurrentScreen();
        if (!(screen instanceof GuiContainer)) {
            LOGGER.warn("weird that in a non container screen with non-default container, screen:\n{}", screen);
            return ((IContainer) InventoryUtil.getCurrentContainer()).dc$getSectionHandler();
        }
        GuiContainer guiContainer = (GuiContainer) screen;

        SectionHandler newHandler = new SectionHandler(guiContainer);
        ((IContainer) container).dc$setSectionHandler(newHandler);
        return newHandler;
    }

    public static ContainerSection getSection(EnumSection section) {
        ContainerSection ret = getSectionHandler().sectionMap.get(section);
        if (ret == null) {
            LOGGER.warn("no section instance for {}", section);
            return ContainerSection.EMPTY;
        }
        return ret;
    }

    public static boolean hasSection(EnumSection section) {
        return getSectionHandler().sectionMap.containsKey(section);
    }

    public static List<ContainerSection> getUnIdentifiedSections() {
        return getSectionHandler().unIdentifiedSections;
    }

    public static Stream<ContainerSection> streamAllSections() {
        SectionHandler sectionHandler = getSectionHandler();
        return Stream.concat(sectionHandler.unIdentifiedSections.stream(), sectionHandler.sectionMap.values().stream()).distinct();
    }

    public static Optional<ContainerSection> getSectionMouseOver() {
        Optional<Slot> slotMouseOver = InventoryUtil.getSlotMouseOver();
        return slotMouseOver.map(SectionHandler::getSection);
    }

    public static ContainerSection getSection(Slot slot) {
        return streamAllSections().filter(x -> x.hasSlot(slot)).findFirst().orElse(ContainerSection.EMPTY);
    }

    public static ContainerSection getSection(int globalIndex) {
        return streamAllSections()
                .filter(x -> x.slots()
                        .stream()
                        .anyMatch(y -> InventoryUtil.getSlotId(y) == globalIndex)
                )
                .findFirst()
                .orElse(ContainerSection.EMPTY);
    }
}
