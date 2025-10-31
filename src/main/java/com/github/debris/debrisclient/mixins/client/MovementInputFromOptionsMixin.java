package com.github.debris.debrisclient.mixins.client;

import com.github.debris.debrisclient.feat.FreeCam;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin {
    @Inject(method = "updatePlayerMoveState", at = @At("HEAD"), cancellable = true)
    private void cancel(CallbackInfo ci) {
        if (FreeCam.isActive()) ci.cancel();
    }
}
