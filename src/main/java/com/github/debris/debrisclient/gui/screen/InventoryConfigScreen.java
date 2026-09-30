package com.github.debris.debrisclient.gui.screen;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.config.InventoryConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.ConfigGuiTabBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.Nullable;

public class InventoryConfigScreen extends ConfigScreen {
    private static final ConfigGuiTabBase VALUE = new ConfigGuiTabBase("值", 100, false, InventoryConfig.VALUE);
    private static final ConfigGuiTabBase LIST = new ConfigGuiTabBase("列表", 100, false, InventoryConfig.LIST);
    private static final ConfigGuiTabBase HOTKEY = new ConfigGuiTabBase("热键", 204, true, InventoryConfig.HOTKEY);
    private static final ImmutableList<IConfigGuiTab> TABS = ImmutableList.of(
            VALUE,
            LIST,
            HOTKEY
    );

    public InventoryConfigScreen(@Nullable GuiScreen parent) {
        super(InventoryConfig.getInstance(), parent, TABS, new TextComponentString(DebrisClient.MOD_NAME + " inventory configs"));
        this.tab = VALUE;
    }
}
