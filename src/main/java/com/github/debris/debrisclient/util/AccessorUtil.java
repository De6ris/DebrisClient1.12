package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.mixins.client.IClientMixin;
import com.github.debris.debrisclient.mixins.client.gui.IGuiScreenMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

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
}
