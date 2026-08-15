package com.github.debris.debrisclient.localization;

import com.github.debris.debrisclient.util.StringUtil;

// TODO make language keys
public enum EnchantPlanText implements ITranslatable {
    PREFIX("附魔规划: %s"),
    ERROR("出错, 请查看日志"),
    NOT_ENCHANTABLE("物品不可附魔"),
    NORMAL_BOOKS_NOT_FOUND("物品栏无常规附魔书"),
    INVALID_BOOKS("存在无效附魔书"),
    PUNISHED_BOOKS("存在有惩罚附魔书, 使用袪魔台重置之"),
    ENCHANTMENT_DUPLICATE("存在附魔重复: %s"),
    ENCHANTMENT_CONFLICT("存在附魔冲突: %s"),
    INFO_START("铁砧附魔规划: 结果如下, 每步提供了附魔书列表, 以及合并到物品上时消耗的等级(合并书的消耗忽略)"),
    INFO_STEP("第%d步, 消耗%d级: %s%s"),
    INFO_STEP_PUNISH(", 有惩罚亏损, 建议增减附魔书后重新规划"),
    ;
    private final String chinese;

    EnchantPlanText(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return this.chinese;
    }

    public String formatChinese(Object... objects) {
        return StringUtil.translate(this.chinese, objects);
    }

    @Override
    public String getKey() {
        return "dummy";
    }
}
