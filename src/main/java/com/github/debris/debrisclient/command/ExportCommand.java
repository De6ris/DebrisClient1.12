package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.feat.LanguageFileExport;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends AbstractCommand {
    private static final String LANG = "lang";

    public ExportCommand() {
        super("dcexport");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 1 && LANG.equals(args[0])) {
            LanguageFileExport.run(Minecraft.getMinecraft());
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return Collections.singletonList(LANG);
        }
        return Collections.emptyList();
    }
}
