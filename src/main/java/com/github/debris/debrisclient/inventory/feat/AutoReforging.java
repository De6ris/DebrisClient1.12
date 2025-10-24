package com.github.debris.debrisclient.inventory.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.baubles.AbstractReforgingTask;
import com.github.debris.debrisclient.unsafe.baubles.BountifulBaublesReforgingTask;
import com.github.debris.debrisclient.unsafe.baubles.QualityToolsReforgingTask;
import com.github.debris.debrisclient.unsafe.mod.BountifulBaublesAccess;
import com.github.debris.debrisclient.unsafe.mod.QualityToolsAccess;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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

    public static void onMouseClicked(GuiScreen screen, int mouseX, int mouseY, int mouseButton) {
        if (ModReference.hasMod(ModReference.QUALITYTOOLS) && QualityToolsAccess.isReforgingGUI(screen) && mouseButton == 1) {
            for (GuiButton button : AccessorUtil.getButtonList(screen)) {
                if (button.id != AutoReforging.BUTTON_ID) continue;
                if (button.mousePressed(screen.mc, mouseX, mouseY)) {
                    DCConfig.ReforgingLevel.cycle(true);
                    SoundUtil.playClickSound(screen.mc);
                    break;
                }
            }
        }
    }

    public static void renderTooltip() {

    }
}
