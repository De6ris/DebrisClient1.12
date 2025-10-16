package com.github.debris.debrisclient.modmixins.xray;

import com.github.debris.debrisclient.feat.FreeCam;
import com.xray.XRay;
import com.xray.xray.Controller;
import com.xray.xray.Events;
import com.xray.xray.Render;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Events.class, remap = false)
public class EventsMixin {
    /**
     * @author Debris
     * @reason compat
     */
    @SubscribeEvent
    @Overwrite
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        if (Controller.drawOres()) {
            float f = event.getPartialTicks();
            EntityPlayerSP camera = FreeCam.isActive() ? FreeCam.getFreeCamera() : XRay.mc.player;
            Render.drawOres((float) camera.prevPosX + ((float) camera.posX - (float) camera.prevPosX) * f, (float) camera.prevPosY + ((float) camera.posY - (float) camera.prevPosY) * f, (float) camera.prevPosZ + ((float) camera.posZ - (float) camera.prevPosZ) * f);
        }
    }
}
