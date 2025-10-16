package com.github.debris.debrisclient;

import net.minecraftforge.fml.common.Loader;

public class ModReference {
    public static final String BAUBLES = "baubles";
    public static final String BOUNTIFULBAUBLES = "bountifulbaubles";
    public static final String CHARM = "charm";
    public static final String DISENCHANTER = "disenchanter";
    public static final String ENHANCEDVISUALS = "enhancedvisuals";
    public static final String ENTITYCULLING = "entityculling";
    public static final String FORGOTTENITEMS = "forgottenitems";
    public static final String ICEANDFIRE = "iceandfire";
    public static final String INVTWEAKS = "inventorytweaks";
    public static final String POTIONCORE = "potioncore";
    public static final String JECH = "jecharacters";
    public static final String MALILIB = "malilib";
    public static final String MODULARUI = "modularui";
    public static final String QUALITYTOOLS = "qualitytools";
    public static final String QUARK = "quark";
    public static final String RETRO_SOPHISTICATED_BACKPACKS = "retro_sophisticated_backpacks";
    public static final String RUSTIC = "rustic";
    public static final String TWEAKEROO = "tweakeroo";
    public static final String WAYSTONES = "waystones";
    public static final String XRAY = "xray";

    public static boolean hasMod(String modId) {
        return Loader.isModLoaded(modId);
    }
}
