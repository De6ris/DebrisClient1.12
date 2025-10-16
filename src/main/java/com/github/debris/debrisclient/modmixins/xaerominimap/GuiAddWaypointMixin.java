package com.github.debris.debrisclient.modmixins.xaerominimap;

import com.github.debris.debrisclient.feat.FreeCam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xaero.common.gui.GuiAddWaypoint;

@Mixin(value = GuiAddWaypoint.class, remap = false)
public class GuiAddWaypointMixin {
    @WrapOperation(
            method = {"getAutomaticX", "getAutomaticYInput", "getAutomaticZ"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;",
                    remap = true
            ),
            remap = false
    )
    private Entity freeCamCompat(Minecraft instance, Operation<Entity> original) {
        if (FreeCam.isActive()) return FreeCam.getFreeCamera();
        return original.call(instance);
    }
}
