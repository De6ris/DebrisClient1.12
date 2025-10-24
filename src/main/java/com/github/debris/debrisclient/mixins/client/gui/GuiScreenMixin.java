package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.event.Hooks;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void onMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        Hooks.onMouseClicked((GuiScreen) (Object) this, mouseX, mouseY, mouseButton);
    }
}
