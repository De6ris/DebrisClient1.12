package com.github.debris.debrisclient.mixins.client.gui;

import com.github.debris.debrisclient.feat.IMBlocker;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class GuiChatMixin {
    @Shadow
    private String defaultInputFieldText;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        IMBlocker.onNewChat(this.defaultInputFieldText);
    }
}
