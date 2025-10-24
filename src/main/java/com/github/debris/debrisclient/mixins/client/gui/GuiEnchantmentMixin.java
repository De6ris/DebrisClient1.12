package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.feat.enchant.preview.EnchantPreview;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEnchantment.class)
public abstract class GuiEnchantmentMixin extends GuiContainer {
    public GuiEnchantmentMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onRender(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        EnchantPreview.render(this);
    }

    @SuppressWarnings("RedundantIfStatement")
    @WrapWithCondition(
            method = "drawGuiContainerBackgroundLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawSplitString(Ljava/lang/String;IIII)V"),
            require = 0// crash with old versions of SME
    )
    private boolean prevent(FontRenderer instance, String str, int x, int y, int wrapWidth, int textColor) {
        if (EnchantPreview.isCracked()) return false;
        return true;
    }
}
