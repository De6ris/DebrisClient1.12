package com.github.debris.debrisclient.modmixins.xaeroworldmap;

import com.github.debris.debrisclient.feat.FreeCam;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.events.ClientEvents;

@Mixin(value = ClientEvents.class, remap = false)
public abstract class ClientEventsMixin {
    // these make no difference
    @Inject(
            method = "renderTick(Lnet/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent;)V",
            at = @At("HEAD"),
            remap = false
    )
    private void preRender(TickEvent.RenderTickEvent event, CallbackInfo ci) {
        FreeCam.preRender(Minecraft.getMinecraft());
    }

    @Inject(
            method = "renderTick(Lnet/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent;)V",
            at = @At("RETURN"),
            remap = false
    )
    private void postRender(TickEvent.RenderTickEvent event, CallbackInfo ci) {
        FreeCam.postRender(Minecraft.getMinecraft());
    }
}
