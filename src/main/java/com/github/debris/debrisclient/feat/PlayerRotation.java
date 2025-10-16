package com.github.debris.debrisclient.feat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PlayerRotation {
    public static void lookAtEntity(EntityPlayerSP player, Entity entity) {
        lookAtDirection(player, getDirection(player, entity));
    }

    public static void lookAtDirection(EntityPlayerSP player, Vec3d directionVec) {
        double dx = directionVec.x;
        double dy = directionVec.y;
        double dz = directionVec.z;
        double dh = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, dh));
        lookAtAngles(player, yaw, pitch);
    }

    public static void lookAtAngles(EntityPlayerSP player, float yaw, float pitch) {
        player.setLocationAndAngles(player.posX, player.posY, player.posZ, yaw, pitch);
    }

    private static Vec3d getDirection(Entity from, Entity to) {
        return to.getPositionEyes(1.0F).subtract(from.getPositionEyes(1.0F));
    }
}
