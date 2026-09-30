package com.github.debris.debrisclient.config;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.feat.*;
import com.github.debris.debrisclient.feat.enchant.plan.EnchantPlan;
import com.github.debris.debrisclient.gui.screen.DCConfigScreen;
import com.github.debris.debrisclient.gui.screen.InventoryConfigScreen;
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
        DCConfig.OpenConfigScreen.getKeybind().setCallback((action, key) -> {
            client.displayGuiScreen(new DCConfigScreen(null));
            return true;
        });

        DCConfig.OpenInventoryConfigScreen.getKeybind().setCallback((action, key) -> {
            client.displayGuiScreen(new InventoryConfigScreen(null));
            return true;
        });

        DCConfig.ToggleGameMode.getKeybind().setCallback((action, key) -> MiscFeat.toggleGameMode(client));

        DCConfig.CopyTPCommand.getKeybind().setCallback((action, key) -> MiscFeat.copyTPCommand(client));

        DCConfig.RuneTweak.setValueChangeCallback(configBoolean -> {
            if (ModReference.hasMod(ModReference.FORGOTTEN_ITEMS)) {
                ForgottenItemsAccess.syncRuneTweak(configBoolean.getBooleanValue());
            }
        });

        DCConfig.IMBlocker.setValueChangeCallback(IMBlocker::onConfigChange);

        DCConfig.AddToIMBlockerWhiteList.getKeybind().setCallback((action, key) -> MiscFeat.addToIMBlockerWhiteList(client));

        DCConfig.AlignWithEnderEye.getKeybind().setCallback((action, key) -> MiscFeat.alignWithEnderEye(client));

        DCConfig.CopyMeasureData.getKeybind().setCallback((action, key) -> MiscFeat.copyMeasureData(client));

        ConfigFactory.setToggleCallback(DCConfig.FreeCam, (action, key) -> FreeCam.toggle(client), FreeCam::isActive);

        ConfigFactory.setToggleCallback(DCConfig.HoldAttack, (action, key) -> AutoClicker.toggleHoldAttack(client), () -> AutoClicker.isHoldAttacking(client));
        ConfigFactory.setToggleCallback(DCConfig.HoldUse, (action, key) -> AutoClicker.toggleHoldUse(client), () -> AutoClicker.isHoldUsing(client));

        DCConfig.AnvilEnchantPlan.getKeybind().setCallback((action, key) -> EnchantPlan.run(client));

        DCConfig.FastSearch.getKeybind().setCallback((action, key) -> SearchTweaks.searchHovered(client));
        DCConfig.ClearSearch.getKeybind().setCallback((action, key) -> SearchTweaks.clear(client));

        DCConfig.DebugKey.getKeybind().setCallback((action, key) -> MiscFeat.debug(client));

        initInventory(client);
    }

    private static void initInventory(Minecraft client) {
        InventoryConfig.SwitchPreset.setValueChangeCallback(InventoryPreset::switchPreset);

        InventoryConfig.SortInventory.getKeybind().setCallback((action, key) -> SortInventory.onKey(client));

        InventoryConfig.AutoContainerOperation.getKeybind().setCallback((action, key) -> {
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

        InventoryConfig.ThrowSame.getKeybind().setCallback((action, key) -> {
            if (Predicates.notInGuiContainer(client)) return false;
            return InventoryTweaks.tryThrowSimilar();
        });

        InventoryConfig.ThrowSection.getKeybind().setCallback((action, key) -> {
            if (Predicates.notInGuiContainer(client)) return false;
            return InventoryTweaks.tryThrowSection();
        });
    }
}
