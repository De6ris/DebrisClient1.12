package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.localization.GenericLocalizationKeys;
import com.github.debris.debrisclient.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class SereneSeasonsAccess {
    private static final Map<Season.SubSeason, String> NAME_MAP;

    @Nullable
    public static ITextComponent getSubSeasonInfo(Minecraft client) {
        WorldClient world = client.world;
        if (world == null) {
            return null;
        }

        ISeasonState seasonState = SeasonHelper.getSeasonState(world);

        int subSeasonDuration = seasonState.getSubSeasonDuration();
        double ratio = (double) (seasonState.getSeasonCycleTicks() % subSeasonDuration) / subSeasonDuration;

        return GenericLocalizationKeys.SUB_SEASON_INFO.translate(
                nameOf(seasonState.getSubSeason()),
                MathUtil.asPercentage(ratio)
        );
    }

    private static ITextComponent nameOf(Season.SubSeason subSeason) {
        String key = NAME_MAP.get(subSeason);
        return new TextComponentTranslation(key);
    }

    private static String getKey(Season.SubSeason subSeason) {
        return "sereneseasons." + subSeason.name().toLowerCase() + ".name";
    }

    static {
        NAME_MAP = new EnumMap<>(Season.SubSeason.class);
        for (Season.SubSeason subSeason : Season.SubSeason.VALUES) {
            NAME_MAP.put(subSeason, getKey(subSeason));
        }
    }
}
