package com.github.debris.debrisclient.unsafe.mod;

import com.cleanroommc.modularui.screen.ContainerCustomizer;
import com.cleanroommc.modularui.screen.ModularContainer;
import net.minecraft.inventory.Container;

import java.lang.reflect.Field;
import java.util.function.Function;

public class RSBackpacksAccess {
    private static boolean initialized = false;
    private static boolean available = false;
    private static Function<ModularContainer, ContainerCustomizer> ContainerCustomizerGetter = null;

    public static boolean isBackpackContainer(Container container) {
        if (!(container instanceof ModularContainer)) return false;
        if (!isReflectionAvailable()) return false;
        ModularContainer modularContainer = (ModularContainer) container;
        ContainerCustomizer containerCustomizer = ContainerCustomizerGetter.apply(modularContainer);
        return containerCustomizer.getClass().getName().contains("BackpackContainer");
    }

    private static boolean isReflectionAvailable() {
        if (!initialized) {
            available = initializeReflection();
            initialized = false;
        }
        return available;
    }

    private static boolean initializeReflection() {
        Class<ModularContainer> clazz = ModularContainer.class;
        try {
            Field field = clazz.getDeclaredField("containerCustomizer");
            field.setAccessible(true);
            ContainerCustomizerGetter = container -> {
                try {
                    return (ContainerCustomizer) field.get(container);
                } catch (IllegalAccessException e) {
                    return null;
                }
            };
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
