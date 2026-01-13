package com.github.debris.debrisclient.modmixins.qualitytools;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.gui.button.GuiBetterButton;
import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import com.google.common.collect.ImmutableList;
import com.tmtravlr.qualitytools.reforging.GuiReforgingStation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiReforgingStation.class, remap = false)
public abstract class GuiReforgingStationMixin extends GuiContainer {
    @Unique
    private GuiButton autoButton;

    public GuiReforgingStationMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void addAutoReforgingButton(CallbackInfo ci) {
        if (DCConfig.AutoReforging.getBooleanValue()) {
            this.autoButton = new GuiBetterButton(
                    AutoReforging.BUTTON_ID,
                    this.guiLeft + this.xSize - 40,
                    this.guiTop + 60,
                    30,
                    16,
                    "自动"
            );
            this.addButton(this.autoButton);
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), remap = true, cancellable = true)
    private void autoReforge(GuiButton button, CallbackInfo ci) {
        if (button.id == AutoReforging.BUTTON_ID) {
            AutoReforging.tryAutoReforging(this.mc);
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void renderTooltip(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        // is "mouse over" in fact
        if (this.autoButton.mousePressed(this.mc, mouseX, mouseY)) {
            this.drawHoveringText(
                    ImmutableList.of(
                            "右键切换目标颜色:",
                            DCConfig.ReforgingLevel.getOptionListValue().getDisplayName()
                    ),
                    mouseX,
                    mouseY
            );
        }
    }
}
