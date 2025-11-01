package com.github.debris.debrisclient.mixins.client.entity;

import com.github.debris.debrisclient.config.DCConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
    private void librarianGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (!DCConfig.LibrarianGlowing.getBooleanValue()) return;
        if ((Entity) (Object) this instanceof EntityVillager) {
            EntityVillager entityVillager = (EntityVillager) (Object) this;
            VillagerRegistry.VillagerProfession profession = entityVillager.getProfessionForge();
            String path = profession.getSkin().getPath();
            if (path.contains("librarian")) {
                cir.setReturnValue(true);
            }
        }
    }
}
