package com.github.debris.debrisclient.feat.lock;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.gui.button.GuiBetterButton;
import com.github.debris.debrisclient.unsafe.mod.LocksAccess;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.Arrays;

public class LocksTweak {
    public static final int BUTTON_ID = "auto_unlock".hashCode();

    /**
     * Say 4,5,1,2,6,0,3: The whole password
     * Say 4,5,1,-1,-1,-1,-1: Part known
     */
    private static int[] password = null;

    public static boolean active() {
        return DCConfig.LocksTweak.getBooleanValue();
    }

    public static boolean inactive() {
        return !DCConfig.LocksTweak.getBooleanValue();
    }

    public static void onOpen(GuiContainer gui, int length) {
        AutoUnlock.disable();
        if (inactive()) return;
        if (LocksAccess.isNewLock(gui)) {
            password = new int[length];
            Arrays.fill(password, -1);
        }
    }

    public static void onPin(GuiContainer gui, int currPin, boolean correct, boolean reset) {
        if (inactive()) return;
        if (correct) {
            int index = LocksAccess.getCurrentIndex(gui);
            password[index] = currPin;
        }
        AutoUnlock.onPin(gui, currPin, correct, reset);
    }

    public static void onRender(GuiContainer gui, double[] posX, int mouseX, int mouseY) {
        if (inactive()) return;
        LockHint.renderHint(gui, posX, password);
    }

    public static GuiBetterButton createButton(GuiContainer gui) {
        return new GuiBetterButton(
                BUTTON_ID,
                gui.guiLeft + 40,
                gui.guiTop - 16,
                30,
                16,
                "自动"
        );
    }

    public static void auto() {
        if (inactive()) return;
        AutoUnlock.enable();
    }

    public static void onTick(GuiContainer gui, boolean[] pins, int currentPin) {
        if (inactive()) return;
        AutoUnlock.onTick(gui, pins, currentPin, password);
    }
}
