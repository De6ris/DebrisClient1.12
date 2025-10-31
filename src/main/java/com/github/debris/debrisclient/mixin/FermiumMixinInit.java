package com.github.debris.debrisclient.mixin;

import com.github.debris.debrisclient.DebrisClient;
import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name(DebrisClient.MOD_ID)
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class FermiumMixinInit implements IFMLLoadingPlugin {
    public FermiumMixinInit() {
        FermiumRegistryAPI.enqueueMixin(false, "mixins.debrisclient.json");
        FermiumRegistryAPI.enqueueMixin(true, "mixins.debrisclient_late.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
