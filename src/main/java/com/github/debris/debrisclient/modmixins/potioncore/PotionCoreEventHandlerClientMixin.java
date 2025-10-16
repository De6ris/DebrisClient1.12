package com.github.debris.debrisclient.modmixins.potioncore;

import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tmtravlr.potioncore.PotionCoreEventHandlerClient;
import com.tmtravlr.potioncore.potion.PotionPerplexity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PotionCoreEventHandlerClient.class, remap = false)
public class PotionCoreEventHandlerClientMixin {
    @WrapOperation(method = "onClientTick",
            at = @At(value = "INVOKE",
                    target = "Lcom/tmtravlr/potioncore/potion/PotionPerplexity;isEnabled()Z", remap = false
            ),
            remap = false)
    private static boolean disablePerplexity(PotionPerplexity instance, Operation<Boolean> original) {
        if (DCConfig.DisablePotionCore.getBooleanValue()) return false;
        return original.call(instance);
    }
}
