package com.github.debris.debrisclient.modmixins.waystones;

import com.github.debris.debrisclient.gui.button.GuiBetterButton;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.Waystone2Xaero;
import com.github.debris.debrisclient.unsafe.mod.XaeroMiniMapAccess;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiWaystoneList.class, remap = false)
public class GuiWaystoneListMixin extends GuiScreen {
    @Shadow
    @Final
    private WaystoneEntry fromWaystone;
    @Shadow
    private GuiButton btnPrevPage;
    @Unique
    private GuiBetterButton addWaypoint;

    @Inject(method = "initGui",
            at = @At(value = "INVOKE",
                    target = "Lnet/blay09/mods/waystones/client/gui/GuiWaystoneList;updateList()V",
                    remap = false),
            remap = true)
    private void addButton(CallbackInfo ci) {
        if (!DCConfig.WayStoneTweak.getBooleanValue()) return;
        addWaypoint = new GuiBetterButton(Waystone2Xaero.BUTTON_ID,
//                this.width / 2 - 20,
//                this.height / 2 + 22,
                0,
                0,
                50,
                16,
                "创建路径点");
        this.buttonList.add(addWaypoint);
    }

//    @Inject(method = "updateList", at = @At("RETURN"), remap = false)
//    private void updateY(CallbackInfo ci) {
//        this.addWaypoint.y = this.btnPrevPage.y - 18;
//    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), remap = true, cancellable = true)
    private void onClick(GuiButton button, CallbackInfo ci) {
        if (button.id == Waystone2Xaero.BUTTON_ID) {
            XaeroMiniMapAccess.createWayPoint(this.fromWaystone.getDimensionId(), this.fromWaystone.getPos(), this.fromWaystone.getName());
            ci.cancel();
        }
    }
}
