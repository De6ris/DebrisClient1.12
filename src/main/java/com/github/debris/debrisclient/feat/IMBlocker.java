package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.windows.WindowsImManager;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;

public class IMBlocker {
    public static final ImmutableList<String> BUILT_IN_SCREENS = ImmutableList.of(
            "xaero.common.gui.GuiAddWaypoint",
            "com.xray.gui.manage.GuiBlockListScrollable",
            "com.xray.gui.GuiSelectionScreen",
            "net.blay09.mods.waystones.client.gui.GuiEditWaystone"
    );

    public static boolean isActive() {
        return DCConfig.IMBlocker.getBooleanValue();
    }

    public static void onNewScreen(GuiScreen guiScreenIn) {
        if (isActive()) {
            if (IMBlocker.handleIndividually(guiScreenIn)) return;
            IMBlocker.setState(IMBlocker.shouldUseIM(guiScreenIn));
        }
    }

    public static void onNewChat(String defaultInputFieldText) {
        if (isActive()) {
            IMBlocker.setState(!defaultInputFieldText.startsWith("/"));
        }
    }

    public static void onTextFieldFocus(boolean isFocusedIn) {
        if (isActive()) {
            IMBlocker.setState(isFocusedIn);
        }
    }

    public static void onWorldLoad() {
        if (isActive()) disable();
    }

    /**
     * If true, skip the default behavior, at handle this at {@link GuiScreen#initGui()}
     */
    public static boolean handleIndividually(GuiScreen guiScreenIn) {
        if (guiScreenIn instanceof GuiChat) return true;
        return false;
    }

    public static boolean shouldUseIM(GuiScreen guiScreenIn) {
        if (guiScreenIn == null) return false;
        if (guiScreenIn instanceof GuiScreenBook) return true;
        if (guiScreenIn instanceof GuiEditSign) return true;
        if (DCConfig.IMBlockerWhiteList.getStrings().contains(guiScreenIn.getClass().getName())) return true;
        return false;
    }

    public static void setState(boolean state) {
        if (state) {
            enable();
        } else {
            disable();
        }
    }

    public static void enable() {
        if (WindowsImManager.VALID) WindowsImManager.makeOn();
    }

    public static void disable() {
        if (WindowsImManager.VALID) WindowsImManager.makeOff();
    }

    public static ImmutableList<String> getBuiltInScreenNames() {
        return ImmutableList.of(
                "xaero.common.gui.GuiAddWaypoint",
                "com.xray.gui.manage.GuiBlockListScrollable",
                "com.xray.gui.GuiSelectionScreen",
                "net.blay09.mods.waystones.client.gui.GuiEditWaystone"
        );
    }

    public static Mode getMode() {
        return Mode.ENGLISH;// TODO
    }

    public static void switchMode() {
        if (getMode() == Mode.ENGLISH) {
            switchToChinese();
        } else {
            switchToEnglish();
        }
    }

    public static void switchToEnglish() {
        if (WindowsImManager.VALID) WindowsImManager.switchToEnglish();
    }

    public static void switchToChinese() {
        if (WindowsImManager.VALID) WindowsImManager.switchToChinese();
    }

    public enum Mode {
        ENGLISH,
        CHINESE,
        ;
    }
}
