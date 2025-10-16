package com.github.debris.debrisclient.mixin;

import zone.rong.mixinbooter.ILateMixinLoader;
import zone.rong.mixinbooter.MixinLoader;

import java.util.Collections;
import java.util.List;

@MixinLoader
public class LateMixinInit implements ILateMixinLoader {
    public LateMixinInit() {
    }

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.debrisclient_late.json");
    }
}
