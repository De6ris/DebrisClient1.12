package com.github.debris.debrisclient.config.gui;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.ConfigGuiTabBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;

public class DCConfigScreen extends GuiConfigsBase {
    private static final ConfigGuiTabBase VALUE = new ConfigGuiTabBase("值", 100, false, HideConfig.filter(DCConfig.VALUE));
    private static final ConfigGuiTabBase INTEGRATION = new ConfigGuiTabBase("联动", 100, false, HideConfig.filter(DCConfig.INTEGRATION));
    private static final ConfigGuiTabBase LIST = new ConfigGuiTabBase("列表", 100, false, HideConfig.filter(DCConfig.LIST));
    private static final ConfigGuiTabBase HOTKEY = new ConfigGuiTabBase("热键", 204, true, HideConfig.filter(DCConfig.HOTKEY));
    private static final ConfigGuiTabBase YEETS = new ConfigGuiTabBase("禁用", 100, false, HideConfig.filter(DCConfig.YEETS));
    private static final ConfigGuiTabBase GLOWS = new ConfigGuiTabBase("发光", 100, false, HideConfig.filter(DCConfig.GLOWS));

    private static final ImmutableList<IConfigGuiTab> TABS = ImmutableList.of(
            VALUE,
            INTEGRATION,
            LIST,
            HOTKEY,
            YEETS,
            GLOWS
    );

    private static IConfigGuiTab tab = VALUE;

    public DCConfigScreen() {
        super(10, 50, DebrisClient.MOD_ID, null, TABS, DebrisClient.MOD_NAME + " configs");
        AutoReforging.makeConfigComments();
    }

    @Override
    public IConfigGuiTab getCurrentTab() {
        return tab;
    }

    @Override
    public void setCurrentTab(IConfigGuiTab tab) {
        DCConfigScreen.tab = tab;
    }
}
