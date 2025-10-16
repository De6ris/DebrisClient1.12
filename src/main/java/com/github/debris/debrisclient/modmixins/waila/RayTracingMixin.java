package com.github.debris.debrisclient.modmixins.waila;

import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mcp.mobius.waila.overlay.RayTracing;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RayTracing.class, remap = false)
public class RayTracingMixin {
    @WrapOperation(method = "fire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;", remap = true))
    private Entity freeCamCompat(Minecraft instance, Operation<Entity> original) {
        if (FreeCam.isActive()) return FreeCam.getFreeCamera();
        return original.call(instance);
    }
}
