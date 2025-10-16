package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.config.Callbacks;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.mixins.client.IClientMixin;
import com.github.debris.debrisclient.util.ChatUtil;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Locale;

public class MiscFeat {
    public static boolean addToIMBlockerWhiteList(Minecraft client) {
        GuiScreen screen = client.currentScreen;
        if (screen == null) {
            Callbacks.LOGGER.warn("adding null screen to im blocker white list?");
            return false;
        }
        String name = screen.getClass().getName();
        List<String> strings = DCConfig.IMBlockerWhiteList.getStrings();
        if (strings.contains(name)) return false;
        strings.add(name);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean alignWithEnderEye(Minecraft client) {
        if (Predicates.notInGame(client)) return false;

        EntityPlayerSP player = client.player;

        AxisAlignedBB box = player.getEntityBoundingBox().grow(32.0D);

        List<EntityEnderEye> eyes = client.world.getEntitiesWithinAABB(EntityEnderEye.class, box);

        if (eyes.isEmpty()) return false;

        PlayerRotation.lookAtEntity(player, eyes.get(0));
        return true;
    }

    public static boolean toggleGameMode(Minecraft client) {
        EntityPlayerSP player = client.player;
        if (player.canUseCommand(2, "")) {
            if (player.isCreative()) {
                player.sendChatMessage("/gamemode 0");
            } else {
                player.sendChatMessage("/gamemode 1");
            }
        } else {
            ((IClientMixin) client).invokeDebugFeedbackTranslated("无权限更改游戏模式");
        }
        return true;
    }


    public static boolean copyTPCommand(Minecraft client) {
        ChatUtil.addLocalChat(client, "已复制TP指令");
        EntityPlayerSP player = client.player;
        setClipboard(
                String.format(
                        Locale.ROOT,
                        "/tp @s %.2f %.2f %.2f %.2f %.2f",
                        player.posX,
                        player.posY,
                        player.posZ,
                        player.rotationYaw,
                        player.rotationPitch
                )
        );
        return true;
    }

    public static void setClipboard(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, null);
    }

    public static boolean copyMeasureData(Minecraft client) {
        EntityPlayerSP player = client.player;
        setClipboard(
                String.format(
                        Locale.ROOT,
                        "%.2f %.2f %.2f",
                        player.posX,
                        player.posZ,
                        player.rotationYaw
                )
        );
        return true;
    }

    public static void runAutoPickUp(Minecraft client) {
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
            PlayerRotation.lookAtEntity(player, x);
            playerController.interactWithEntity(player, x, EnumHand.MAIN_HAND);
        });
        PlayerRotation.lookAtAngles(player, yaw, pitch);
    }
}
