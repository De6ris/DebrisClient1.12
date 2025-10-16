package com.github.debris.debrisclient.mixins.client;

import com.github.debris.debrisclient.config.DCConfig;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Locale.class)
public class LocaleMixin {
    @Inject(method = "checkUnicode", at = @At("HEAD"), cancellable = true)
    private void forceASCII(CallbackInfo ci) {
        if (DCConfig.ForceASCII.getBooleanValue()) {
            ci.cancel();
        }
    }
}
