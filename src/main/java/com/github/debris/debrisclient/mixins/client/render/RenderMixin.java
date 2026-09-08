package com.github.debris.debrisclient.mixins.client.render;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.EntityColor;
import com.github.debris.debrisclient.util.ComponentUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class RenderMixin<T extends Entity> {

    @Shadow
    public abstract FontRenderer getFontRendererFromRenderManager();

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void bountifulGlowingColor(T entityIn, CallbackInfoReturnable<Integer> cir) {
        if (DCConfig.BountifulGlowingColor.getBooleanValue()) {
            cir.setReturnValue(ComponentUtil.getColorInteger(this.getFontRendererFromRenderManager(), EntityColor.getColor(entityIn)));
        }
    }
}
