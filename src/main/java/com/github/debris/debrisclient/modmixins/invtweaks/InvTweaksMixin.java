package com.github.debris.debrisclient.modmixins.invtweaks;

import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import invtweaks.InvTweaks;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = InvTweaks.class, remap = false)
public class InvTweaksMixin {
    @WrapWithCondition(method = "onSortingKeyPressed",
            at = @At(value = "INVOKE",
                    target = "Linvtweaks/InvTweaks;handleSorting(Lnet/minecraft/client/gui/GuiScreen;)V",
                    remap = false
            ),
            remap = false)
    private boolean onSort(InvTweaks instance, GuiScreen screen) {
        if (DCConfig.DisableSortingOutOfGUI.getBooleanValue() && screen == null) return false;
        return true;
    }
}
