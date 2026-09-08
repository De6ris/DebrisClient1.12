package com.github.debris.debrisclient.integration.waila;

import com.github.debris.debrisclient.util.EntityType;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaEntityProviderImpl implements IWailaEntityProvider {
    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig(WailaPluginImpl.ShowEntityRegistryKey)) {
            ResourceLocation identifier = EntityType.getType(entity);
            String s = identifier == null ? "unknown" : identifier.toString();
            currenttip.add("注册名: " + s);
        }
        return currenttip;
    }
}
