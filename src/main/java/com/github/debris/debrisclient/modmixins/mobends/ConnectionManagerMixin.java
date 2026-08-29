package com.github.debris.debrisclient.modmixins.mobends;

import com.github.debris.debrisclient.config.DCConfig;
import goblinbob.mobends.core.connection.ConnectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ConnectionManager.class, remap = false)
public class ConnectionManagerMixin {
    @Inject(method = "setup", at = @At("HEAD"), remap = false, cancellable = true)
    private void disable(CallbackInfo ci) {
        if (DCConfig.DisableInternetConnection.getBooleanValue()) ci.cancel();
    }
}
