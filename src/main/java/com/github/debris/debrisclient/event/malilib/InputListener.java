package com.github.debris.debrisclient.event.malilib;

import com.github.debris.debrisclient.DebrisClient;
import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.feat.HoldInventoryMoving;
import com.github.debris.debrisclient.inventory.feat.InventoryTweaks;
import com.github.debris.debrisclient.inventory.feat.WheelMoving;
import com.github.debris.debrisclient.inventory.section.ContainerSection;
import com.github.debris.debrisclient.inventory.section.SectionHandler;
import com.github.debris.debrisclient.unsafe.mod.RSBackpacksAccess;
import com.github.debris.debrisclient.util.InputUtil;
import com.github.debris.debrisclient.util.InventoryUtil;
import com.github.debris.debrisclient.util.Predicates;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.*;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Optional;

public class InputListener implements IKeybindProvider, IMouseInputHandler, IKeyboardInputHandler {
    public static final InputListener Instance = new InputListener();
    private final IntSet BUTTON_UP_CANCEL_SET = new IntOpenHashSet();

    private InputListener() {
    }

    @Override
    public List<? extends IHotkey> getAllHotkeys() {
        return DCConfig.HOTKEY;
    }

    @Override
    public List<KeybindCategory> getHotkeyCategoriesForCombinedView() {
        return ImmutableList.of(
                new KeybindCategory(DebrisClient.MOD_NAME, "热键", DCConfig.HOTKEY)
        );
    }

    @Override
    public boolean onKeyInput(int eventKey, boolean eventKeyState) {
        return false;
    }

    @Override
    public boolean onMouseInput(int eventButton, int dWheel, boolean eventButtonState) {
        if (dWheel != 0 && handleScroll(dWheel > 0, dWheel)) return true;

        if (eventButtonState) {
            return this.handleButtonDown(eventButton);
        } else {
            return this.handleButtonUp(eventButton);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean handleScroll(boolean up, int amount) {
        if (Predicates.notInGuiContainer(Minecraft.getMinecraft()))
            return false;// the below assuming valid environment

        if (WheelMoving.handleScroll(up, amount)) return true;

        return false;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean handleButtonDown(int eventButton) {
        if (eventButton == 0) {

            if (Predicates.notInGuiContainer(Minecraft.getMinecraft()))
                return false;// the below assuming valid environment

            if (DCConfig.ModifierMoveAll.getKeybind().isKeybindHeld()) {
                Optional<ContainerSection> optional = SectionHandler.getSectionMouseOver();
                if (optional.isPresent()) {
                    optional.get().notEmptyRun(InventoryUtil::quickMove);
                    return true;
                }
            }

            if (DCConfig.ModifierSpreadItem.getKeybind().isKeybindHeld()) {
                if (InventoryTweaks.trySpreading(false)) {
                    this.cancelButtonUp(eventButton);
                    return true;
                }
            }

            if (DCConfig.HoldInventoryMoving.getBooleanValue() && HoldInventoryMoving.start()) {
                return true;
            }
        }

        if (eventButton == 1) {
            if (DCConfig.ModifierSpreadItem.getKeybind().isKeybindHeld()) {
                if (InventoryTweaks.trySpreading(true)) {
                    this.cancelButtonUp(eventButton);// will put down one at HandledScreen.mouseReleased if not canceled
                    return true;
                }
            }
        }

        return false;
    }

    public boolean handleButtonUp(int eventButton) {
        if (eventButton == 0) {

            if (Predicates.notInGuiContainer(Minecraft.getMinecraft()))
                return false;// the below assuming valid environment

            HoldInventoryMoving.stop();
        }
        if (BUTTON_UP_CANCEL_SET.contains(eventButton)) {
            BUTTON_UP_CANCEL_SET.remove(eventButton);
            return true;
        }
        return false;
    }

    private void cancelButtonUp(int eventButton) {
        BUTTON_UP_CANCEL_SET.add(eventButton);
    }

    @Override
    public void onMouseMoved() {
        if (Predicates.notInGuiContainer(Minecraft.getMinecraft())) return;// the below assuming valid environment

        HoldInventoryMoving.mouseMove();

        if (ModReference.hasMod(ModReference.RETRO_SOPHISTICATED_BACKPACKS) && RSBackpacksAccess.isBackpackContainer(InventoryUtil.getCurrentContainer())) {
            if (InputUtil.isCtrlDown() && InputUtil.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode())) {
                InventoryUtil.getSlotMouseOver().ifPresent(InventoryUtil::dropStackIfPossible);
            }
        }// fix: vanilla hold throwing in this gui won't work, don't know why
    }
}
