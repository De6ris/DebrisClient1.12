package com.github.debris.debrisclient.unsafe.mod;

import baubles.common.container.ContainerPlayerExpanded;
import net.minecraft.inventory.Container;

public class BaublesAccess {
    public static boolean isBaubleContainer(Container container) {
        return container instanceof ContainerPlayerExpanded;
    }
}
