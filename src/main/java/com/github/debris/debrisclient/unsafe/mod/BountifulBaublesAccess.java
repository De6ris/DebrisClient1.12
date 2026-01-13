package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.StringUtil;
import cursedflames.bountifulbaubles.baubleeffect.EnumBaubleModifier;
import cursedflames.bountifulbaubles.block.ContainerReforger;
import cursedflames.bountifulbaubles.block.GuiReforger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.stream.Stream;

public class BountifulBaublesAccess {
    public static boolean isReforgingGUI(GuiScreen screen) {
        return screen instanceof GuiReforger;
    }

    public static boolean isReforgingContainer(Container container) {
        return container instanceof ContainerReforger;
    }

    public static boolean canReforge(GuiScreen screen) {
        if (!isReforgingGUI(screen)) return false;
        ItemStack bauble = EnumSection.BountifulBaublesReforging.get().getFirstSlot().getStack();
        if (bauble.isEmpty()) return false;
        return AccessorUtil.getButtonList(screen).get(0).enabled;
    }

    public static Stream<String> streamQualities() {
        return Arrays.stream(EnumBaubleModifier.values()).map(x -> x.name);
    }

    public static String translate(String name) {
        String key = String.format("bountifulbaubles.modifier.%s.name", name);
        return StringUtil.translate(key);
    }
}
