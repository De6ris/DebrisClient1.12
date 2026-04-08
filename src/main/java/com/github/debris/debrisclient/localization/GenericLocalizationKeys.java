package com.github.debris.debrisclient.localization;

public enum GenericLocalizationKeys implements LocalizationKey {
    INFO_DISPLAY_SUB_SEASON("gui.xaero_infodisplay.sub_season"),
    SUB_SEASON_INFO("gui.sub_season.info"),
    ;

    private final String key;

    GenericLocalizationKeys(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
