package com.github.debris.debrisclient.modmixins.modularui;

import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.github.debris.debrisclient.unsafe.modularui.GuiActions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(value = ModularScreen.class, remap = false)
public abstract class ModularScreenMixin {
    @Shadow
    public abstract void registerGuiActionListener(IGuiAction action);

    @Inject(method = "<init>(Ljava/lang/String;Ljava/util/function/Function;Z)V", at = @At("RETURN"), remap = false)
    private void onInit(String owner, Function<ModularGuiContext, ModularPanel> mainPanelCreator, boolean ignored, CallbackInfo ci) {
        this.registerGuiActionListener(new GuiActions.MousePressed());
        this.registerGuiActionListener(new GuiActions.MouseReleased());
        this.registerGuiActionListener(new GuiActions.MouseDrag());
//        this.registerGuiActionListener(new GuiActions.MouseScroll());// available in malilib
    }
}
