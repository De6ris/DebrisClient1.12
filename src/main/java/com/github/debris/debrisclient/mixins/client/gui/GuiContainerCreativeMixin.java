package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.inventory.section.SectionHandler;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainerCreative.class, priority = 999)
public abstract class GuiContainerCreativeMixin extends InventoryEffectRenderer {
    public GuiContainerCreativeMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "setCurrentCreativeTab", at = @At("RETURN"))
    private void update(CreativeTabs par1CreativeTabs, CallbackInfo ci) {
        SectionHandler.updateSection(this);
    }
}
