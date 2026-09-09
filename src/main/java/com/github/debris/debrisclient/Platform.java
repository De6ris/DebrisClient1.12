package com.github.debris.debrisclient;

import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class Platform {
    public static Path getRootDir() {
        return Minecraft.getMinecraft().gameDir.toPath();
    }

    public static boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }
}
