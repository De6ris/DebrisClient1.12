package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.mixins.client.IPlayerControllerMPMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class InteractionUtil {
    public static int getHotBar(Minecraft client) {
        return client.player.inventory.currentItem;
    }

    public static void setHotBar(int index) {
        Minecraft client = Minecraft.getMinecraft();
        client.player.inventory.currentItem = index;
        ((IPlayerControllerMPMixin) client.playerController).invokeSyncCurrentPlayItem();
    }

    public static void swapHands() {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
    }
}
