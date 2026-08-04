package com.github.debris.debrisclient.mixins.client.entity;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.EntityGlowing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;

    @Shadow
    public int ticksExisted;

    @Shadow
    public abstract boolean isGlowing();

    @Unique
    private boolean clientGlowing = false;

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void entityGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (this.world.isRemote && this.clientGlowing) cir.setReturnValue(true);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void entityVisible(CallbackInfoReturnable<Boolean> cir) {
        if (this.world.isRemote && DCConfig.DisableInvisibility.getBooleanValue() && !((Entity) (Object) this instanceof EntityPlayer)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInRangeToRender3d", at = @At("HEAD"), cancellable = true)
    private void entityInRange(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (DCConfig.SkipCullingGlowingEntity.getBooleanValue() && this.isGlowing()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void onUpdate(CallbackInfo ci) {
        if (this.world.isRemote && this.ticksExisted % 20 == 0) {
            this.clientGlowing = EntityGlowing.shouldGlow((Entity) (Object) this);
        }
    }
}
