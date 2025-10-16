package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class AutoClicker {
    public static void onTick(Minecraft client) {
//        if (!Predicates.inGameNoGui(client)) return;
    }

    public static boolean toggleHoldAttack(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return false;
        KeyBinding keyBinding = client.gameSettings.keyBindAttack;
        KeyBinding.setKeyBindState(keyBinding.getKeyCode(), !keyBinding.isKeyDown());
        return true;
    }

    public static boolean toggleHoldUse(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return false;
        KeyBinding keyBinding = client.gameSettings.keyBindUseItem;
        KeyBinding.setKeyBindState(keyBinding.getKeyCode(), !keyBinding.isKeyDown());
        return true;
    }

    public static boolean isHoldAttacking(Minecraft client) {
        return client.gameSettings.keyBindAttack.isKeyDown();
    }

    public static boolean isHoldUsing(Minecraft client) {
        return client.gameSettings.keyBindUseItem.isKeyDown();
    }
}
