package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class AutoPickUp {

    public static void onTick(Minecraft client) {
        if (Predicates.inGameNoGui(client) && DCConfig.AutoPickUp.getKeybind().isKeybindHeld()) run(client);
    }

    public static void run(Minecraft client) {
        EntityPlayerSP player = client.player;
        if (DCConfig.StrictMode.getBooleanValue()) {
            MovementInput movementInput = player.movementInput;
            if (movementInput.moveForward != 0 || movementInput.moveStrafe != 0) return;// avoid jitter
        }
        PlayerControllerMP playerController = client.playerController;
        double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        AxisAlignedBB box = player.getEntityBoundingBox().grow(reach);
        List<EntityItem> entityItems = client.world.getEntitiesWithinAABB(EntityItem.class, box);
        if (entityItems.isEmpty()) return;
        float yaw = player.rotationYaw;
        float pitch = player.rotationPitch;
        entityItems.forEach(x -> {
//            if (!shouldSkip(x.getItem())) return;
            PlayerRotation.lookAtEntity(player, x);
            playerController.interactWithEntity(player, x, EnumHand.MAIN_HAND);
        });
        PlayerRotation.lookAtAngles(player, yaw, pitch);
    }

    private static boolean shouldSkip(ItemStack stack) {
        return false;
    }
}
