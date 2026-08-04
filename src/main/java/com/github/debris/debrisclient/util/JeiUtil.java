package com.github.debris.debrisclient.util;

import mezz.jei.api.IJeiRuntime;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class JeiUtil {
    public static IJeiRuntime jeiRuntime;

    @Nullable
    public static Object getHoveredStack() {
        Object ingredient = jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse();
        if (ingredient == null) ingredient = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse();
        return ingredient;
    }

    @Nullable
    public static String getStackName(@Nullable Object stack) {
        if (stack instanceof ItemStack) {
            return ((ItemStack) stack).getDisplayName();
        }

        if (stack instanceof FluidStack) {
            return ((FluidStack) stack).getLocalizedName();
        }

        return null;
    }
}
