package com.github.debris.debrisclient.feat.enchant.preview;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ParallelSeedChecker {
    private final AtomicInteger progress = new AtomicInteger(0);

    private CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

    public float getProgress() {
        return (float) this.progress.get() / 65536;
    }

    public void maybeCancel() {
        try {
            if (this.future.isDone()) {
                return;
            }
            this.future.cancel(false);
        } catch (CancellationException ignored) {
        }
    }

    public void upload(World world, BlockPos position, ItemStack stack, EnchantingTableData data, int power, int medium, IntList potentials, Runnable finish) {
        this.future = CompletableFuture.runAsync(() -> iterateParallel(world, position, stack, data, power, medium, potentials)).thenRun(finish);
    }

    private void iterateParallel(World world, BlockPos position, ItemStack stack, EnchantingTableData data, int power, int medium, IntList potentials) {
        try {
            this.progress.set(0);
            IntStream.range(0, 65536)
                    .parallel()
                    .forEach(high -> {
                        Random rand = new Random();
                        for (int low = 0; low < 16; low++) {
                            int xpSeed = high << 16 | medium | low;
                            if (XpSeedCracker.matches(world, position, data, stack, rand, power, xpSeed)) {
                                potentials.add(xpSeed);
                            }
                        }

                        this.progress.getAndIncrement();
                    });
        } catch (RuntimeException e) {
            XpSeedCracker.LOGGER.warn(e);
        }
    }
}
