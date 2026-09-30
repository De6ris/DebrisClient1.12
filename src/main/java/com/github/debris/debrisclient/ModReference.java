package com.github.debris.debrisclient;

import net.minecraftforge.fml.common.Loader;

public class ModReference {
    public static final String ARTIFACTS = "artifacts";
    public static final String BAUBLES = "baubles";
    public static final String BOUNTIFUL_BAUBLES = "bountifulbaubles";
    public static final String CHARM = "charm";
    public static final String DEFILED_LANDS = "defiledlands";
    public static final String DISENCHANTER = "disenchanter";
    public static final String ENHANCED_VISUALS = "enhancedvisuals";
    public static final String ENTITY_CULLING = "entityculling";
    public static final String FAMILIAR_FAUNA = "familiarfauna";
    public static final String FISHING_MADE_BETTER = "fishingmadebetter";
    public static final String FORGOTTEN_ITEMS = "forgottenitems";
    public static final String ICE_AND_FIRE = "iceandfire";
    public static final String INV_TWEAKS = "inventorytweaks";
    public static final String ITEM_PHYSIC = "itemphysic";
    public static final String LOCKS = "locks";
    public static final String LYCANITES_MOBS = "lycanitesmobs";
    public static final String POTION_CORE = "potioncore";
    public static final String JECH = "jecharacters";
    public static final String JEI = "jei";
    public static final String MALILIB = "malilib";
    public static final String MO_BENDS = "mobends";
    public static final String MODULARUI = "modularui";
    public static final String QUALITY_TOOLS = "qualitytools";
    public static final String QUARK = "quark";
    public static final String REFINED_STORAGE = "refinedstorage";
    public static final String RETRO_SOPHISTICATED_BACKPACKS = "retro_sophisticated_backpacks";
    public static final String RUSTIC = "rustic";
    public static final String SCALING_HEALTH = "scalinghealth";
    public static final String SERENE_SEASONS = "sereneseasons";
    public static final String SO_MANY_ENCHANTMENTS = "somanyenchantments";
    public static final String TRINKETS_AND_BAUBLES = "xat";
    public static final String TWEAKEROO = "tweakeroo";
    public static final String WAYSTONES = "waystones";
    public static final String XAERO_MINI_MAP = "xaerominimap";
    public static final String XAERO_WORLD_MAP = "xaeroworldmap";
    public static final String XRAY = "xray";

    public static boolean hasMod(String modId) {
        return Loader.isModLoaded(modId);
    }
}
