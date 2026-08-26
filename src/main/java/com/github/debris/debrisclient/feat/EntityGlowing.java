package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.unsafe.mod.*;
import com.github.debris.debrisclient.util.EntityType;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityGlowing {
    private static final List<Predicate<Entity>> ENTRIES = new ArrayList<>();

    public static boolean shouldGlow(Entity entity) {
        if (DCConfig.AllEntitiesGlowing.getBooleanValue()) return true;
        return ENTRIES.stream().anyMatch(x -> x.test(entity));
    }

    private static boolean isInList(Entity entity) {
        List<String> list = DCConfig.GlowEntityList.getStrings();
        if (list.isEmpty()) return false;
        ResourceLocation type = EntityType.getType(entity);
        if (type == null) return false;
        return list.contains(type.toString());
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
        register(EntityGlowing::isInList);
        register(DCConfig.LibrarianGlowing, EntityGlowing::isLibrarian);
        register(DCConfig.ElderGuardianGlowing, entity -> entity instanceof EntityElderGuardian);
        register(DCConfig.BossGlowing, ModReference.LYCANITES_MOBS, LycanitesmobsAccess::isBoss);
        register(DCConfig.DragonGlowing, ModReference.ICE_AND_FIRE, IceAndFireAccess::isDragon);
        register(DCConfig.SeaSerpentGlowing, ModReference.ICE_AND_FIRE, IceAndFireAccess::isSeaSerpent);
        register(DCConfig.GoldenWyrmGlowing, ModReference.DEFILED_LANDS, DefiledLandAccess::isGoldenWyrm);
        register(DCConfig.PixieGlowingFF, ModReference.FAMILIAR_FAUNA, FamiliarFaunaAccess::isPixie);
        register(DCConfig.PixieGlowingIAF, ModReference.ICE_AND_FIRE, IceAndFireAccess::isPixie);
        register(DCConfig.StoneLingGlowing, ModReference.QUARK, QuarkAccess::isStoneLing);
        register(DCConfig.SpectreGlowing, ModReference.CHARM, CharmAccess::isSpectre);
        register(DCConfig.BlightMobGlowing, ModReference.SCALING_HEALTH, ScalingHealthAccess::isBlight);
        register(DCConfig.MimicGlowing, ModReference.ARTIFACTS, entity -> EntityType.matches(entity, EntityType.MIMIC));
    }
}
