package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.Platform;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.ChatUtil;
import com.github.debris.debrisclient.util.ComponentUtil;
import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileExport {
    private static final Logger LOGGER = LogManager.getLogger(FileExport.class);

    public static void exportLang() {
        run(
                "universal.lang",
                () -> AccessorUtil.getProperties(AccessorUtil.getCurrentLocale())
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(x -> x.getKey() + "=" + transform(x.getValue())));
    }

    public static void exportNotTranslatedBiome() {
        run(
                "not_translated_biome.lang",
                () -> StreamSupport.stream(
                                Spliterators.spliterator(Biome.REGISTRY.iterator(), 255, 0),
                                false
                        )
                        .map(biome -> {
                            ResourceLocation registryName = biome.getRegistryName();
                            if (registryName != null) {
                                return "biome." + registryName + ".name";
                            } else {
                                return "biome." + biome.getBiomeName() + ".name";
                            }
                        }).filter(key -> !StringUtil.hasTranslationKey(key))
                        .sorted()
                        .map(x -> x + "=")
        );
    }

    public static boolean run(String file, Supplier<Stream<String>> stream) {
        ChatUtil.setActionBar("正在导出");
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> export(
                Platform.getRootDir().resolve("dumps").resolve(file),
                stream.get()
        ));
        future.whenComplete((b, t) -> {
            if (b == null) b = false;
            ChatUtil.setActionBar(new TextComponentString("导出结果: ").appendSibling(ComponentUtil.successOrFail(b)));
        });
        return true;
    }

    private static boolean export(Path path, Stream<String> stream) {
        path.getParent().toFile().mkdirs();
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            stream.forEach(x -> {
                try {
                    writer.write(x);
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
