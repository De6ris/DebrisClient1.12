package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import svenhjol.charm.crafting.block.BlockCrate;
import svenhjol.charm.world.entity.EntitySpectre;

public class CharmAccess {
    public static boolean isCrate(Block block) {
        return block instanceof BlockCrate;
    }

    public static boolean isSpectre(Entity entity) {
        return entity instanceof EntitySpectre;
    }
}
