package com.github.debris.debrisclient.mixins.client.entity;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.section.SectionHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayer {
    public ClientPlayerMixin(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_, CallbackInfo ci) {
        SectionHandler.onClientPlayerInit(this.inventoryContainer);
    }

    @WrapOperation(method = "onLivingUpdate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z",
                    ordinal = 0))
    public boolean disableNausea(EntityPlayerSP instance, Potion potion, Operation<Boolean> original) {
        if (DCConfig.DisableNausea.getBooleanValue()) return false;
        return original.call(instance, potion);
    }
}