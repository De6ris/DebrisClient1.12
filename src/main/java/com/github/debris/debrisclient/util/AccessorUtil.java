package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.mixins.client.IClientMixin;
import com.github.debris.debrisclient.mixins.client.resources.ILocaleMixin;
import com.github.debris.debrisclient.mixins.client.gui.IGuiScreenMixin;
import com.github.debris.debrisclient.mixins.client.resources.ILanguageManagerMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.Locale;

import java.util.List;
import java.util.Map;

public class AccessorUtil {
    public static void attack(Minecraft client) {
        ((IClientMixin) client).invokeClickMouse();
    }

    public static void use(Minecraft client) {
        ((IClientMixin) client).invokeRightClickMouse();
    }

    public static List<GuiButton> getButtonList(GuiScreen screen) {
        return ((IGuiScreenMixin) screen).getButtonList();
    }

    public static Locale getCurrentLocale() {
        return ILanguageManagerMixin.getCurrentLocale();
    }

    public static Map<String, String> getProperties(Locale locale) {
        return ((ILocaleMixin) locale).getProperties();
    }
}
