package com.github.debris.debrisclient.modmixins.xray;

import com.github.debris.debrisclient.config.DCConfig;
import com.xray.gui.manage.GuiAddBlock;
import com.xray.gui.utils.GuiSlider;
import com.xray.reference.block.BlockItem;
import net.minecraft.block.Block;
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
    @Shadow(remap = false)
    private GuiSlider redSlider;
    @Shadow(remap = false)
    private GuiSlider greenSlider;
    @Shadow(remap = false)
    private GuiSlider blueSlider;

    @Shadow(remap = false)
    private BlockItem selectBlock;
    @Shadow(remap = false)
    private IBlockState state;

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void onInitGui(CallbackInfo ci) {
        if (!DCConfig.XRayAutoColorSelection.getBooleanValue()) return;
        IBlockState state = this.state;
        if (state == null) {
            state = Block.getBlockFromItem(this.selectBlock.getItemStack().getItem()).getDefaultState();
        }
        Color color = new Color(state.getMapColor(this.mc.world, BlockPos.ORIGIN).colorValue);
        this.redSlider.sliderValue = color.getRed() / 255.0F;
        this.greenSlider.sliderValue = color.getGreen() / 255.0F;
        this.blueSlider.sliderValue = color.getBlue() / 255.0F;
    }
}
