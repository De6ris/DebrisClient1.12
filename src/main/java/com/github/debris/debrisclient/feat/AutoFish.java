package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.FishingMBAccess;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;

public class AutoFish {
    public static void onTick(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return;
        if (!ModReference.hasMod(ModReference.FISHINGMADEBETTER)) return;
        FishingMBAccess.onTick(client);
    }
}
