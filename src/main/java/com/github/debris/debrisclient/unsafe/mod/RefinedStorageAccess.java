package com.github.debris.debrisclient.unsafe.mod;

import com.raoulvdberge.refinedstorage.RSKeyBindings;
import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.gui.control.TextFieldSearch;
import com.raoulvdberge.refinedstorage.gui.grid.GuiGrid;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;

public class RefinedStorageAccess {
    public static boolean isRSGui(GuiContainer screen) {
        return screen instanceof GuiBase;
    }

    public static boolean isGrid(GuiContainer guiContainer) {
        return guiContainer instanceof GuiGrid;
    }

    public static GuiTextField getSearchField(GuiContainer guiContainer) {
        return ((GuiGrid) guiContainer).getSearchField();
    }

    public static boolean isSearchField(GuiTextField textField) {
        return textField instanceof TextFieldSearch;
    }

    public static void updateSearch(GuiTextField textField) {
        textField.textboxKeyTyped('a', RSKeyBindings.FOCUS_SEARCH_BAR.getKeyCode());// the 'a' is dummy
        textField.setFocused(false);
    }
}
