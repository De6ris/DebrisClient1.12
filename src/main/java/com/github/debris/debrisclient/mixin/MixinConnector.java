package com.github.debris.debrisclient.mixin;

import com.github.debris.debrisclient.ModReference;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import zone.rong.mixinbooter.service.ModDiscoverer;

public class MixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        if (ModDiscoverer.isModPresent(ModReference.XAERO_LIB)) {
            Mixins.addConfiguration("mixins.debrisclient_xaero.json");
        }
    }
}
