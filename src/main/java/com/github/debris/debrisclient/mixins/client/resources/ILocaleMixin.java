package com.github.debris.debrisclient.mixins.client.resources;

import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Locale.class)
public interface ILocaleMixin {
    @Accessor
    Map<String, String> getProperties();
}
