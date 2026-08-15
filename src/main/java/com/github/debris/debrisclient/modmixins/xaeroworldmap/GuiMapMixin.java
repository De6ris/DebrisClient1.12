package com.github.debris.debrisclient.modmixins.xaeroworldmap;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.FreeCam;
import com.github.debris.debrisclient.util.StringUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.Biome;
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

    @WrapOperation(method = "drawScreen", remap = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getBiomeName()Ljava/lang/String;", remap = true))
    private String translateBiome(Biome instance, Operation<String> original) {
        if (DCConfig.XaeroTranslateBiome.getBooleanValue()) {
            return StringUtil.translateBiome(instance);
        }
        return original.call(instance);
    }
}
