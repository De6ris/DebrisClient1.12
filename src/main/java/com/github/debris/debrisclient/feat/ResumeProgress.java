package com.github.debris.debrisclient.feat;

import net.minecraft.client.gui.GuiScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class ResumeProgress {
    public static final Map<Class<? extends GuiScreen>, Integer> PROGRESS_MAP = new HashMap<>();

    public static OptionalInt getProgress(GuiScreen guiListBase) {
        Class<? extends GuiScreen> clazz = guiListBase.getClass();
        if (PROGRESS_MAP.containsKey(clazz)) {
            return OptionalInt.of(PROGRESS_MAP.get(clazz));
        }
        return OptionalInt.empty();
    }

    public static void saveProgress(GuiScreen screen, int progress) {
        PROGRESS_MAP.put(screen.getClass(), progress);
    }
}
