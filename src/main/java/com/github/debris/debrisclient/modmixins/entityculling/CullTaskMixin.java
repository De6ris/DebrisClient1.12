package com.github.debris.debrisclient.modmixins.entityculling;

import com.github.debris.debrisclient.feat.FreeCam;
import dev.tr7zw.entityculling.CullTask;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CullTask.class, remap = false)
public class CullTaskMixin {
    @Inject(method = "getCameraPos", at = @At("HEAD"), cancellable = true, remap = false)
    private void freeCamCompat(CallbackInfoReturnable<Vec3d> cir) {
        if (FreeCam.isActive()) {
            cir.setReturnValue(FreeCam.getFreeCamera().getPositionEyes(1.0F));// the prev pos is always there, so 0.0 won't work
        }
    }
}
