package com.github.debris.debrisclient.modmixins.xray;

import com.github.debris.debrisclient.config.DCConfig;
import com.xray.gui.manage.GuiAddBlock;
import com.xray.gui.utils.GuiSlider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = GuiAddBlock.class, remap = false)
public class GuiAddBlockMixin extends GuiScreen {
    @Shadow
    private GuiSlider redSlider;
    @Shadow
    private IBlockState state;
    @Shadow
    private GuiSlider greenSlider;
    @Shadow
    private GuiSlider blueSlider;

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void onInitGui(CallbackInfo ci) {
        if (!DCConfig.XRayAutoColorSelection.getBooleanValue()) return;
        if (this.state == null) return;
        Color color = new Color(this.state.getMapColor(this.mc.world, BlockPos.ORIGIN).colorValue);
        this.redSlider.sliderValue = color.getRed() / 255.0F;
        this.greenSlider.sliderValue = color.getGreen() / 255.0F;
        this.blueSlider.sliderValue = color.getBlue() / 255.0F;
    }
}
