package com.github.debris.debrisclient.modmixins.bountifulbaubles;

import com.github.debris.debrisclient.gui.button.GuiBetterButton;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import cursedflames.bountifulbaubles.block.GuiReforger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiReforger.class, remap = false)
public abstract class GuiReforgerMixin extends GuiContainer {
    public GuiReforgerMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void addAutoReforgingButton(CallbackInfo ci) {
        if (DCConfig.AutoReforging.getBooleanValue()) {
            this.addButton(new GuiBetterButton(AutoReforging.BUTTON_ID,
                    this.guiLeft + 10,
                    this.guiTop + 60,
                    30,
                    16,
                    "自动"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), remap = true, cancellable = true)
    private void autoReforge(GuiButton button, CallbackInfo ci) {
        if (button.id == AutoReforging.BUTTON_ID) {
            AutoReforging.tryAutoReforging(this.mc);
            ci.cancel();
        }
    }

}
