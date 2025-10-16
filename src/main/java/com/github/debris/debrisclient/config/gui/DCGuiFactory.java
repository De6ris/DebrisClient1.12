package com.github.debris.debrisclient.config.gui;

import com.github.debris.debrisclient.DebrisClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

public class DCGuiFactory extends DefaultGuiFactory {
    public DCGuiFactory() {
        super(DebrisClient.MOD_ID, DebrisClient.MOD_NAME + " configs");
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        DCConfigScreen gui = new DCConfigScreen();
        gui.setParent(parentScreen);
        return gui;
    }
}
