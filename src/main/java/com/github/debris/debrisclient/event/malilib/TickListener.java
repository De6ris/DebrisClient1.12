package com.github.debris.debrisclient.event.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.AutoClicker;
import com.github.debris.debrisclient.feat.AutoFish;
import com.github.debris.debrisclient.feat.FreeCam;
import com.github.debris.debrisclient.feat.MiscFeat;
import com.github.debris.debrisclient.feat.task.TaskQueue;
import com.github.debris.debrisclient.util.Predicates;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.Minecraft;

public class TickListener implements IClientTickHandler {
    @Override
    public void onClientTick(Minecraft client) {
        if (Predicates.inGameNoGui(client)) {
            if (DCConfig.AutoPickUp.getKeybind().isKeybindHeld()) MiscFeat.runAutoPickUp(client);
        }
        FreeCam.onTick(client);
        AutoClicker.onTick(client);
        AutoFish.onTick(client);
        TaskQueue.onTick(client);
    }
}
