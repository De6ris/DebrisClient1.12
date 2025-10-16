package com.github.debris.debrisclient.modmixins.enhancedvisuals;

import com.github.debris.debrisclient.config.DCConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.creative.enhancedvisuals.client.EVClient;

@Mixin(value = EVClient.class, remap = false)
public class EVClientMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), remap = false, cancellable = true)
    private static void disable(CallbackInfoReturnable<Boolean> cir) {
        if (DCConfig.DisableEnhancedVisuals.getBooleanValue()) cir.setReturnValue(false);
    }
}
