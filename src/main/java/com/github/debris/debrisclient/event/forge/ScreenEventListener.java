package com.github.debris.debrisclient.event.forge;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.lock.LocksTweak;
import com.github.debris.debrisclient.inventory.feat.InventoryTweaks;
import com.github.debris.debrisclient.unsafe.mod.LocksAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ScreenEventListener {
    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (DCConfig.LocksTweak.getBooleanValue() && ModReference.hasMod(ModReference.LOCKS) && LocksAccess.isLockPickingScreen(gui)) {
            event.getButtonList().add(LocksTweak.createButton((GuiContainer) gui));
        }
    }

    @SubscribeEvent
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiContainer) {
            InventoryTweaks.onRenderTick(((GuiContainer) gui));
        }
    }

    @SubscribeEvent
    public static void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        int id = event.getButton().id;
        if (id == LocksTweak.BUTTON_ID) {
            LocksTweak.auto();
        }
    }
}
