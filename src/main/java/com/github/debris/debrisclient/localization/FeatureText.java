package com.github.debris.debrisclient.localization;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public enum FeatureText implements ITranslatable {
    ENTITY_SPAWN("%s在%s处生成了"),
    ;

    private final String key;

    FeatureText(String key) {
        this.key = key;
    }

    public ITextComponent formatChinese(Object... objects) {
        return new TextComponentTranslation(this.key, objects);
    }

    @Override
    public String getKey() {
        return "dummy";
    }
}
