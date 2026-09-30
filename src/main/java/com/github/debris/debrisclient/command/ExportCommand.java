package com.github.debris.debrisclient.command;

import com.github.debris.debrisclient.feat.FileExport;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends AbstractCommand {
    private static final String LANG = "lang";
    private static final String BIOME_NOT_TRANSLATED = "biome_not_translated";

    public ExportCommand() {
        super("dcexport");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 1 && LANG.equals(args[0])) {
            FileExport.exportLang();
        }
        if (args.length == 1 && BIOME_NOT_TRANSLATED.equals(args[0])) {
            FileExport.exportNotTranslatedBiome();
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(
                    args,
                    LANG,
                    BIOME_NOT_TRANSLATED
            );
        }
        return Collections.emptyList();
    }
}
