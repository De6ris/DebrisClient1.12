package com.github.debris.debrisclient.event.malilib;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.config.Callbacks;
import com.github.debris.debrisclient.config.DCConfig;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.event.WorldLoadHandler;
import fi.dy.masa.malilib.hotkeys.IInputManager;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.minecraft.client.Minecraft;

public class InitListener implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(DebrisClient.MOD_ID, DCConfig.getInstance());

        InputListener inputListener = InputListener.Instance;
        InputEventHandler.getKeybindManager().registerKeybindProvider(inputListener);
        IInputManager inputManager = InputEventHandler.getInputManager();
        inputManager.registerMouseInputHandler(inputListener);
        inputManager.registerKeyboardInputHandler(inputListener);

        TickHandler.getInstance().registerClientTickHandler(new TickListener());
        WorldLoadHandler.getInstance().registerWorldLoadPostHandler(new WorldLoadListener());

        Callbacks.init(Minecraft.getMinecraft());
    }
}
