package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.config.DCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class CullingUtil {
    public static boolean shouldCullEntity(Entity entity) {
        if (DCConfig.CullRidingEntity.getBooleanValue() && Minecraft.getMinecraft().player.getRidingEntity() == entity)
            return true;

        String entityString = EntityList.getEntityString(entity);
        if (entityString == null) return false;

        if (DCConfig.CullEntityList.getStrings().stream().anyMatch(entityString::contains)) return true;

        return false;
    }

    public static boolean shouldMuteSound(ISound sound) {
        ResourceLocation identifier = sound.getSoundLocation();
        String path = identifier.getPath();

        if (identifier.equals(SoundEvents.BLOCK_ANVIL_USE.getSoundName()) && DCConfig.MuteAnvil.getBooleanValue())
            return true;
        if (path.contains("aegis") && DCConfig.MuteAegis.getBooleanValue()) return true;

        if (DCConfig.MuteSoundList.getStrings().stream().anyMatch(path::contains)) return true;

        return false;
    }

    public static boolean shouldCullParticle(String pathName) {
        if (DCConfig.CullParticleList.getStrings().stream().anyMatch(pathName::contains)) return true;

        return false;
    }
}
