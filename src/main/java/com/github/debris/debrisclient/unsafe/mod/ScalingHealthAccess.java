package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.silentchaos512.scalinghealth.event.BlightHandler;

public class ScalingHealthAccess {

    public static boolean isBlight(Entity entity) {
        return entity instanceof EntityLivingBase && BlightHandler.isBlight((EntityLivingBase) entity);
    }
}
