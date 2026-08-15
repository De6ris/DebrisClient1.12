package com.github.debris.debrisclient.feat.lock;

import com.github.debris.debrisclient.unsafe.mod.LocksAccess;
import net.minecraft.client.gui.inventory.GuiContainer;

public class AutoUnlock {
    private static boolean active = false;

    private static int tryingIndex = LocksTweak.UNKNOWN;
    private static boolean[] triedPins = null;

    public static void enable() {
        active = true;
    }

    public static void disable() {
        active = false;
    }

    public static void clear() {
        tryingIndex = LocksTweak.UNKNOWN;
        triedPins = null;
    }

    public static void onPin(GuiContainer gui, int currPin, boolean correct, boolean reset) {
        if (!active) return;
        if (!correct && triedPins != null && tryingIndex == LocksAccess.getCurrentIndex(gui)) {
            triedPins[currPin] = true;
        }
    }

    public static void onTick(GuiContainer gui, boolean[] pins, int currentPin, int[] password) {
        if (!active) return;
        int index = LocksAccess.getCurrentIndex(gui);
        if (index == pins.length) {
            LocksAccess.release(gui);
            active = false;// success
            return;
        }

        int order = password[index];
        int destination;
        if (order == LocksTweak.UNKNOWN) {
            destination = findNear(pins, currentPin, index);
        } else {
            destination = order;
        }

        if (destination == LocksTweak.UNKNOWN) {
            // Impossible case, but I keep it
            return;
        }
        pin(gui, currentPin, destination);
    }

    private static int findNear(boolean[] pins, int currentPin, int index) {
        if (tryingIndex != index || triedPins == null || triedPins.length != pins.length) {
            // update
            triedPins = new boolean[pins.length];
            tryingIndex = index;
        }

        int destination = LocksTweak.UNKNOWN;
        int near = Integer.MAX_VALUE;
        for (int pin = 0; pin < pins.length; pin++) {
            if (pins[pin]) continue;// done
            if (triedPins[pin]) continue;// not here
            int distance = Math.abs(pin - currentPin);
            if (distance < near) {
                destination = pin;
                near = distance;
            }
        }
        return destination;
    }

    private static void pin(GuiContainer gui, int currentPin, int destination) {
        if (currentPin < destination) {
            LocksAccess.moveRight(gui);
            return;
        }
        if (currentPin == destination) {
            LocksAccess.release(gui);
            LocksAccess.sendPin(gui);
            return;
        }
        LocksAccess.moveLeft(gui);
    }
}
