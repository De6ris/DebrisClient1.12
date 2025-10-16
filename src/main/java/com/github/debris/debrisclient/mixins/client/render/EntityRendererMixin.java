package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @WrapOperation(method = "setupCameraTransform", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    private boolean disableNausea(EntityPlayerSP instance, Potion potion, Operation<Boolean> original) {
        if (DCConfig.DisableNausea.getBooleanValue()) return false;
        return original.call(instance, potion);
    }

    @WrapOperation(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z", ordinal = 0))
    private boolean disableBlindness(EntityLivingBase instance, Potion potionIn, Operation<Boolean> original) {
        if (DCConfig.DisableBlindness.getBooleanValue()) return false;
        return original.call(instance, potionIn);
    }

    @WrapOperation(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z", ordinal = 0))
    private boolean disableBlindness1(EntityLivingBase instance, Potion potionIn, Operation<Boolean> original) {
        if (DCConfig.DisableBlindness.getBooleanValue()) return false;
        return original.call(instance, potionIn);
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;getMouseOver(F)V", shift = At.Shift.AFTER))
    private void preRenderWorld(float partialTicks, long finishTimeNano, CallbackInfo ci) {
        FreeCam.preRenderWorld(this.mc);
    }

    @Inject(method = "renderWorld", at = @At("RETURN"))
    private void postRenderWorld(float partialTicks, long finishTimeNano, CallbackInfo ci) {
        FreeCam.postRenderWorld(this.mc);
    }

    @ModifyReceiver(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V"))
    private EntityPlayerSP makeCameraTurn(EntityPlayerSP instance, float yaw, float pitch) {
        return FreeCam.isActive() ? FreeCam.getFreeCamera() : instance;
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void cullHand(float partialTicks, int pass, CallbackInfo ci) {
        if (FreeCam.isActive()) ci.cancel();
    }

    @WrapOperation(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSpectator()Z"))
    private boolean forceSpec(EntityPlayerSP instance, Operation<Boolean> original) {
        if (FreeCam.isActive()) return true;
        return original.call(instance);
    }
}
