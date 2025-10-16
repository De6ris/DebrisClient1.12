package com.github.debris.debrisclient.mixins.client;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMPMixin {
    @Invoker("syncCurrentPlayItem")
    void invokeSyncCurrentPlayItem();
}
