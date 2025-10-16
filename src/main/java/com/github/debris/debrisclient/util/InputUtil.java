package com.github.debris.debrisclient.util;

import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputUtil {
    public static boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public static boolean isCtrlDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

    public static boolean isLeftClicking() {
        return Mouse.isButtonDown(0);
    }

    public static boolean isRightClicking() {
        return Mouse.isButtonDown(1);
    }

    public static boolean isKeyDown(int keyCode) {
        return KeybindMulti.isKeyDown(keyCode);
    }
}
