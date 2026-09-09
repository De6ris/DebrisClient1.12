package com.github.debris.debrisclient.modmixins.xat;

import com.github.debris.debrisclient.config.DCConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xzeroair.trinkets.vip.VIPHandler;

@Mixin(value = VIPHandler.class, remap = false)
public abstract class VIPHandlerMixin {
    @Inject(method = "popVIPList", at = @At("HEAD"), remap = false, cancellable = true)
    private static void disable(CallbackInfo ci) {
        if (DCConfig.DisableInternetConnection.getBooleanValue()) ci.cancel();
    }// static to match pre 0.33
}
