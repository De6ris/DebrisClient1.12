package com.github.debris.debrisclient.command;

import net.minecraftforge.client.ClientCommandHandler;

public class ClientCommands {
    public static void register() {
        ClientCommandHandler instance = ClientCommandHandler.instance;
        instance.registerCommand(new ExportCommand());
        instance.registerCommand(new EnchantPlanCommand());
        instance.registerCommand(new LookCommand());
        instance.registerCommand(new GlowCommand());
    }
}
