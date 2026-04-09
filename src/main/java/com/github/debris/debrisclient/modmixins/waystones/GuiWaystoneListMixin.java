package com.github.debris.debrisclient.modmixins.waystones;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.FastWaypoint;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiWaystoneList.class, remap = false)
public class GuiWaystoneListMixin extends GuiScreen {
    @Shadow
    @Final
    private WaystoneEntry fromWaystone;

    @Inject(method = "initGui",
            at = @At(value = "INVOKE",
                    target = "Lnet/blay09/mods/waystones/client/gui/GuiWaystoneList;updateList()V",
                    remap = false),
            remap = true)
    private void addButton(CallbackInfo ci) {
        if (DCConfig.FastWaypoint.getBooleanValue()) {
            this.buttonList.add(FastWaypoint.createButton());
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), remap = true)
    private void onClick(GuiButton button, CallbackInfo ci) {
        if (button.id == FastWaypoint.BUTTON_ID) {
            FastWaypoint.createWayPoint(this.fromWaystone.getDimensionId(), this.fromWaystone.getPos(), this.fromWaystone.getName());
        }
    }
}
