package com.github.debris.debrisclient.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class AbstractCommand extends CommandBase {
    private final String name;

    protected AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + this.name + ".usage";
    }
}
