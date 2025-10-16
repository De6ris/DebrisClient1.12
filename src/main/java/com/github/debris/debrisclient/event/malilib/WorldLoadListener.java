package com.github.debris.debrisclient.event.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.FreeCam;
import com.github.debris.debrisclient.feat.IMBlocker;
import fi.dy.masa.malilib.interfaces.IWorldLoadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import javax.annotation.Nullable;

public class WorldLoadListener implements IWorldLoadListener {
    @Override
    public void onWorldLoadPre(@Nullable WorldClient worldBefore, @Nullable WorldClient worldAfter, Minecraft mc) {
        FreeCam.clear(mc);
    }

    @Override
    public void onWorldLoadPost(@Nullable WorldClient worldBefore, @Nullable WorldClient worldAfter, Minecraft mc) {
        IMBlocker.onWorldLoad();
        DCConfig.RuneTweak.onValueChanged();
    }
}
