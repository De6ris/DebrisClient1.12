package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.feat.IMBlocker;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public class GuiTextFieldMixin {
    @Inject(method = "setFocused", at = @At("RETURN"))
    private void inputMethod(boolean isFocusedIn, CallbackInfo ci) {
        IMBlocker.onTextFieldFocus(isFocusedIn);
    }
}
