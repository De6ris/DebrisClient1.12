package com.github.debris.debrisclient.mixin;

import com.github.debris.debrisclient.ModReference;
import com.google.common.collect.ImmutableList;
import zone.rong.mixinbooter.ILateMixinLoader;
import zone.rong.mixinbooter.MixinLoader;

import java.util.List;

@MixinLoader
public class LateMixinInit implements ILateMixinLoader {
    public LateMixinInit() {
    }

    @Override
    public List<String> getMixinConfigs() {
        return ImmutableList.of(
                "mixins.debrisclient_late.json",
                "mixins.debrisclient_xaero.json"
        );
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        if (mixinConfig.equals("mixins.debrisclient_xaero.json")) {
            return ModReference.hasMod(ModReference.XAERO_LIB);
        }
        return true;
    }
}
