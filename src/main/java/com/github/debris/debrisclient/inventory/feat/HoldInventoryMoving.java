package com.github.debris.debrisclient.inventory.feat;

import baubles.common.container.ContainerPlayerExpanded;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.InputUtil;
import com.github.debris.debrisclient.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import javax.annotation.Nullable;
import java.util.Optional;

public class HoldInventoryMoving {
    private static Mode MODE = Mode.NONE;
    @Nullable
    private static Slot LAST_MOVED_SLOT = null;

    /**
     * @return If I should cancel left-clicking
     */
    public static boolean start() {
        if (!InventoryUtil.getSlotMouseOver().isPresent()) return false;

        MODE = detectMode();

        if (MODE.isNone()) return false;

        run(MODE);
        return true;
    }

    private static Mode detectMode() {
        if (InputUtil.isShiftDown()) {
            return Mode.SINGLE;
        }
        if (DCConfig.ModifierMoveSimilar.getKeybind().isKeybindHeld()) {
            return Mode.SIMILAR;
        }
        return Mode.NONE;
    }

    public static void stop() {
        if (!MODE.isNone()) {
            LAST_MOVED_SLOT = null;
            MODE = Mode.NONE;
        }
    }

    public static void mouseMove() {
        if (MODE.isNone()) return;
        if (MODE != detectMode()) {
            stop();
            return;
        }
        run(MODE);
    }

    private static void run(Mode mode) {
        Optional<Slot> optional = InventoryUtil.getSlotMouseOver();
        if (!optional.isPresent()) return;
        Slot slot = optional.get();
        if (LAST_MOVED_SLOT == slot) return;

        handle(slot, mode);

        LAST_MOVED_SLOT = slot;
    }

    private static void handle(Slot slot, Mode mode) {
        if (mode == Mode.SINGLE) {
            InventoryUtil.quickMoveIfPossible(slot);
        }
        if (mode == Mode.SIMILAR) {
            InventoryTweaks.tryMoveSimilar();
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean isUnsafe(GuiContainer guiContainer) {
        if (InventoryUtil.getContainer(guiContainer) instanceof ContainerPlayerExpanded) return true;
        return false;
    }

    private enum Mode {
        NONE,
        SINGLE,
        SIMILAR,
        ;

        private boolean isNone() {
            return this == NONE;
        }
    }
}
