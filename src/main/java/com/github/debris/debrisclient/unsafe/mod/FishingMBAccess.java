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

        if (fishingData == null) return;
        if (!fishingData.isFishing()) return;

        int errorVariance = fishingData.getErrorVariance();
        int diff = fishingData.getReelAmount() - fishingData.getReelTarget();

        if (Math.abs(diff) < errorVariance && pressing != PacketKeybindS.Keybind.NONE) {
            PrimaryPacketHandler.INSTANCE.sendToServer(new PacketKeybindS(PacketKeybindS.Keybind.NONE));
            pressing = PacketKeybindS.Keybind.NONE;
            return;
        }
        if (diff > 0 && pressing != PacketKeybindS.Keybind.REEL_IN) {
            PrimaryPacketHandler.INSTANCE.sendToServer(new PacketKeybindS(PacketKeybindS.Keybind.REEL_IN));
            pressing = PacketKeybindS.Keybind.REEL_IN;
            return;
        }
        if (diff < 0 && pressing != PacketKeybindS.Keybind.REEL_OUT) {
            PrimaryPacketHandler.INSTANCE.sendToServer(new PacketKeybindS(PacketKeybindS.Keybind.REEL_OUT));
            pressing = PacketKeybindS.Keybind.REEL_OUT;
        }
    }

    public static boolean isFishingRod(ItemStack stack) {
        return stack.getItem() instanceof ItemBetterFishingRod;
    }
}
