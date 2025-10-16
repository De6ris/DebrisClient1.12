package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiRepair;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRepair.class)
public class GuiRepairMixin {
    @ModifyExpressionValue(method = "drawGuiContainerForegroundLayer",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerCapabilities;isCreativeMode:Z", opcode = Opcodes.GETFIELD))
    private boolean levelView(boolean original) {
        if (DCConfig.AnvilLevelView.getBooleanValue()) return true;
        return original;
    }
}
