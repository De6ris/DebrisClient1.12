package com.github.debris.debrisclient.modmixins.xaerominimap;

import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.minimap.render.MinimapRenderer;

@Mixin(value = MinimapRenderer.class, remap = false)
public class MinimapRendererMixin {
    @WrapOperation(method = "renderMinimap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;",
                    remap = true
            ),
            remap = false
    )
    private Entity freeCamCompat(Minecraft instance, Operation<Entity> original) {
        if (FreeCam.isActive()) return FreeCam.getFreeCamera();
        return original.call(instance);
    }

    @Inject(method = "getEntityYaw", at = @At("HEAD"), remap = false, cancellable = true)
    private void freeCamCompat(Entity e, float partial, CallbackInfoReturnable<Float> cir) {
        if (FreeCam.isActive() && FreeCam.getFreeCamera() == e) cir.setReturnValue(e.rotationYaw);
    }
}
