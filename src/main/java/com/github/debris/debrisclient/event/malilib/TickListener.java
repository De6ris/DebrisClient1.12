package com.github.debris.debrisclient.event.malilib;

import com.github.debris.debrisclient.feat.AutoClicker;
import com.github.debris.debrisclient.feat.AutoFish;
import com.github.debris.debrisclient.feat.AutoPickUp;
import com.github.debris.debrisclient.feat.FreeCam;
import com.github.debris.debrisclient.feat.task.TaskQueue;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.Minecraft;

public class TickListener implements IClientTickHandler {
    @Override
    public void onClientTick(Minecraft client) {
        AutoPickUp.onTick(client);
        FreeCam.onTick(client);
        AutoClicker.onTick(client);
        AutoFish.onTick(client);
        TaskQueue.onTick(client);
    }

}
