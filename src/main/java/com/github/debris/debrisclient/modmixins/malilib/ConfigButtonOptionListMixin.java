package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.api.IConfigEnum;
import com.github.debris.debrisclient.util.StringUtil;
import fi.dy.masa.malilib.config.options.IConfigOptionList;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonOptionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConfigButtonOptionList.class, remap = false)
public abstract class ConfigButtonOptionListMixin extends ButtonGeneric {
    @Shadow
    @Final
    private IConfigOptionList config;

    public ConfigButtonOptionListMixin(int x, int y, int width, boolean rightAlign, String translationKey, Object... args) {
        super(x, y, width, rightAlign, translationKey, args);
    }

    @Inject(method = "generateDisplayString", at = @At("RETURN"), remap = false)
    private void onDisplayStringUpdate(CallbackInfoReturnable<String> cir) {
        if (this.config instanceof IConfigEnum) {
            this.setHoverStrings(StringUtil.createOptionListTooltip(this.config));
        }
    }
}
