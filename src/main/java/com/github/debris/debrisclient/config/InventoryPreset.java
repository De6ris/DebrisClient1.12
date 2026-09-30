package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.gui.screen.InventoryConfigScreen;
import com.github.debris.debrisclient.inventory.feat.WheelMovingMode;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.gui.GuiScreen;

public class InventoryPreset {
    public static void switchPreset(ConfigBoolean config) {
        if (config.getBooleanValue()) {
            InventoryConfig.HoldInventoryMoving.setBooleanValue(true);
            InventoryConfig.BetterQuickMoving.setBooleanValue(true);
            InventoryConfig.WheelMoving.setEnumValue(WheelMovingMode.DEFAULT);
            InventoryConfig.BetterSwapHandsKey.setBooleanValue(true);
            InventoryConfig.BetterHoldingItem.setBooleanValue(true);


            InventoryConfig.SortInventory.getKeybind().setValueFromString("R");
            InventoryConfig.AutoContainerOperation.getKeybind().setValueFromString("SPACE");
            InventoryConfig.ModifierSpreadItem.getKeybind().setValueFromString("LMENU");
            InventoryConfig.ModifierMoveSame.getKeybind().setValueFromString("LCONTROL");
            InventoryConfig.ModifierMoveStack.getKeybind().setValueFromString("LSHIFT");
            InventoryConfig.ModifierMoveAll.getKeybind().setValueFromString("SPACE");
            InventoryConfig.ThrowSection.getKeybind().setValueFromString("SPACE,Q");
            InventoryConfig.ThrowSame.getKeybind().setValueFromString("LSHIFT,Q");
        } else {
            InventoryConfig.HoldInventoryMoving.setBooleanValue(false);
            InventoryConfig.BetterQuickMoving.setBooleanValue(false);
            InventoryConfig.WheelMoving.setEnumValue(WheelMovingMode.NONE);
            InventoryConfig.BetterSwapHandsKey.setBooleanValue(false);
            InventoryConfig.BetterHoldingItem.setBooleanValue(false);

            InventoryConfig.SortInventory.resetToDefault();
            InventoryConfig.AutoContainerOperation.resetToDefault();
            InventoryConfig.ModifierSpreadItem.resetToDefault();
            InventoryConfig.ModifierMoveSame.resetToDefault();
            InventoryConfig.ModifierMoveStack.resetToDefault();
            InventoryConfig.ModifierMoveAll.resetToDefault();
            InventoryConfig.ThrowSection.resetToDefault();
            InventoryConfig.ThrowSame.resetToDefault();
        }
        GuiScreen screen = GuiUtils.getCurrentScreen();
        if (screen instanceof InventoryConfigScreen) {
            ((InventoryConfigScreen) screen).reCreateConfigWidgets();
            //noinspection DataFlowIssue
            ((InventoryConfigScreen) screen).getConfigsListWidget().refreshEntries();
        }
    }
}
