package com.github.debris.debrisclient.mixins.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IClientMixin {
    @Invoker("debugFeedbackTranslated")
    void invokeDebugFeedbackTranslated(String untranslatedTemplate, Object... objs);

    @Invoker("clickMouse")
    void invokeClickMouse();

    @Invoker("rightClickMouse")
    void invokeRightClickMouse();
}
