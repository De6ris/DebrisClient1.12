package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {
    @WrapOperation(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isUser()Z"))
    private boolean freeCamCompat(AbstractClientPlayer instance, Operation<Boolean> original) {
        if (FreeCam.isActive() && instance == Minecraft.getMinecraft().player) {
            return false;
        }
        return original.call(instance);
    }
}