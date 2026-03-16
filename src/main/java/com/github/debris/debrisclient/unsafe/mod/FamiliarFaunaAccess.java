package com.github.debris.debrisclient.unsafe.mod;

import familiarfauna.entities.EntityPixie;
import net.minecraft.entity.Entity;

public class FamiliarFaunaAccess {
    public static boolean isPixie(Entity entity) {
        return entity instanceof EntityPixie;
    }
}
