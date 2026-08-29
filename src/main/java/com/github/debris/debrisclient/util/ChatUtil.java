package com.github.debris.debrisclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
    public static void addLocalChat(Minecraft client, String chat) {
        addLocalChat(client, new TextComponentString(chat));
    }

    public static void addLocalChat(Minecraft client, ITextComponent component) {
        client.ingameGUI.addChatMessage(ChatType.CHAT, component);
    }

    public static void setActionBar(String message) {
        setActionBar(new TextComponentString(message));
    }

    public static void setActionBar(ITextComponent component) {
        Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, component);
    }
}
