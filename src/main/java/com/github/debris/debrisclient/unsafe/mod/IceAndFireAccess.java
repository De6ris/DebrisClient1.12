package com.github.debris.debrisclient.unsafe.mod;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.Entity;

public class IceAndFireAccess {
    public static boolean isPixie(Entity entity) {
        return entity instanceof EntityPixie;
    }

    public static boolean isDragon(Entity entity) {
        return entity instanceof EntityDragonBase;
    }

    public static boolean isSeaSerpent(Entity entity) {
        return entity instanceof EntitySeaSerpent;
    }
}
