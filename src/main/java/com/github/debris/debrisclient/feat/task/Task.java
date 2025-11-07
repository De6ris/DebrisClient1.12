package com.github.debris.debrisclient.feat.task;

import net.minecraft.client.Minecraft;

public interface Task {
    TaskResult execute(Minecraft client);
}
