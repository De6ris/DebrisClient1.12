package com.github.debris.debrisclient.util;


import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.CharmAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemUtil {
    public static boolean compareID(ItemStack itemStack, ItemStack other) {
        return ItemStack.areItemsEqual(itemStack, other);
    }

    public static boolean compareMeta(ItemStack itemStack, ItemStack other) {
        return itemStack.getMetadata() == other.getMetadata();
    }

    public static boolean compareIDMeta(ItemStack itemStack, ItemStack other) {
        return compareID(itemStack, other) && compareMeta(itemStack, other);
    }

    public static Predicate<ItemStack> predicateIDMeta(ItemStack template) {
        return x -> compareIDMeta(x, template);
    }

    public static boolean isFullStack(ItemStack itemStack) {
        return itemStack.getCount() >= itemStack.getMaxStackSize();
    }

    public static boolean canMerge(ItemStack to, ItemStack from) {
        if (isFullStack(to)) return false;// full slot can not merge
        return compareIDMeta(to, from);
    }

    public static List<ItemStack> filterNonEmpty(List<ItemStack> list) {
        return list.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    public static List<ItemStack> readShulkerBox(ItemStack itemStack) {
        return readInventory(itemStack, 27);
    }

    public static List<ItemStack> readCharmCrate(ItemStack itemStack) {
        return readInventory(itemStack, 9);
    }

    @SuppressWarnings("ConstantConditions")
    public static List<ItemStack> readInventory(ItemStack itemStack, int size) {
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        if (!itemStack.hasTagCompound()) return items;
        NBTTagCompound itemStackNBT = itemStack.getTagCompound();
        if (!itemStackNBT.hasKey("BlockEntityTag")) return items;
        NBTTagCompound compound = itemStackNBT.getCompoundTag("BlockEntityTag");
        if (compound.hasKey("inventory", 9)) {
            ItemStackHelper.loadAllItems(compound, items);
        }
        return items;
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean isContainer(ItemStack itemStack) {
        if (isShulkerBox(itemStack)) return true;
        if (isCharmCrate(itemStack)) return true;
        return false;
    }

    private static Optional<Block> asBlock(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) item;
            return Optional.of(itemBlock.getBlock());
        }
        return Optional.empty();
    }

    public static boolean isShulkerBox(ItemStack itemStack) {
        Optional<Block> block = asBlock(itemStack.getItem());
        return block.isPresent() && block.get() instanceof BlockShulkerBox;
    }

    public static boolean isCharmCrate(ItemStack itemStack) {
        if (!ModReference.hasMod(ModReference.CHARM)) return false;
        Optional<Block> block = asBlock(itemStack.getItem());
        return block.isPresent() && CharmAccess.isCrate(block.get());
    }
}
