package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.block.Block;
import svenhjol.charm.crafting.block.BlockCrate;

public class CharmAccess {
    public static boolean isCrate(Block block) {
        return block instanceof BlockCrate;
    }
}
