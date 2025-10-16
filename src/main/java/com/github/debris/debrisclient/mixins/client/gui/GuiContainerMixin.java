package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.feat.BetterSwapHands;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin extends GuiScreen {
    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;checkHotbarKeys(I)Z"))
    private void onCheckHotbarKeys(char typedChar, int keyCode, CallbackInfo ci) {
        if (DCConfig.BetterSwapHandsKey.getBooleanValue() && this.mc.gameSettings.keyBindSwapHands.getKeyCode() == keyCode) {
            BetterSwapHands.run((GuiContainer) (Object) this);
        }
    }
}
