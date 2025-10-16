package com.github.debris.debrisclient.modmixins.xaeroworldmap;

import com.github.debris.debrisclient.feat.FreeCam;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.MapProcessor;
import xaero.map.gui.GuiMap;

@Mixin(value = GuiMap.class, remap = false)
public class GuiMapMixin {
    @Shadow
    private Entity player;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void freeCamCompat(GuiScreen parent, GuiScreen escape, MapProcessor mapProcessor, Entity player, CallbackInfo ci) {
        if (FreeCam.isActive()) this.player = FreeCam.getFreeCamera();
    }
}
