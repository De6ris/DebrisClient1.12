package com.github.debris.debrisclient.unsafe.mod;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.Entity;

public class LycanitesmobsAccess {
    public static boolean isBoss(Entity entity) {
        return entity instanceof BaseCreatureEntity && ((BaseCreatureEntity) entity).isBoss();
    }
}
