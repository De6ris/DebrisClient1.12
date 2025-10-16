package com.github.debris.debrisclient.modmixins.iceandfire;

import com.github.alexthe666.iceandfire.api.IEntityEffectCapability;
import com.github.alexthe666.iceandfire.capability.entityeffect.EntityEffectClientHandler;
import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityEffectClientHandler.class, remap = false)
public class EntityEffectClientHandlerMixin {
    @WrapOperation(method = "tickUpdate",
            at = @At(value = "INVOKE",
                    target = "Lcom/github/alexthe666/iceandfire/api/IEntityEffectCapability;isCharmed()Z",
                    remap = false),
            remap = false)
    private static boolean disable(IEntityEffectCapability instance, Operation<Boolean> original) {
        if (DCConfig.DisableSiren.getBooleanValue()) return true;
        return original.call(instance);
    }
}
