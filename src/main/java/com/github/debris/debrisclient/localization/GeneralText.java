package com.github.debris.debrisclient.localization;

public enum GeneralText implements ITranslatable {
    FAIL("result.fail"),
    SUCCESS("result.success"),
    ;

    private final String key;

    GeneralText(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
