package com.github.debris.debrisclient.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class EntityType {

    public static final ResourceLocation MIMIC = new ResourceLocation("artifacts", "mimic");

    @Nullable
    public static ResourceLocation getType(Entity entity) {
        return EntityList.getKey(entity);
    }

    public static boolean matches(Entity entity, ResourceLocation id) {
        ResourceLocation type = getType(entity);
        if (type == null) return false;
        return type.equals(id);
    }
}
