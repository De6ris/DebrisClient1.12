package com.github.debris.debrisclient.util;


import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.unsafe.mod.CharmAccess;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemUtil {
    public static boolean compareID(ItemStack itemStack, ItemStack other) {
        return ItemStack.areItemsEqual(itemStack, other);
    }

    public static boolean compareNBT(ItemStack itemStack, ItemStack other) {
        return ItemStack.areItemStackShareTagsEqual(itemStack, other);
    }

    public static boolean compareIDNBT(ItemStack itemStack, ItemStack other) {
        return compareID(itemStack, other) && compareNBT(itemStack, other);
    }

    public static Predicate<ItemStack> predicateIDNBT(ItemStack template) {
        return x -> compareIDNBT(x, template);
    }

    public static boolean isFullStack(ItemStack itemStack) {
        return itemStack.getCount() >= itemStack.getMaxStackSize();
    }

    public static boolean canMerge(ItemStack to, ItemStack from) {
        if (isFullStack(to)) return false;// full slot can not merge
        return compareIDNBT(to, from);
    }

    public static List<ItemStack> filterNonEmpty(List<ItemStack> list) {
        return list.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    public static List<ItemStack> readShulkerBox(ItemStack itemStack) {
        return readContainer(itemStack, 27, "BlockEntityTag");
    }

    public static List<ItemStack> readCharmCrate(ItemStack itemStack) {
        return readContainer(itemStack, 9, ImmutableList.of("BlockEntityTag", "inventory"));
    }

    public static List<ItemStack> readContainer(ItemStack itemStack, int size, String path) {
        return readContainer(itemStack, size, Collections.singletonList(path));
    }

    @SuppressWarnings("DataFlowIssue")
    public static List<ItemStack> readContainer(ItemStack itemStack, int size, List<String> path) {
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        if (!itemStack.hasTagCompound()) return items;

        NBTTagCompound cursor = itemStack.getTagCompound();
        for (String s : path) {
            if (!cursor.hasKey(s)) {
                return items;
            }
            cursor = cursor.getCompoundTag(s);
        }

        if (cursor.hasKey("Items", Constants.NBT.TAG_LIST)) {
            ItemStackHelper.loadAllItems(cursor, items);
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
