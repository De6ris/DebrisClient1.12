package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.mixins.client.IClientMixin;
import net.minecraft.client.Minecraft;

public class AccessorUtil {
    public static void attack(Minecraft client) {
        ((IClientMixin) client).invokeClickMouse();
    }

    public static void use(Minecraft client) {
        ((IClientMixin) client).invokeRightClickMouse();
    }
}
