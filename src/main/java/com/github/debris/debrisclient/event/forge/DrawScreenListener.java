package com.github.debris.debrisclient.event.forge;

import com.github.debris.debrisclient.inventory.feat.InventoryTweaks;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DrawScreenListener {
    @SubscribeEvent
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiContainer) {
            InventoryTweaks.onRenderTick(((GuiContainer) gui));
        }
    }
}
