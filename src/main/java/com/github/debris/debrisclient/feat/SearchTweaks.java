package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.RefinedStorageAccess;
import com.github.debris.debrisclient.util.JeiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SearchTweaks {
    public static boolean searchHovered(Minecraft client) {
        return execute(client, textField -> {
            String name = JeiUtil.getStackName(JeiUtil.getHoveredStack());
            if (name == null) return false;

            textField.setText(name);

            if (ModReference.hasMod(ModReference.REFINED_STORAGE) && RefinedStorageAccess.isSearchField(textField)) {
                RefinedStorageAccess.updateSearch(textField);
            }

            return true;
        });
    }

    public static boolean clear(Minecraft client) {
        return execute(client, textField -> {
            if (textField.getText().isEmpty()) return false;
            textField.setText("");

            if (ModReference.hasMod(ModReference.REFINED_STORAGE) && RefinedStorageAccess.isSearchField(textField)) {
                RefinedStorageAccess.updateSearch(textField);
            }

            return true;
        });
    }

    private static boolean execute(Minecraft client, Function<GuiTextField, Boolean> action) {
        GuiScreen screen = client.currentScreen;
        if (!(screen instanceof GuiContainer)) return false;
        GuiTextField textField = findTextField((GuiContainer) screen);
        if (textField == null) return false;
        if (textField.isFocused()) return false;

        return action.apply(textField);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Nullable
    private static GuiTextField findTextField(GuiContainer screen) {
        if (ModReference.hasMod(ModReference.REFINED_STORAGE) && RefinedStorageAccess.isRSGui(screen)) {
            GuiTextField rsField = findTextField_rs(screen);
            if (rsField != null) return rsField;
        }

//        GuiTextField vanillaField = findTextField_vanilla(screen);
//        if (vanillaField != null) return vanillaField;

        return null;
    }

    @Nullable
    private static GuiTextField findTextField_rs(GuiContainer guiContainer) {
        if (RefinedStorageAccess.isGrid(guiContainer)) {
            return RefinedStorageAccess.getSearchField(guiContainer);
        }

        return null;
    }

    @Nullable
    private static GuiTextField findTextField_vanilla(GuiContainer screen) {
        return null;
    }

}
