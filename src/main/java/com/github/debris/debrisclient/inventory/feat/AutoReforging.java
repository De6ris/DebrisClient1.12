package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.baubles.AbstractReforgingTask;
import com.github.debris.debrisclient.unsafe.baubles.BountifulBaublesReforgingTask;
import com.github.debris.debrisclient.unsafe.baubles.QualityToolsReforgingTask;
import com.github.debris.debrisclient.unsafe.mod.BountifulBaublesAccess;
import com.github.debris.debrisclient.unsafe.mod.QualityToolsAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nullable;

public class AutoReforging {
    /**
     * 599873089
     */
    public static final int BUTTON_ID = "auto_reforging".hashCode();

    public static boolean tryAutoReforging(Minecraft client) {
        AbstractReforgingTask task = createReforgingTask(client);
        if (task != null) {
            Thread reforgingThread = new Thread(task, "ReforgingThread");
            reforgingThread.start();
            return true;
        }
        return false;
    }

    @Nullable
    private static AbstractReforgingTask createReforgingTask(Minecraft client) {
        GuiScreen screen = client.currentScreen;
        if (ModReference.hasMod(ModReference.QUALITYTOOLS) && QualityToolsAccess.canReforge(screen)) {
            return new QualityToolsReforgingTask(client);
        }
        if (ModReference.hasMod(ModReference.BOUNTIFULBAUBLES) && BountifulBaublesAccess.canReforge(screen)) {
            return new BountifulBaublesReforgingTask(client);
        }
        return null;
    }
}
