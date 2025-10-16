package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.ResumeProgress;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.gui.widgets.WidgetScrollBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiListBase.class, remap = false)
public abstract class GuiListBaseMixin<TYPE, WIDGET extends WidgetListEntryBase<TYPE>, WIDGETLIST extends WidgetListBase<TYPE, WIDGET>> extends GuiBase {
    @Shadow
    protected abstract WIDGETLIST getListWidget();

    @Unique
    private final GuiListBase instance = (GuiListBase) (Object) this;

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void onInit(CallbackInfo ci) {
        if (DCConfig.ProgressResuming.getBooleanValue()) {
            WIDGETLIST listWidget = this.getListWidget();
            if (listWidget != null) {
                WidgetScrollBar scrollbar = listWidget.getScrollbar();
                ResumeProgress.getProgress(this.instance).ifPresent(scrollbar::setValue);
            }
        }
    }
}
