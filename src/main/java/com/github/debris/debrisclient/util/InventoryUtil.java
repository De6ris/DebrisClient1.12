package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.inventory.section.ContainerSection;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class InventoryUtil {
    public static void dropOne(Slot slot) {
        drop(slot, false);
    }

    public static void dropStack(Slot slot) {
        drop(slot, true);
    }

    public static void dropStackIfPossible(Slot slot) {
        if (hasItem(slot)) dropStack(slot);
    }

    // the second param is special
    public static void drop(Slot slot, boolean ctrl) {
        clickSlot(slot, ctrl ? 1 : 0, ClickType.THROW);
    }

    // hotBar: 0-8 or 40
    public static void swapHotBar(Slot slot, int hotBar) {
        clickSlot(slot, hotBar, ClickType.SWAP);
    }

    public static void gatherItems(Slot slot) {
        if (isHoldingItem()) {
            clickSlot(slot, 0, ClickType.PICKUP_ALL);
        }
    }

    public static void moveToEmpty(Slot slot, Slot empty) {
        leftClick(slot);
        leftClick(empty);
    }

    public static void moveOneItem(Slot to, Slot from) {
        leftClick(from);
        rightClick(to);
        leftClick(from);
    }

    public static void swapSlots(Slot slot, Slot other) {
        swapHotBar(slot, 0);
        swapHotBar(other, 0);
        swapHotBar(slot, 0);
    }

    public static boolean canMergeSlot(Slot to, Slot from) {
        if (hasItem(to) && hasItem(from)) {
            return ItemUtil.canMerge(to.getStack(), from.getStack());
        }
        return true;// empty slots always merge
    }

    public static ItemStack getHeldStack() {
        return getPlayerInventory().getItemStack();
    }

    public static boolean isHoldingItem() {
        return !getHeldStack().isEmpty();
    }

    public static void putHeldItemDown(ContainerSection section) {
        if (isHoldingItem()) {
            section.getEmptySlot().ifPresent(InventoryUtil::leftClick);
        }
    }

    public static void dropHeldItem() {
        if (isHoldingItem()) {
            clickSlot(-999, 0, ClickType.PICKUP);
        }
    }

    public static void quickMove(Slot slot) {
        click(slot, false, ClickType.QUICK_MOVE);
    }

    public static void quickMove(int index) {
        click(index, false, ClickType.QUICK_MOVE);
    }

    public static void quickMoveIfPossible(Slot slot) {
        if (hasItem(slot)) quickMove(slot);
    }

    public static void startSpreading(boolean rightClick) {
        clickSlot(-999, Container.getQuickcraftMask(0, rightClick ? 1 : 0), ClickType.QUICK_CRAFT);
    }

    public static void addToSpreading(Slot slot, boolean rightClick) {
        clickSlot(slot, Container.getQuickcraftMask(1, rightClick ? 1 : 0), ClickType.QUICK_CRAFT);
    }

    public static void finishSpreading(boolean rightClick) {
        clickSlot(-999, Container.getQuickcraftMask(2, rightClick ? 1 : 0), ClickType.QUICK_CRAFT);
    }

    public static void leftClick(Slot slot) {
        click(slot, false, ClickType.PICKUP);
    }

    public static void leftClick(int index) {
        click(index, false, ClickType.PICKUP);
    }

    public static void rightClick(Slot slot) {
        click(slot, true, ClickType.PICKUP);
    }

    public static void rightClick(int index) {
        click(index, true, ClickType.PICKUP);
    }

    public static void click(Slot slot, boolean rightClick, ClickType type) {
        click(getSlotId(slot), rightClick, type);
    }

    public static void click(int index, boolean rightClick, ClickType type) {
        clickSlot(index, rightClick ? 1 : 0, type);
    }

    // the button also imply some other data
    public static void clickSlot(Slot slot, int button, ClickType type) {
        clickSlot(getSlotId(slot), button, type);
    }

    // This is the final click slot, act as a valve
    public static void clickSlot(int index, int button, ClickType type) {
        if (GuiUtils.getCurrentScreen() instanceof GuiContainerCreative) {
            Container currentContainer = getCurrentContainer();
            currentContainer.slotClick(index, button, type, getClientPlayer());
            currentContainer.detectAndSendChanges();
        } else {
            getController().windowClick(getWindowID(), index, button, type, getClientPlayer());
        }
        markDirty();
    }

    private static int changeCount = 0;

    private static void markDirty() {
        changeCount++;
    }

    public static int getChangeCount() {
        return changeCount;
    }

//    public static void clickButton(int buttonId) {
//        getController().clickButton(getWindowID(), buttonId);
//    }
//
//    public static void clickRecipe(NetworkRecipeId recipe, boolean craftAll) {
//        getController().clickRecipe(getWindowID(), recipe, craftAll);
//    }

    public static void dropAllMatching(Predicate<ItemStack> predicate) {
        getSlots().stream().filter(x -> predicate.test(x.getStack())).forEach(InventoryUtil::dropStack);
    }

    public static Optional<Slot> getSlotMouseOver() {
        return Optional.ofNullable(getGuiContainer().getSlotUnderMouse());
    }

    public static int getSlotId(Slot slot) {
//        if (slot instanceof CreativeInventoryScreen.CreativeSlot creativeSlot) {
//            return creativeSlot.slot.id;
//        } else {
        return slot.slotNumber;
//        }
    }

    public static boolean isEmpty(Slot slot) {
        return !slot.getHasStack();
    }

    public static boolean hasItem(Slot slot) {
        return slot.getHasStack();
    }

    public static ItemStack getStack(Slot slot) {
        return slot.getStack();
    }

    public static boolean isEmpty(ItemStack itemStack) {
        return itemStack.isEmpty();
    }

    public static boolean hasItem(ItemStack itemStack) {
        return !itemStack.isEmpty();
    }

    public static List<Slot> getSlots() {
        return getSlots(getCurrentContainer());
    }

    public static List<Slot> getSlots(Container container) {
        return container.inventorySlots;
    }

    public static GuiContainer getGuiContainer() {
        return (GuiContainer) getClient().currentScreen;
    }

    public static int getWindowID() {
        return getCurrentContainer().windowId;
    }

    public static PlayerControllerMP getController() {
        return getClient().playerController;
    }

    public static Container getContainer(GuiContainer guiContainer) {
        return guiContainer.inventorySlots;
    }

    public static Container getInventoryContainer() {
        return getClientPlayer().inventoryContainer;
    }

    public static Container getCurrentContainer() {
        return getClientPlayer().openContainer;
    }

    public static EntityPlayerSP getClientPlayer() {
        return getClient().player;
    }

    public static Minecraft getClient() {
        return Minecraft.getMinecraft();
    }

    public static InventoryPlayer getPlayerInventory() {
        return getClientPlayer().inventory;
    }

    public static boolean isPlayerInventory(IInventory inventory) {
        return inventory instanceof InventoryPlayer;
    }

}
