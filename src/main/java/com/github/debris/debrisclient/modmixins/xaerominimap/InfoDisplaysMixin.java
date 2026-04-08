package com.github.debris.debrisclient.modmixins.xaerominimap;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.XaeroMiniMapAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.hud.minimap.info.InfoDisplayIO;
import xaero.hud.minimap.info.InfoDisplayManager;
import xaero.hud.minimap.info.InfoDisplays;

@Mixin(value = InfoDisplays.class, remap = false)
public class InfoDisplaysMixin {
    @Shadow(remap = false)
    @Final
    private InfoDisplayManager manager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(InfoDisplayIO io, CallbackInfo ci) {
        if (ModReference.hasMod(ModReference.SERENE_SEASONS)) {
            this.manager.add(XaeroMiniMapAccess.getSubSeasonInfo());
        }
    }
}
