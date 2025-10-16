package com.github.debris.debrisclient.forge;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.event.forge.DrawScreenListener;
import com.github.debris.debrisclient.event.forge.PlaySoundListener;
import com.github.debris.debrisclient.event.forge.TooltipListener;
import com.github.debris.debrisclient.event.malilib.InitListener;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DebrisClient.MOD_ID,
        name = DebrisClient.MOD_NAME,
        version = DebrisClient.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "1.12.2",
        dependencies = "required-after:malilib;",
        guiFactory = "com.github.debris.debrisclient.config.gui.DCGuiFactory")
public class ModImpl {
    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     * Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(TooltipListener.class);
        MinecraftForge.EVENT_BUS.register(PlaySoundListener.class);
        MinecraftForge.EVENT_BUS.register(DrawScreenListener.class);
        InitializationHandler.getInstance().registerInitializationHandler(new InitListener());
    }
}
