package com.github.debris.debrisclient.mixins.client.world;

import com.github.debris.debrisclient.event.Hooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldClient.class)
public abstract class WorldClientMixin extends World {
    @Shadow
    @Final
    private Minecraft mc;

    protected WorldClientMixin(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Inject(method = "spawnEntity", at = @At("RETURN"))
    private void onEntitySpawn(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        Hooks.onEntitySpawn(this.mc, this, entityIn);
    }
}
