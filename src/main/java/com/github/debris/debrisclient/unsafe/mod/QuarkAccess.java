package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.entity.Entity;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.management.feature.FToSwitchItems;
import vazkii.quark.world.entity.EntityStoneling;

public class QuarkAccess {
    public static boolean isBetterSwapHands() {
        return ModuleLoader.isFeatureEnabled(FToSwitchItems.class);
    }

    public static boolean isStoneLing(Entity entity) {
        return entity instanceof EntityStoneling;
    }
}
