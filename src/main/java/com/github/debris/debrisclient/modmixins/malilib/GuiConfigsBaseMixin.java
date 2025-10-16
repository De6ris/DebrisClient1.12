package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.ResumeProgress;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetScrollBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiConfigsBase.class, remap = false)
public abstract class GuiConfigsBaseMixin extends GuiListBase<GuiConfigsBase.ConfigOptionWrapper, WidgetConfigOption, WidgetListConfigOptions> {
    @Unique
    private final GuiConfigsBase instance = (GuiConfigsBase) (Object) this;

    protected GuiConfigsBaseMixin(int listX, int listY) {
        super(listX, listY);
    }

    @Inject(method = "onGuiClosed", at = @At("RETURN"), remap = true)
    private void onRemoved(CallbackInfo ci) {
        if (DCConfig.ProgressResuming.getBooleanValue()) {
            WidgetListConfigOptions listWidget = this.getListWidget();
            if (listWidget != null) {
                WidgetScrollBar scrollbar = listWidget.getScrollbar();
                ResumeProgress.saveProgress(this.instance, scrollbar.getValue());
            }
        }
    }
}
