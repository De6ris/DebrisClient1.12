package com.github.debris.debrisclient.modmixins.malilib;

import com.github.debris.debrisclient.config.DCConfig;
import fi.dy.masa.malilib.MaLiLib;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MaLiLib.class, remap = false)
public class DisableSignatureWarningMixin {
    @Inject(method = "onFingerPrintViolation", at = @At("HEAD"), cancellable = true, remap = false)
    public void mute(FMLFingerprintViolationEvent event, CallbackInfo ci) {
        if (DCConfig.DisableSignatureWarning.getBooleanValue()) ci.cancel();
    }
}
