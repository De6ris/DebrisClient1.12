package com.github.debris.debrisclient.unsafe.mod;

import lykrast.defiledlands.common.entity.passive.EntityBookWyrm;
import net.minecraft.entity.Entity;

public class DefiledLandAccess {
    public static boolean isGoldenWyrm(Entity entity) {
        return entity instanceof EntityBookWyrm && ((EntityBookWyrm) entity).isGolden();
    }
}
