package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.feat.FastWaypoint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.Container;
import net.minecraft.village.MerchantRecipeList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GuiMerchant.class)
public abstract class GuiMerchantMixin extends GuiContainer {
    @Shadow
    @Final
    private IMerchant merchant;

    @Nullable
    @Unique
    private GuiButton fastWaypoint;

    public GuiMerchantMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (FastWaypoint.isActive()) {
            this.fastWaypoint = FastWaypoint.createButtonVillager(this);
            this.fastWaypoint.visible = false;
            this.addButton(this.fastWaypoint);
        }
    }

    @Inject(method = "updateScreen", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (this.fastWaypoint == null) return;
        String name = FastWaypoint.ofMerchantRecipes(this.merchant.getRecipes(this.mc.player));
        this.fastWaypoint.visible = (name != null);
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void onClick(GuiButton button, CallbackInfo ci) {
        if (button.id == FastWaypoint.BUTTON_ID) {
            IMerchant merchant = this.merchant;
            MerchantRecipeList recipes = merchant.getRecipes(this.mc.player);
            String name = FastWaypoint.ofMerchantRecipes(recipes);
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
