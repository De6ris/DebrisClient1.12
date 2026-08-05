package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.windows.WindowsImManager;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;

import javax.annotation.Nullable;

public class IMBlocker {
    private static final OSType OS_TYPE = OSType.get();

    private static final boolean VALID = OS_TYPE != OSType.NONE;

    public static final ImmutableList<String> BUILT_IN_SCREENS = ImmutableList.of(
            "xaero.common.gui.GuiAddWaypoint",
            "com.xray.gui.manage.GuiBlockListScrollable",
            "com.xray.gui.GuiSelectionScreen",
            "net.blay09.mods.waystones.client.gui.GuiEditWaystone",
            "vazkii.quark.client.gui.GuiBetterEditSign"
    );

    public static boolean isActive() {
        return VALID && DCConfig.IMBlocker.getBooleanValue();
    }

    public static void onNewScreen(@Nullable GuiScreen guiScreenIn) {
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

    public static void onGameFocus() {
        if (isActive()) disable();
    }

    public static void onConfigChange(ConfigBoolean configBoolean) {
        if (VALID && !configBoolean.getBooleanValue()) {
            IMBlocker.enable();
        }
    }


    /**
     * If true, skip the default behavior, at handle this at {@link GuiScreen#initGui()}
     */
    private static boolean handleIndividually(@Nullable GuiScreen guiScreenIn) {
        if (guiScreenIn instanceof GuiChat) return true;
        return false;
    }

    private static boolean shouldUseIM(GuiScreen guiScreenIn) {
        if (guiScreenIn == null) return false;
        if (guiScreenIn instanceof GuiScreenBook) return true;
        if (guiScreenIn instanceof GuiEditSign) return true;
        if (DCConfig.IMBlockerWhiteList.getStrings().contains(guiScreenIn.getClass().getName())) return true;
        return false;
    }

    private static void setState(boolean state) {
        if (state) {
            enable();
        } else {
            disable();
        }
    }

    private static void enable() {
        if (OS_TYPE.isWindows()) {
            WindowsImManager.makeOn();
        }
    }

    private static void disable() {
        if (OS_TYPE.isWindows()) {
            WindowsImManager.makeOff();
        }
    }

    public static Mode getMode() {
        return Mode.ENGLISH;// TODO
    }

    private static void switchMode() {
        if (getMode() == Mode.ENGLISH) {
            switchToChinese();
        } else {
            switchToEnglish();
        }
    }

    private static void switchToEnglish() {
        if (OS_TYPE.isWindows()) {
            WindowsImManager.switchToEnglish();
        }
    }

    private static void switchToChinese() {
        if (OS_TYPE.isWindows()) {
            WindowsImManager.switchToChinese();
        }
    }

    public enum Mode {
        ENGLISH,
        CHINESE,
        ;
    }
}
