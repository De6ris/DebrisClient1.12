package com.github.debris.debrisclient.gui.screen;

import com.github.debris.debrisclient.config.ConfigHandlerImpl;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigScreen extends GuiConfigsBase {
    @Nullable
    protected IConfigGuiTab tab;

    public ConfigScreen(ConfigHandlerImpl configHandler, @Nullable GuiScreen parent, List<IConfigGuiTab> configTabs, ITextComponent title) {
        super(10, 50, configHandler.getId(), parent, configTabs, "");
        this.title = title.getFormattedText();
    }

    @Override
    public void setCurrentTab(IConfigGuiTab iConfigGuiTab) {
        this.tab = iConfigGuiTab;
    }

    @Override
    public @Nullable IConfigGuiTab getCurrentTab() {
        return this.tab;
    }
}
