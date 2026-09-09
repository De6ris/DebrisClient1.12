package com.github.debris.debrisclient.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.jetbrains.annotations.Nullable;

public class RayTraceUtil {

    @Nullable
    public static BlockPos getBlockPos(Minecraft client) {
        if (client.objectMouseOver == null || client.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return null;
        }
        return client.objectMouseOver.getBlockPos();
    }

    @Nullable
    public static IBlockState getBlockState(Minecraft client) {
        if (client.objectMouseOver == null || client.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return null;
        }
        return client.world.getBlockState(client.objectMouseOver.getBlockPos());
    }

    @Nullable
    public static Entity getEntity(Minecraft client) {
        if (client.objectMouseOver == null || client.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY) {
            return null;
        }
        return client.objectMouseOver.entityHit;
    }
}
