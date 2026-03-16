package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.mod.*;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityGlowing {
    private static final List<Predicate<Entity>> ENTRIES = new ArrayList<>();

    public static boolean shouldGlow(Entity entity) {
        return ENTRIES.stream().anyMatch(x -> x.test(entity));
    }

    private static boolean isInList(Entity entity) {
        ResourceLocation key = EntityList.getKey(entity.getClass());
        if (key == null) return false;
        return DCConfig.GlowEntityList.getStrings().contains(key.toString());
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean isLibrarian(Entity entity) {
        if (entity instanceof EntityVillager) {
            EntityVillager entityVillager = (EntityVillager) entity;
            VillagerRegistry.VillagerProfession profession = entityVillager.getProfessionForge();
            String path = profession.getSkin().getPath();
            if (path.contains("librarian")) return true;
        }
        return false;
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(ConfigBoolean config, Predicate<Entity> predicate) {
        register(entity -> config.getBooleanValue() && predicate.test(entity));
    }

    private static void register(ConfigBoolean config, String modId, Predicate<Entity> predicate) {
        register(entity -> config.getBooleanValue() && ModReference.hasMod(modId) && predicate.test(entity));
    }

    private static void register(Predicate<Entity> predicate) {
        ENTRIES.add(predicate);
    }

    static {
        register(DCConfig.ListGlowing, EntityGlowing::isInList);
        register(DCConfig.LibrarianGlowing, EntityGlowing::isLibrarian);
        register(DCConfig.BossGlowing, ModReference.LYCANITESMOBS, LycanitesmobsAccess::isBoss);
        register(DCConfig.GoldenWyrmGlowing, ModReference.DEFILED_LANDS, DefiledLandAccess::isGoldenWyrm);
        register(DCConfig.PixieGlowingFF, ModReference.FAMILIAR_FAUNA, FamiliarFaunaAccess::isPixie);
        register(DCConfig.PixieGlowingIAF, ModReference.ICEANDFIRE, IceAndFireAccess::isPixie);
        register(DCConfig.StoneLingGlowing, ModReference.QUARK, QuarkAccess::isStoneLing);
        register(DCConfig.SpectreGlowing, ModReference.CHARM, CharmAccess::isSpectre);
    }
}
