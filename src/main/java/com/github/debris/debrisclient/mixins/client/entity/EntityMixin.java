package com.github.debris.debrisclient.mixins.client.entity;

import com.github.debris.debrisclient.feat.EntityGlowing;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
    private void entityGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (EntityGlowing.shouldGlow((Entity) (Object) this)) cir.setReturnValue(true);
    }
}
