package com.github.debris.debrisclient.mixins.client.resources;

import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanguageManager.class)
public interface ILanguageManagerMixin {
    @Accessor("CURRENT_LOCALE")
    static Locale getCurrentLocale() {
        throw new AssertionError();
    }
}
