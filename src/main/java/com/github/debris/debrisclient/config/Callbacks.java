package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.gui.DCConfigScreen;
import com.github.debris.debrisclient.feat.*;
import com.github.debris.debrisclient.feat.enchant.plan.AnvilEnchantPlan;
import com.github.debris.debrisclient.inventory.feat.BrewingBarrelTweak;
import com.github.debris.debrisclient.inventory.feat.DisenchanterTweak;
import com.github.debris.debrisclient.inventory.feat.InventoryTweaks;
import com.github.debris.debrisclient.inventory.sort.SortInventory;
import com.github.debris.debrisclient.unsafe.mod.ForgottenItemsAccess;
import com.github.debris.debrisclient.util.Predicates;
import com.github.debris.debrisclient.util.SoundUtil;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Callbacks {
    public static final Logger LOGGER = LogManager.getLogger(Callbacks.class);

    public static void init(Minecraft client) {
        DCConfig.OpenWindow.getKeybind().setCallback((action, key) -> {
            client.displayGuiScreen(new DCConfigScreen());
            return true;
        });

        DCConfig.ToggleGameMode.getKeybind().setCallback((action, key) -> MiscFeat.toggleGameMode(client));

        DCConfig.CopyTPCommand.getKeybind().setCallback((action, key) -> MiscFeat.copyTPCommand(client));

        DCConfig.SortInventory.getKeybind().setCallback((action, key) -> SortInventory.onKey(client));

        DCConfig.AutoContainerOperation.getKeybind().setCallback((action, key) -> {
            if (BrewingBarrelTweak.run(client)) {
                SoundUtil.playClickSound(client);
                return true;
            }
            if (DisenchanterTweak.run(client)) {
                SoundUtil.playClickSound(client);
                return true;
            }
            return false;
        });

        DCConfig.ThrowSimilar.getKeybind().setCallback((action, key) -> {
            if (Predicates.notInGuiContainer(client)) return false;
            InventoryTweaks.tryThrowSimilar();
            return true;
        });

        DCConfig.ThrowSection.getKeybind().setCallback((action, key) -> {
            if (Predicates.notInGuiContainer(client)) return false;
            return InventoryTweaks.tryThrowSection();
        });

        DCConfig.RuneTweak.setValueChangeCallback(configBoolean -> {
            if (ModReference.hasMod(ModReference.FORGOTTENITEMS)) {
                ForgottenItemsAccess.syncRuneTweak(configBoolean.getBooleanValue());
            }
        });

        DCConfig.IMBlocker.setValueChangeCallback(configBoolean -> {
            if (!configBoolean.getBooleanValue()) IMBlocker.enable();
        });

        DCConfig.AddToIMBlockerWhiteList.getKeybind().setCallback((action, key) -> MiscFeat.addToIMBlockerWhiteList(client));

        DCConfig.AlignWithEnderEye.getKeybind().setCallback((action, key) -> MiscFeat.alignWithEnderEye(client));

        DCConfig.CopyMeasureData.getKeybind().setCallback((action, key) -> MiscFeat.copyMeasureData(client));

        ConfigFactory.setToggleCallback(DCConfig.FreeCam, (action, key) -> FreeCam.toggle(client), FreeCam::isActive);

        ConfigFactory.setToggleCallback(DCConfig.HoldAttack, (action, key) -> AutoClicker.toggleHoldAttack(client), () -> AutoClicker.isHoldAttacking(client));
        ConfigFactory.setToggleCallback(DCConfig.HoldUse, (action, key) -> AutoClicker.toggleHoldUse(client), () -> AutoClicker.isHoldUsing(client));

        DCConfig.AnvilEnchantPlan.getKeybind().setCallback((action, key) -> AnvilEnchantPlan.run(client));
    }

}
