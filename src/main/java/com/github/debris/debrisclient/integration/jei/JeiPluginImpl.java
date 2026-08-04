package com.github.debris.debrisclient.integration.jei;

import com.github.debris.debrisclient.util.JeiUtil;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JeiPluginImpl implements IModPlugin {
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JeiUtil.jeiRuntime = jeiRuntime;
    }
}
