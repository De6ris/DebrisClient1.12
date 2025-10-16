package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.util.CullingUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class ParticleMixin {
    @Shadow
    protected TextureAtlasSprite particleTexture;

    @Inject(method = "renderParticle", at = @At("HEAD"), cancellable = true)
    private void cullParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (this.particleTexture == null) return;
        if (CullingUtil.shouldCullParticle(this.particleTexture.getIconName())) ci.cancel();
    }
}
