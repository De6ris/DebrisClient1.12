package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.FastWaypoint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMerchant.class)
public abstract class GuiMerchantMixin extends GuiContainer {
    @Shadow
    @Final
    private IMerchant merchant;

    public GuiMerchantMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (DCConfig.FastWaypoint.getBooleanValue()) {
            this.addButton(FastWaypoint.createButton());
        }
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void onClick(GuiButton button, CallbackInfo ci) {
        if (button.id == FastWaypoint.BUTTON_ID) {
            IMerchant merchant = this.merchant;
            String name = FastWaypoint.ofMerchantRecipes(merchant.getRecipes(this.mc.player));
            if (name != null) {
                FastWaypoint.createWayPoint(
                        merchant.getWorld().provider.getDimension(),
                        merchant.getPos(),
                        name
                );
            }
        }
    }
}
