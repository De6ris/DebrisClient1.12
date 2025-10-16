package com.github.debris.debrisclient.mixins.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(GuiScreen.class)
public interface IGuiScreenMixin {
    @Invoker("actionPerformed")
    void invokeActionPerformed(GuiButton button);

    @Accessor("buttonList")
    List<GuiButton> getButtonList();
}
