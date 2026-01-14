package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.mod.LycanitesmobsAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class GlowingUtil {
    @SuppressWarnings("RedundantIfStatement")
    public static boolean shouldGlow(Entity entity) {
        if (DCConfig.LibrarianGlowing.getBooleanValue() && entity instanceof EntityVillager) {
            EntityVillager entityVillager = (EntityVillager) entity;
            VillagerRegistry.VillagerProfession profession = entityVillager.getProfessionForge();
            String path = profession.getSkin().getPath();
            if (path.contains("librarian")) return true;
        }

        if (
                DCConfig.BossGlowing.getBooleanValue()
                        && ModReference.hasMod(ModReference.LYCANITESMOBS)
                        && LycanitesmobsAccess.isBoss(entity)
        ) return true;

        return false;
    }
}
