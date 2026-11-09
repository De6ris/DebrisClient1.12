package com.github.debris.debrisclient.mixins.mod.salutation;

import com.github.debris.debrisclient.event.Hooks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import speiger.src.salutation.client.gui.chat.AdvancedTabCompleter;

@Mixin(value = AdvancedTabCompleter.class, remap = false)
public class AdvancedTabCompleterMixin {
    @Shadow
    protected Minecraft mc;

    @ModifyVariable(method = "setCompletions", at = @At("HEAD"), ordinal = 0, remap = true, argsOnly = true)
    private String[] addClientCommands(String[] completions) {
        return Hooks.preSetCompletions(completions);
    }

}
