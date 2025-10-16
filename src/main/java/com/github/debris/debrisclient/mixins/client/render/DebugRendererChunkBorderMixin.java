package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugRendererChunkBorder.class)
public abstract class DebugRendererChunkBorderMixin {
    @WrapOperation(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/entity/EntityPlayerSP;"))
    private EntityPlayerSP useCameraEntity(Minecraft instance, Operation<EntityPlayerSP> original) {
        // Fix the chunk border renderer using the client player instead of the camera entity
        if (FreeCam.isActive()) {
            return FreeCam.getFreeCamera();
        }
        return original.call(instance);
    }
}
