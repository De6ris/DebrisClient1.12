package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.StringUtil;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = WidgetListConfigOptions.class, remap = false)
public class WidgetListConfigOptionsMixin {
    @Inject(method = "getEntryStringsForFilter(Lfi/dy/masa/malilib/gui/GuiConfigsBase$ConfigOptionWrapper;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private void commentSearch(GuiConfigsBase.ConfigOptionWrapper entry, CallbackInfoReturnable<List<String>> cir) {
        if (DCConfig.CommentSearch.getBooleanValue()) {
            cir.setReturnValue(StringUtil.commentSearch(entry.getConfig()));
        }
    }
}
