package com.github.debris.debrisclient.modmixins.craftingtweaks;

import com.github.debris.debrisclient.event.Hooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.craftingtweaks.client.ClientProvider;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientProvider.class, remap = false)
public class ClientProviderMixin {
    @WrapOperation(
            method = "refillLastCrafted",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;areItemStackTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z",
                    remap = true
            ),
            remap = false
    )
    private boolean makeLoose(ItemStack stackA, ItemStack stackB, Operation<Boolean> original) {
        if (Hooks.nbtTakenAsEqual(stackA, stackB)) return true;
        return original.call(stackA, stackB);
    }
}
