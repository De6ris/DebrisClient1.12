package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.feat.PlayerRotation;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class LookCommand extends AbstractCommand {
    public LookCommand() {
        super("dclook");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 3) {
            Minecraft client = Minecraft.getMinecraft();
            if (Predicates.notInGame(client)) return;
            BlockPos pos = CommandBase.parseBlockPos(sender, args, 0, true);
            PlayerRotation.lookAtBlock(client.player, pos);
        }
    }
}
