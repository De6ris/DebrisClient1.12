package com.github.debris.debrisclient.modmixins.potioncore;

import com.github.debris.debrisclient.config.DCConfig;
import com.tmtravlr.potioncore.potion.PotionSpin;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PotionSpin.class, remap = false)
public class PotionSpinMixin {
    @Inject(method = "performEffect", at = @At("HEAD"), cancellable = true, remap = true)
    private void disable(EntityLivingBase entity, int amplifier, CallbackInfo ci) {
        if (DCConfig.DisablePotionCore.getBooleanValue()) ci.cancel();
    }
}
