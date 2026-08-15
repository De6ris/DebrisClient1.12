package com.github.debris.debrisclient.localization;

import com.github.debris.debrisclient.util.StringUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public interface ITranslatable {
    String getKey();

    default ITextComponent translate() {
        return new TextComponentTranslation(this.getKey());
    }

    default ITextComponent translate(Object... objects) {
        return new TextComponentTranslation(this.getKey(), objects);
    }

    default String translateS() {
        return StringUtil.translate(this.getKey());
    }

    default String translateS(Object... objects) {
        return StringUtil.translate(this.getKey(), objects);
    }
}
