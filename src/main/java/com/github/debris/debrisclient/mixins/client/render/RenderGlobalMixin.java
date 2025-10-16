package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.config.DCConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {
//    @Inject(method = "isOutlineActive", at = @At("HEAD"), cancellable = true)
//    private void librarianGlowing(Entity entityIn, Entity viewer, ICamera camera, CallbackInfoReturnable<Boolean> cir) {
//        if (DCConfig.LibrarianGlowing.getBooleanValue() && entityIn instanceof EntityVillager) {
//            EntityVillager entityVillager = (EntityVillager) entityIn;
//            VillagerRegistry.VillagerProfession profession = entityVillager.getProfessionForge();
//            if (profession.getSkin().getPath().contains("librarian")) {
//                cir.setReturnValue(true);
//            }
//        }// TODO why fail in rlc
//    }

    @SuppressWarnings("RedundantIfStatement")
    @WrapOperation(method = "isOutlineActive", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isGlowing()Z"))
    private boolean librarianGlowing(Entity instance, Operation<Boolean> original) {
        if (original.call(instance)) return true;
        if (DCConfig.LibrarianGlowing.getBooleanValue() && instance instanceof EntityVillager) {
            EntityVillager entityVillager = (EntityVillager) instance;
            VillagerRegistry.VillagerProfession profession = entityVillager.getProfessionForge();
            if (profession.getSkin().getPath().contains("librarian")) {
                return true;
            }
        }
        return false;
    }
}
