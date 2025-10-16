package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.util.CullingUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void cullEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
        if (CullingUtil.shouldCullEntity(entityIn)) ci.cancel();
    }
}
