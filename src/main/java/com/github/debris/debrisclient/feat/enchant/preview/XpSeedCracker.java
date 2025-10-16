package com.github.debris.debrisclient.feat.enchant.preview;

import com.github.debris.debrisclient.config.DCConfig;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class XpSeedCracker {
    private static final Logger LOGGER = LogManager.getLogger(XpSeedCracker.class);
    private int previousHigher;

    private int higher;// higher 28 bits
    private int[] tableLevel;
    private int[] clueId;
    private int[] clueLevel;

    private int lower;// lower 4 bits
    private State state = State.PENDING;
    private final Random rand = new Random();
    private IntList candidates;

    public void clear() {
        this.state = State.PENDING;
    }

    private boolean isDebug() {
        return DCConfig.Debug.getBooleanValue();
    }

    public boolean isCracked() {
        return this.state == State.CRACKED;
    }

    public int getSeed() {
        return this.higher | this.lower;
    }

    public void update(int truncatedSeed, int[] tableLevel, int[] clueId, int[] clueLevel) {
        this.previousHigher = this.higher;
        this.higher = truncatedSeed;
        this.tableLevel = tableLevel;
        this.clueId = clueId;
        this.clueLevel = clueLevel;
    }

    public void crack(ItemStack stack) {
        if (this.higher == this.previousHigher) {
            if (state == State.MULTICHOICE) {
                candidates.removeIf(lower -> !matches(stack, this.higher | lower));
                int size = candidates.size();
                if (size == 0) {
                    state = State.FAIL;
                    if (isDebug()) LOGGER.info("0 match, this should not happen");
                } else if (size == 1) {
                    state = State.CRACKED;
                    lower = candidates.get(0);
                }
                return;
            }
            if (state == State.CRACKED) {
                return;// keep the state
            }
        }

        IntList potentials = new IntArrayList();
        for (int i = 0; i < 16; i++) {
            int trySeed = this.higher | i;
            if (matches(stack, trySeed)) {
                potentials.add(i);
            }
        }

        int size = potentials.size();
        if (size == 0) {
            state = State.FAIL;
            if (isDebug()) LOGGER.info("0 match, this should not happen");
        } else if (size == 1) {
            state = State.CRACKED;
            lower = potentials.get(0);
        } else {
            state = State.MULTICHOICE;
            candidates = potentials;
            if (isDebug()) LOGGER.info("candidates size: {}", potentials.size());
        }
    }

//    private int getPower() {
//        float power = 0;
//        for (int j = -1; j <= 1; ++j) {
//            for (int k = -1; k <= 1; ++k) {
//                if ((j != 0 || k != 0) && this.world.isAirBlock(this.position.add(k, 0, j)) && this.world.isAirBlock(this.position.add(k, 1, j))) {
//                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j * 2));
//                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j * 2));
//                    if (k != 0 && j != 0) {
//                        power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j));
//                        power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j));
//                        power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k, 0, j * 2));
//                        power += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, position.add(k, 1, j * 2));
//                    }
//                }
//            }
//        }
//        return (int) power;
//    }

    private boolean matches(ItemStack stack, int trySeed) {
//        this.rand.setSeed(trySeed);

//        int[] enchantLevels = new int[3];
        int[] enchantClue = new int[3];
        int[] worldClue = new int[3];

//        for (int i = 0; i < 6; i++) {
//            rand.nextInt();
//        }// will consume random when computing enchant levels

//        for (int i1 = 0; i1 < 3; ++i1) {
//            enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, power, stack);
//            enchantClue[i1] = -1;
//            worldClue[i1] = -1;
//
//            if (enchantLevels[i1] < i1 + 1) {
//                enchantLevels[i1] = 0;
//            }
//            enchantLevels[i1] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(world, position, i1, power, stack, enchantLevels[i1]);
//        }// don't know why 10 20 30 goes to 2 5 8 in client

        for (int j1 = 0; j1 < 3; ++j1) {
            if (tableLevel[j1] > 0) {
                List<EnchantmentData> list = this.getEnchantmentList(stack, j1, tableLevel[j1], trySeed);// here the rand is set seed again

                if (list != null && !list.isEmpty()) {
                    EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                    enchantClue[j1] = Enchantment.getEnchantmentID(enchantmentdata.enchantment);
                    worldClue[j1] = enchantmentdata.enchantmentLevel;
                }
            }
        }

        return Arrays.equals(this.clueId, enchantClue) && Arrays.equals(this.clueLevel, worldClue);
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot, int level, int seed) {
        this.rand.setSeed(seed + enchantSlot);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, level, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }

        return list;
    }

    private enum State {
        PENDING,
        FAIL,
        MULTICHOICE,
        CRACKED,
        ;
    }
}
