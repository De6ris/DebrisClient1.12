package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import com.tmtravlr.qualitytools.config.ConfigLoader;
import com.tmtravlr.qualitytools.reforging.ContainerReforgingStation;
import com.tmtravlr.qualitytools.reforging.GuiReforgingStation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.stream.Stream;

public class QualityToolsAccess {
    public static boolean isReforgingGUI(GuiScreen screen) {
        return screen instanceof GuiReforgingStation;
    }

    public static boolean isReforgingContainer(Container container) {
        return container instanceof ContainerReforgingStation;
    }

    public static boolean canReforge(GuiScreen screen) {
        if (!isReforgingGUI(screen)) return false;
        ItemStack tool = EnumSection.QualityToolsReforgingTool.get().getFirstSlot().getStack();
        ItemStack material = EnumSection.QualityToolsReforgingMaterial.get().getFirstSlot().getStack();
        return QualityToolsHelper.canReforgeWith(tool, material);
    }

    public static boolean ready() {
        return !ConfigLoader.qualityTypes.isEmpty();
    }

    public static Stream<String> streamQualities() {
        return ConfigLoader.qualityTypes.values().stream()
                .flatMap(x -> Arrays.stream(x.qualities))
                .map(x -> x.name);
    }
}
