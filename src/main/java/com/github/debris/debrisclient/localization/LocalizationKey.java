package com.github.debris.debrisclient.localization;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public interface LocalizationKey {
    String getKey();

    default ITextComponent translate() {
        return new TextComponentTranslation(this.getKey());
    }

    default ITextComponent translate(Object... objects) {
        return new TextComponentTranslation(this.getKey(), objects);
    }
}
