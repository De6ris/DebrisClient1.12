package com.github.debris.debrisclient.event;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.MiscFeat;
import com.github.debris.debrisclient.inventory.feat.AutoReforging;
import com.google.common.collect.ObjectArrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.Arrays;

public class Hooks {
    public static void onMouseClicked(Minecraft client, GuiScreen screen, int mouseX, int mouseY, int mouseButton) {
        AutoReforging.onMouseClicked(client, screen, mouseX, mouseY, mouseButton);
    }

    public static void onEntitySpawn(Minecraft mc, World world, Entity entity) {
        MiscFeat.notifyEntitySpawn(mc, entity);
    }

    // assuming items are already compared
    public static boolean nbtTakenAsEqual(ItemStack stackA, ItemStack stackB) {
        if (!DCConfig.RefillBaublesIgnoreNBT.getBooleanValue()) return false;

        ResourceLocation registryName = stackA.getItem().getRegistryName();
        if (registryName == null) return false;

        return "bountifulbaubles".equals(registryName.getNamespace());
    }

    public static String[] preSetCompletions(String[] completions) {
        if (DCConfig.SalutationClientCommandsCompat.getBooleanValue()) {
            String[] clientCompletions = ClientCommandHandler.instance.latestAutoComplete;
            if (clientCompletions != null) {
                clientCompletions = Arrays.stream(clientCompletions).map(x -> {
                    x = x.replace(TextFormatting.GRAY.toString(), "");
                    x = x.replace(TextFormatting.RESET.toString(), "");
                    return x;
                }).toArray(String[]::new);
                completions = ObjectArrays.concat(clientCompletions, completions, String.class);
            }
        }
        return completions;
    }
}
