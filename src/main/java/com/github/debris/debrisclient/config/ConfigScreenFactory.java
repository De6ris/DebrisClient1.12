package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.gui.screen.DCConfigScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

public class ConfigScreenFactory extends DefaultGuiFactory {
    public ConfigScreenFactory() {
        super(DebrisClient.MOD_ID, DebrisClient.MOD_NAME + " configs");
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        DCConfigScreen gui = new DCConfigScreen(parentScreen);
        gui.setParent(parentScreen);
        return gui;
    }
}
