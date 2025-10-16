package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import com.tmtravlr.qualitytools.reforging.ContainerReforgingStation;
import com.tmtravlr.qualitytools.reforging.GuiReforgingStation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

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
}
