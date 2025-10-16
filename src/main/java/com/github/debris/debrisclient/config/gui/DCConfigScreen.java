package com.github.debris.debrisclient.config.gui;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.ConfigGuiTabBase;
import fi.dy.masa.malilib.config.options.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;

import java.util.ArrayList;
import java.util.List;

public class DCConfigScreen extends GuiConfigsBase {
    private static final ConfigGuiTabBase VALUE = new ConfigGuiTabBase("值", 100, false, DCConfig.VALUE);
    private static final ConfigGuiTabBase COMPAT = new ConfigGuiTabBase("兼容", 100, false, buildCompat());
    private static final ConfigGuiTabBase LIST = new ConfigGuiTabBase("列表", 100, false, DCConfig.LIST);
    private static final ConfigGuiTabBase HOTKEY = new ConfigGuiTabBase("热键", 204, true, DCConfig.HOTKEY);
    private static final ConfigGuiTabBase YEETS = new ConfigGuiTabBase("禁用", 100, false, DCConfig.YEETS);

    private static final ImmutableList<IConfigGuiTab> TABS = ImmutableList.of(
            VALUE,
            COMPAT,
            LIST,
            HOTKEY,
            YEETS
    );

    private static IConfigGuiTab tab = VALUE;

    public DCConfigScreen() {
        super(10, 50, DebrisClient.MOD_ID, null, TABS, DebrisClient.MOD_NAME + " configs");
    }

    @Override
    public IConfigGuiTab getCurrentTab() {
        return tab;
    }

    @Override
    public void setCurrentTab(IConfigGuiTab tab) {
        DCConfigScreen.tab = tab;
    }

    /**
     * Hide those not loaded
     */
    private static ImmutableList<IConfigBase> buildCompat() {
        List<IConfigBase> mutable = new ArrayList<>(DCConfig.COMPAT);
        if (!ModReference.hasMod(ModReference.FORGOTTENITEMS)) mutable.remove(DCConfig.RuneTweak);
        if (!ModReference.hasMod(ModReference.XRAY)) mutable.remove(DCConfig.XRayAutoColorSelection);
        if (!ModReference.hasMod(ModReference.WAYSTONES)) mutable.remove(DCConfig.WayStoneTweak);
        if (!ModReference.hasMod(ModReference.QUALITYTOOLS) || !ModReference.hasMod(ModReference.BOUNTIFULBAUBLES)) {
            mutable.remove(DCConfig.AutoReforging);
            mutable.remove(DCConfig.ReforgingLevel);
            mutable.remove(DCConfig.ReforgingWhiteList);
        }
        if (!ModReference.hasMod(ModReference.INVTWEAKS)) mutable.remove(DCConfig.DisableSortingOutOfGUI);
        return ImmutableList.copyOf(mutable);
    }
}
