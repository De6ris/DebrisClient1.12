package com.github.debris.debrisclient.unsafe.mod;

import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.util.Cuboid6i;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class LocksAccess {
    public static final boolean KEEP_HISTORY = true;

    private static Cuboid6i lockPos = null;

    public static boolean isNewLock(GuiContainer gui) {
        if (KEEP_HISTORY) {
            // not very reliable, currently
            Cuboid6i box = container((LockPickingGui) gui).lockable.box;
            if (box.equals(lockPos)) {
                return false;
            }
            lockPos = box;
            return true;
        }
        return true;
    }

    public static int getCurrentIndex(GuiContainer gui) {
        return container((LockPickingGui) gui).getCurrentIndex();
    }

    private static LockPickingContainer container(LockPickingGui gui) {
        return (LockPickingContainer) gui.inventorySlots;
    }

    public static boolean isLockPickingScreen(GuiScreen gui) {
        return gui instanceof LockPickingGui;
    }

    public static void moveRight(GuiContainer screen) {
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindRight.getKeyCode());
    }

    public static void moveLeft(GuiContainer screen) {
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindLeft.getKeyCode());
    }

    public static void release(GuiContainer screen) {
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindLeft.getKeyCode());
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindRight.getKeyCode());
    }

    public static void sendPin(GuiContainer screen) {
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindForward.getKeyCode());
    }
}
