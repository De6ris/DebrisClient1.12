package com.github.debris.debrisclient.unsafe.mod;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.entity.Entity;

public class IceAndFireAccess {
    public static boolean isPixie(Entity entity) {
        return entity instanceof EntityPixie;
    }
}
