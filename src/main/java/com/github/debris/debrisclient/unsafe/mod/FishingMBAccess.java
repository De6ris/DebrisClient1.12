package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.theawesomegem.fishingmadebetter.common.capability.fishing.FishingCapabilityProvider;
import net.theawesomegem.fishingmadebetter.common.capability.fishing.IFishingData;
import net.theawesomegem.fishingmadebetter.common.item.fishingrod.ItemBetterFishingRod;
import net.theawesomegem.fishingmadebetter.common.networking.PrimaryPacketHandler;
import net.theawesomegem.fishingmadebetter.common.networking.packet.PacketKeybindS;

public class FishingMBAccess {
    private static PacketKeybindS.Keybind pressing = PacketKeybindS.Keybind.NONE;

    public static void onTick(Minecraft client) {
        IFishingData fishingData = client.player.getCapability(FishingCapabilityProvider.FISHING_DATA_CAP, null);

        if (fishingData == null || !fishingData.isFishing()) {
            release(client);
            return;
        }

        int errorVariance = fishingData.getErrorVariance();
        int diff = fishingData.getReelAmount() - fishingData.getReelTarget();

        if (Math.abs(diff) < errorVariance) {
            release(client);
            return;
        }
        if (diff > 0) {
            press(PacketKeybindS.Keybind.REEL_IN);
            return;
        }
        if (diff < 0) {
            press(PacketKeybindS.Keybind.REEL_OUT);
        }
    }

    public static void release(Minecraft client) {
        if (pressing == PacketKeybindS.Keybind.NONE) return;
        if (client != null && client.player != null) {
            PrimaryPacketHandler.INSTANCE.sendToServer(new PacketKeybindS(PacketKeybindS.Keybind.NONE));
        }
        pressing = PacketKeybindS.Keybind.NONE;
    }

    private static void press(PacketKeybindS.Keybind keybind) {
        if (pressing == keybind) return;
        PrimaryPacketHandler.INSTANCE.sendToServer(new PacketKeybindS(keybind));
        pressing = keybind;
    }

    public static boolean isFishingRod(ItemStack stack) {
        return stack.getItem() instanceof ItemBetterFishingRod;
    }
}
