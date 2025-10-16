package com.github.debris.debrisclient.modmixins.forgottenitems;

import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tschipp.forgottenitems.items.ItemCraftingRune;

@Mixin(value = ItemCraftingRune.class, remap = false)
public class ItemCraftingRuneMixin {// overwritten by Localizator mod, so obsolete this
    @WrapOperation(method = "addInformation", remap = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isCreative()Z"))
    private boolean wrap(EntityPlayer instance, Operation<Boolean> original) {
        if (DCConfig.RuneTweak.getBooleanValue()) {
            return true;
        }
        return original.call(instance);
    }
}
