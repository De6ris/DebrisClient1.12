package com.github.debris.debrisclient.feat.enchant.plan;

import com.github.debris.debrisclient.util.AnvilUtil;
import com.github.debris.debrisclient.util.EnchantUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

class EnchantRouter {
    final ItemStack stack;
    final ArrayList<ItemStack> books;

    EnchantRouter(ItemStack stack, List<ItemStack> books) {
        this.stack = stack;
        this.books = books.stream()
                .sorted(getComparator())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Comparator<ItemStack> getComparator() {
        return Comparator.comparingInt(book -> -getEffectiveEnchantCost(book));// big comes first
    }

    List<Step> plan(boolean hasUpgrade) {
        int originalOperations = AnvilUtil.getOperations(stack);

        List<Step> steps = new ArrayList<>();

        List<ItemStack> booksAsLocal = books;

        if (hasUpgrade) {
            int booksAfterUpgrade = booksAsLocal.size() - getBookCountBeforeUpgrade(originalOperations);
            if (booksAfterUpgrade > 0) {
                return planBoth(booksAfterUpgrade, steps, originalOperations, booksAsLocal);
            }
        }

        return planSingle(hasUpgrade, originalOperations, booksAsLocal, steps);
    }

    private List<Step> planSingle(boolean hasUpgrade, int originalOperations, List<ItemStack> booksAsLocal, List<Step> steps) {
        int operation = originalOperations;
        while (!booksAsLocal.isEmpty()) {
            CombinedBook combinedBook = planLocal(operation, booksAsLocal);
            int levelCost = combinedBook.getLevelCost(operation);
            steps.add(new Step(combinedBook.subBooks, levelCost));
            booksAsLocal.removeAll(combinedBook.subBooks);
            operation++;
        }

        if (hasUpgrade && operation >= 4) {
            steps.add(new Step(ImmutableList.of(getUpgrade()), 10));
        }
        return steps;
    }

    private List<Step> planBoth(int booksAfterUpgrade, List<Step> steps, int originalOperations, List<ItemStack> booksAsLocal) {
        int operationsAfterUpgrade = getOperations(booksAfterUpgrade);

        int maxPossibleOperation = Math.max(4, operationsAfterUpgrade);

        List<Step> former = new ArrayList<>();
        List<Step> latter = new ArrayList<>();

        for (int operation = 0; operation < maxPossibleOperation; operation++) {
            if (originalOperations <= operation) {
                CombinedBook combinedBook = planLocal(operation, booksAsLocal);
                int levelCost = combinedBook.getLevelCost(operation);
                former.add(new Step(combinedBook.subBooks, levelCost));
                booksAsLocal.removeAll(combinedBook.subBooks);
            }
            if (operation < operationsAfterUpgrade) {
                CombinedBook combinedBook = planLocal(operation, booksAsLocal);
                int levelCost = combinedBook.getLevelCost(operation);
                latter.add(new Step(combinedBook.subBooks, levelCost));
                booksAsLocal.removeAll(combinedBook.subBooks);
            }
        }

        steps.addAll(former);
        steps.add(new Step(ImmutableList.of(getUpgrade()), 10));
        steps.addAll(latter);
        return steps;
    }

    private int getOperations(int books) {
        if (books == 1) return 1;
        if (books <= 3) return 2;
        if (books <= 7) return 3;
        if (books <= 11) return 4;
        if (books <= 15) return 5;
        return (books / 4) + 2;
    }

    private int getBookCountBeforeUpgrade(int operations) {
        if (operations == 0) return 1 + 2 + 4 + 4;
        if (operations == 1) return 2 + 4 + 4;
        if (operations == 2) return 4 + 4;
        if (operations == 3) return 4;
        return 0;
    }

    private CombinedBook planLocal(int operations, List<ItemStack> books) {
        int maxBookOperations = Math.min(operations, 2);//at most 4 books to combine
        if (maxBookOperations == 0) {
            return create(books.get(0), 0);
        }
        if (maxBookOperations == 1) {
            return plan1(books);
        }
        if (maxBookOperations == 2) {
            return plan2(books);
        }
        throw new AssertionError();
    }

    private CombinedBook plan1(List<ItemStack> books) {
        int size = books.size();
        if (size <= 2) {
            return create(ImmutableList.copyOf(books), size == 2 ? 1 : 0);
        }

        List<ItemStack> twoBooks = null;

        loop:
        for (int i = 0; i < size; i++) {
            ItemStack book1 = books.get(i);
            int cost1 = getEffectiveEnchantCost(book1);

            for (int j = i + 1; j < size; j++) {
                ItemStack book2 = books.get(j);
                int cost2 = getEffectiveEnchantCost(book2);
                if (2 + cost1 + cost2 > 30) continue;

                twoBooks = ImmutableList.of(book1, book2);
                break loop;
            }
        }

        if (twoBooks == null) {// i.e. everything is expensive
            twoBooks = ImmutableList.of(books.get(size - 2), books.get(size - 1));
        }

        return create(twoBooks, 1);
    }

    private CombinedBook plan2(List<ItemStack> books) {
        int size = books.size();
        if (size <= 4) {
            int bookOperations;
            if (size >= 3) bookOperations = 2;
            else if (size == 2) bookOperations = 1;
            else bookOperations = 0;
            return create(ImmutableList.copyOf(books), bookOperations);
        }

        List<ItemStack> fourBooks = null;

        loop:
        for (int i = 0; i < size; i++) {
            ItemStack book1 = books.get(i);
            int cost1 = getEffectiveEnchantCost(book1);

            for (int j = i + 1; j < size; j++) {
                ItemStack book2 = books.get(j);
                int cost2 = getEffectiveEnchantCost(book2);

                for (int k = j + 1; k < size; k++) {
                    ItemStack book3 = books.get(k);
                    int cost3 = getEffectiveEnchantCost(book3);

                    for (int l = k + 1; l < size; l++) {
                        ItemStack book4 = books.get(l);
                        int cost4 = getEffectiveEnchantCost(book4);

                        if (6 + cost1 + cost2 + cost3 + cost4 > 30) continue;

                        fourBooks = ImmutableList.of(book1, book2, book3, book4);
                        break loop;
                    }
                }
            }
        }

        if (fourBooks == null) {// i.e. everything is expensive
            fourBooks = ImmutableList.of(books.get(size - 4), books.get(size - 3), books.get(size - 2), books.get(size - 1));
        }

        return create(fourBooks, 2);
    }

    private CombinedBook create(ItemStack book, int operations) {
        return new CombinedBook(ImmutableList.of(book), operations, getEffectiveEnchantCost(book));
    }

    private CombinedBook create(List<ItemStack> books, int operations) {
        return new CombinedBook(books, operations, getEffectiveEnchantCost(books));
    }

    int getEffectiveEnchantCost(List<ItemStack> books) {
        return books.stream().mapToInt(this::getEffectiveEnchantCost).sum();
    }

    int getEffectiveEnchantCost(ItemStack book) {
        return getEffectiveEnchantCost(EnchantmentHelper.getEnchantments(book));
    }

    private int getEffectiveEnchantCost(Map<Enchantment, Integer> map) {
        Map<Enchantment, Integer> newMap = map.entrySet().stream()
                .filter(x -> x.getKey().canApply(stack))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return EnchantUtil.calculateEnchantmentCost(newMap);
    }

    private static ItemStack getUpgrade() {
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        HashMap<Enchantment, Integer> map = new HashMap<>();
        map.put(AnvilEnchantPlan.UPGRADED_POTENTIALS, 1);
        EnchantmentHelper.setEnchantments(map, stack);
        return stack;
    }
}
