package com.github.debris.debrisclient.integration.waila;

import com.github.debris.debrisclient.DebrisClient;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.Entity;

@WailaPlugin
public class WailaPluginImpl implements IWailaPlugin {
    public static final String ShowEntityRegistryKey = "debrisclient.showEntityRegistryKey";

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.addConfig(DebrisClient.MOD_NAME, ShowEntityRegistryKey);
        registrar.registerBodyProvider(new WailaEntityProviderImpl(), Entity.class);
    }
}
