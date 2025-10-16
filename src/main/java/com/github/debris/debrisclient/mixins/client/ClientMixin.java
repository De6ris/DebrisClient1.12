package com.github.debris.debrisclient.mixins.client;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.IMBlocker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class ClientMixin {
    @Inject(method = "isReducedDebug", at = @At("HEAD"), cancellable = true)
    public void noReducedDebugInfo(CallbackInfoReturnable<Boolean> cir) {
        if (DCConfig.NoReducedDebugInfo.getBooleanValue()) cir.setReturnValue(false);
    }

    @Inject(method = "displayGuiScreen", at = @At(value = "RETURN", ordinal = 1))
    private void onNewScreen(GuiScreen guiScreenIn, CallbackInfo ci) {
        IMBlocker.onNewScreen(guiScreenIn);
    }
}
