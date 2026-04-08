package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.DCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

import java.util.List;

public class EntityCulling {
    /**
     * Changes the behavior of EntityCulling mod
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean shouldSkipCulling(Minecraft client, Entity entity) {
        if (client.player == entity) return true;
        if (DCConfig.SkipCullingGlowingEntity.getBooleanValue() && entity.isGlowing()) return true;

        return false;
    }

    /**
     * My culling
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean shouldCullEntity(Entity entity) {
        if (DCConfig.CullRidingEntity.getBooleanValue() && Minecraft.getMinecraft().player.getRidingEntity() == entity)
            return true;

        List<String> list = DCConfig.CullEntityList.getStrings();
        if (list.isEmpty()) return false;

        String entityString = EntityList.getEntityString(entity);
        if (entityString == null) return false;

        if (list.stream().anyMatch(entityString::contains)) return true;

        return false;
    }
}
