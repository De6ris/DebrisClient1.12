package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.feat.enchant.plan.EnchantPlan;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class EnchantPlanCommand extends AbstractCommand {
    public EnchantPlanCommand() {
        super("dcenchantplan");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0) {
            EnchantPlan.run(Minecraft.getMinecraft());
        }
    }
}
