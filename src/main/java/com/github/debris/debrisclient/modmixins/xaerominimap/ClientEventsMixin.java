package com.github.debris.debrisclient.modmixins.xaerominimap;

import com.github.debris.debrisclient.feat.FreeCam;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.events.ClientEvents;

@Mixin(value = ClientEvents.class, remap = false)
public class ClientEventsMixin {
    @Inject(method = "handleRenderGameOverlayEventPreAll", at = @At("HEAD"), remap = false)
    private void preRender(float partialTicks, CallbackInfo ci) {
        FreeCam.preRender(Minecraft.getMinecraft());
    }

    @Inject(method = "handleRenderGameOverlayEventPost", at = @At("RETURN"), remap = false)
    private void postRender(RenderGameOverlayEvent.Post event, CallbackInfo ci) {
        FreeCam.postRender(Minecraft.getMinecraft());
    }
}
