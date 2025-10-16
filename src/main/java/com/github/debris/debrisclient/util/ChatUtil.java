package com.github.debris.debrisclient.util;

import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
    public static void addLocalChat(Minecraft client, String chat) {
        client.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(chat));
    }

    public static void setActionBar(String message) {
        InfoUtils.printActionbarMessage(message);
    }
}
