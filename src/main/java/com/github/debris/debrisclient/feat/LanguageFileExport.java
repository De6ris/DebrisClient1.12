package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.Platform;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.ChatUtil;
import com.github.debris.debrisclient.util.ComponentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LanguageFileExport {
    private static final Logger LOGGER = LogManager.getLogger(LanguageFileExport.class);

    private static final Path OUTPUT_DIR = Platform.getRootDir().resolve("dumps").resolve("universal.lang");

    public static boolean run(Minecraft client) {
        Locale locale = AccessorUtil.getCurrentLocale();
        Map<String, String> properties = AccessorUtil.getProperties(locale);
        ChatUtil.setActionBar("正在导出语言文件");
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> export(properties));
        future.whenComplete((b, t) -> {
            if (b == null) b = false;
            ChatUtil.setActionBar(new TextComponentString("导出结果: ").appendSibling(ComponentUtil.successOrFail(b)));
        });
        return true;
    }

    private static boolean export(Map<String, String> properties) {
        OUTPUT_DIR.getParent().toFile().mkdirs();
        try (BufferedWriter writer = Files.newBufferedWriter(OUTPUT_DIR, StandardOpenOption.CREATE)) {
            properties.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(x -> {
                try {
                    writer.write(x.getKey() + "=" + transform(x.getValue()));
                    writer.newLine();
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            });
            return true;
        } catch (IOException e) {
            LOGGER.warn(e);
            return false;
        }
    }

    private static String transform(String s) {
        return s.replace("\n", "\\n");
    }
}
