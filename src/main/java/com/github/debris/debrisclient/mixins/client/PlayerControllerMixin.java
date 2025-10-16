package com.github.debris.debrisclient.mixins.client;

import com.github.debris.debrisclient.inventory.feat.BetterQuickMoving;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMixin {
    @Inject(method = "windowClick", at = @At("HEAD"))
    private void onClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (type == ClickType.QUICK_MOVE) {
            BetterQuickMoving.run(slotId, mouseButton);
        }
    }
}
