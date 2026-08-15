package com.github.debris.debrisclient.localization;

import com.github.debris.debrisclient.util.StringUtil;

// TODO make language keys
public enum TooltipText implements ITranslatable {
    ENCHANTMENT_COST("附魔成本: %d级"),
    ANVIL_PUNISHMENT("铁砧惩罚: %d级(%d次操作)"),
    ENCHANTMENT_CONFLICT("冲突附魔: %s"),
    EQUIVALENT_LEVEL("折合等级: %d");

    private final String chinese;

    TooltipText(String chinese) {
        this.chinese = chinese;
    }

    public String formatChinese(Object... objects) {
        return StringUtil.translate(this.chinese, objects);
    }

    @Override
    public String getKey() {
        return "dummy";
    }
}
