package com.github.debris.debrisclient.localization;

// TODO make language keys
public enum ChatHudText implements ITranslatable {
    GAME_MODE_NOT_PERMITTED("无权限更改游戏模式"),
    TP_COMMAND_COPIED("已复制TP指令"),
    COORDINATES_TOOLTIP("点此看向坐标"),
    ENTITY_TYPE_NULL("实体类型为空"),
    ADDED_TO_LIST("已将%s添加到列表"),
    NO_TARGET_ENTITY("未指向实体"),
    ENTITY_SPAWN("%s在%s处生成了"),
    ;

    private final String key;

    ChatHudText(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
