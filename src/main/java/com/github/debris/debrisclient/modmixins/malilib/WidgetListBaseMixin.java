package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.PinYinSupport;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WidgetListBase.class, remap = false)
public class WidgetListBaseMixin<TYPE, WIDGET extends WidgetListEntryBase<TYPE>> {
    @ModifyExpressionValue(method = "matchesFilter(Ljava/lang/String;Ljava/lang/String;)Z", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(Ljava/lang/String;)I"))
    private int pinYinSearch(int original, @Local(argsOnly = true, ordinal = 0) String entryString, @Local(argsOnly = true, ordinal = 1) String filterText) {
        if (!DCConfig.PinYinSearch.getBooleanValue()) return original;
        if (original >= 0) return original;
        if (PinYinSupport.available() && PinYinSupport.matchesFilter(entryString, filterText)) return 0;
        return original;
    }
}
