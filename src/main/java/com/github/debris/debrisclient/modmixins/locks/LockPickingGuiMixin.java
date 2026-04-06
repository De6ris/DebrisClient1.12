package com.github.debris.debrisclient.modmixins.locks;

import com.github.debris.debrisclient.feat.lock.LocksTweak;
import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.client.gui.sprite.Sprite;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = LockPickingGui.class, remap = false)
public abstract class LockPickingGuiMixin extends GuiContainer {
    @Shadow
    protected int currPin;

    @Shadow
    protected Sprite[] springs;

    @Shadow
    @Final
    public int length;

    @Shadow
    protected abstract int getSelectedPin();

    @Shadow
    @Final
    public boolean[] pins;

    public LockPickingGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void onOpen(LockPickingContainer cont, CallbackInfo ci) {
        LocksTweak.onOpen(this, this.length);
    }

    @Inject(
            method = "handlePin",
            at = @At("HEAD"),
            remap = false
    )
    private void onPin(boolean correct, boolean reset, CallbackInfo ci) {
        LocksTweak.onPin(this, this.currPin, correct, reset);
    }

    @Inject(
            method = "drawGuiContainerBackgroundLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", remap = true),
            remap = true
    )
    private void onRender(float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        double[] posX = Arrays.stream(this.springs).mapToDouble(x -> x.posX).toArray();
        LocksTweak.onRender(this, posX, mouseX, mouseY);
    }

    @Inject(method = "updateScreen", at = @At("RETURN"), remap = true)
    private void onTick(CallbackInfo ci) {
        LocksTweak.onTick(this, this.pins, this.getSelectedPin());
    }
}
