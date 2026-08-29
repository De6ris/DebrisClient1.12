package com.github.debris.debrisclient.mixins.client;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.BreakingCooldownMode;
import com.github.debris.debrisclient.inventory.feat.BetterQuickMoving;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMixin {
    @Shadow
    private int blockHitDelay;

    @Inject(method = "windowClick", at = @At("HEAD"))
    private void onClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (type == ClickType.QUICK_MOVE) {
            BetterQuickMoving.run(slotId, mouseButton);
        }
    }

    @Inject(method = "onPlayerDamageBlock",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 2,
                    shift = At.Shift.AFTER
            )
    )
    private void cullCooldown(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (DCConfig.BreakingCooldown.getEnumValue() == BreakingCooldownMode.DISABLE) {
            this.blockHitDelay = 0;
        }
    }

    @Inject(method = "clickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;onPlayerDestroyBlock(Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private void forceCooldown(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        if (DCConfig.BreakingCooldown.getEnumValue() == BreakingCooldownMode.FORCE) {
            this.blockHitDelay = 5;
        }
    }
}
