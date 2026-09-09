package com.github.debris.debrisclient.localization;

public enum EnchantPreviewText implements ITranslatable {
    CRACKING("正在破解: %.2f%%"),
    FAIL("破解失败"),
    MULTICHOICE("可能种子数: %d, 请放置另一物品提供信息"),
    ;

    private final String key;

    EnchantPreviewText(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
