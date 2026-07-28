package com.github.debris.debrisclient.unsafe.mod;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import rustic.client.gui.GuiBrewingBarrel;
import rustic.common.blocks.fluids.FluidBooze;
import rustic.common.blocks.fluids.ModFluids;
import rustic.common.tileentity.ContainerBrewingBarrel;

public class RusticAccess {
    public static boolean isBrewingBarrelGui(GuiScreen screen) {
        return screen instanceof GuiBrewingBarrel;
    }

    public static boolean isBrewingBarrelContainer(Container container) {
        return container instanceof ContainerBrewingBarrel;
    }

    public static boolean isOnBrewingProcess(GuiScreen screen) {
        return getContainer(screen).getTile().isBrewing();
    }

    public static FluidStack getAuxiliaryFluid(GuiScreen screen) {
        ContainerBrewingBarrel container = getContainer(screen);
        return container.getTile().getAuxiliaryFluid();
    }

    private static ContainerBrewingBarrel getContainer(GuiScreen screen) {
        return (ContainerBrewingBarrel) ((GuiBrewingBarrel) screen).inventorySlots;
    }

    public static FluidStack getOutputFluid(GuiScreen screen) {
        ContainerBrewingBarrel container = getContainer(screen);
        return container.getTile().getOutputFluid();
    }

    public static boolean isGrapeJuice(ItemStack stack) {
        if (FluidUtil.getFluidHandler(stack) == null) return false;
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        if (fluid == null) return false;
        if (fluid.getFluid() == null) return false;
        return fluid.getFluid() == ModFluids.GRAPE_JUICE;
    }

    public static boolean isWine(ItemStack stack) {
        if (stack.getItem() instanceof ItemFluidContainer) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            return fluid != null && fluid.getFluid() != null && fluid.getFluid() instanceof FluidBooze && fluid.tag != null && fluid.tag.hasKey("Quality", 5);
        }
        return false;
    }

    public static float getWineQuality(ItemStack stack) {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        assert fluid != null;
        return fluid.tag.getFloat("Quality");
    }
}
