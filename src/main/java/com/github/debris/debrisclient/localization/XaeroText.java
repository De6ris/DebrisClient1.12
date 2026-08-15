package com.github.debris.debrisclient.localization;

public enum XaeroText implements ITranslatable{
    INFO_DISPLAY_BIOME_REGISTRY_NAME("gui.xaero_infodisplay.biome_registry_name"),
    INFO_DISPLAY_BIOME_TRANSLATED_NAME("gui.xaero_infodisplay.biome_translated_name"),
    INFO_DISPLAY_SUB_SEASON("gui.xaero_infodisplay.sub_season"),
    SUB_SEASON_INFO("gui.sub_season.info");

    private final String key;

    XaeroText(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
