package com.github.debris.debrisclient.feat.task;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class TaskQueue {
    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static int TICK_COUNTER = 0;

    public static void onTick(Minecraft client) {
        ENTRIES.removeIf(entry -> {
            if (entry.killTick == TICK_COUNTER) {
                entry.task.execute(client);
                return true;
            }
            return false;
        });
        TICK_COUNTER++;
    }

    public static void schedule(Task task, int life) {
        ENTRIES.add(new Entry(task, life + TICK_COUNTER));
    }

    private static class Entry {
        private final Task task;
        private final int killTick;

        private Entry(Task task, int killTick) {
            this.task = task;
            this.killTick = killTick;
        }
    }
}
