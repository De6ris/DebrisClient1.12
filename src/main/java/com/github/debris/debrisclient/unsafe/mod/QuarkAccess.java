package com.github.debris.debrisclient.unsafe.mod;

import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.management.feature.FToSwitchItems;

public class QuarkAccess {
    public static boolean isBetterSwapHands() {
        return ModuleLoader.isFeatureEnabled(FToSwitchItems.class);
    }
}
