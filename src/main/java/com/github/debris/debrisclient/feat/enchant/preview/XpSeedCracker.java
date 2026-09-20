package com.github.debris.debrisclient.feat.enchant.preview;

import com.github.debris.debrisclient.Platform;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.util.RayTraceUtil;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class XpSeedCracker {
    public static final Logger LOGGER = LogManager.getLogger(XpSeedCracker.class);

    public static final int POWER_NOT_AVAILABLE = -1;

    private boolean local = false;

    private World world = null;
    private BlockPos position = BlockPos.ORIGIN;

    private EnchantingTableData data;

    private int localHigher;// 31 to 16 bits

    private int previousMedium;
    private int medium;// 15 to 4 bits

    private CrackingState state = CrackingState.PENDING;
    private final Random rand = new Random();
    private final IntList candidates = new IntArrayList();

    private final ParallelSeedChecker parallelChecker = new ParallelSeedChecker();

    XpSeedCracker() {
    }

    public void clear() {
        if (this.state == CrackingState.CRACKING) {
            this.parallelChecker.maybeCancel();
            this.setState(CrackingState.PENDING);
        }
    }

    private boolean isDebug() {
        return DCConfig.Debug.getBooleanValue();
    }

    private void setState(CrackingState state) {
        this.state = state;
    }

    public CrackingState getState() {
        return this.state;
    }

    public int getCandidateSize() {
        return this.candidates.size();
    }

    public float getProgress() {
        return this.parallelChecker.getProgress();
    }

    public boolean isCracked() {
        return this.state == CrackingState.CRACKED;
    }

    public int getSeed() {
        return this.candidates.get(0);
    }

    public void update(ContainerEnchantment container, World world, ItemStack stack) {
        if (this.data == null || !this.data.equals(container.enchantLevels, container.enchantClue, container.worldClue)) {
            this.data = new EnchantingTableData(container.enchantLevels, container.enchantClue, container.worldClue);
            if (stack.isEmpty() || !stack.isItemEnchantable()) {
                this.clear();
                return;
            }
        } else {
            return;
        }

        this.local = Platform.isSinglePlayer();
        this.world = world;
        int power = POWER_NOT_AVAILABLE;
        Minecraft client = Minecraft.getMinecraft();
        BlockPos blockPos = RayTraceUtil.getBlockPos(client);
        if (blockPos != null && client.world.getBlockState(blockPos).getBlock() == Blocks.ENCHANTING_TABLE) {
            this.position = blockPos;
            power = getPower(world, blockPos);
        }

        this.previousMedium = this.medium;
        int packetXpSeed = container.xpSeed;
        if (this.local) {
            this.localHigher = ((packetXpSeed >> 16) & 0xFFFF) << 16;
        }
        this.medium = packetXpSeed & 0x0000FFF0;

        if (power != POWER_NOT_AVAILABLE) {
            this.crack(stack, power);
        }
    }

    private void crack(ItemStack stack, int power) {
        if (state == CrackingState.CRACKING) {
            this.parallelChecker.maybeCancel();
        }

        if (this.medium == this.previousMedium) {
            if (state == CrackingState.MULTICHOICE) {
                this.candidates.removeIf(xpSeed -> !matches(stack, power, xpSeed));
                this.updateState();
                return;
            }
            if (state == CrackingState.CRACKED && matches(stack, power, this.getSeed())) {
                return;// keep the state
            }
        }

        IntList potentials = this.candidates;
        potentials.clear();

        if (this.local) {
            for (int low = 0; low < 16; low++) {
                int xpSeed = this.localHigher | medium | low;
                if (matches(stack, power, xpSeed)) {
                    potentials.add(xpSeed);
                }
            }
            this.updateState();
            return;
        }

        if (DCConfig.EnchantPreviewParallel.getBooleanValue()) {
            this.setState(CrackingState.CRACKING);
            this.parallelChecker.upload(world, position, stack.copy(), data, power, medium, IntLists.synchronize(potentials), this::updateState);
            return;
        }

        this.iterateSerial(stack, potentials, power);
    }

    private void iterateSerial(ItemStack stack, IntList potentials, int power) {
        World world = this.world;
        BlockPos position = this.position;
        EnchantingTableData data = this.data;
        Random rand = this.rand;
        int medium = this.medium;

        for (int high = 0; high < 65536; high++) {
            for (int low = 0; low < 16; low++) {
                int xpSeed = high << 16 | medium | low;
                if (matches(world, position, data, stack, rand, power, xpSeed)) {
                    potentials.add(xpSeed);
                }
            }
        }
        this.updateState();
    }

    private void updateState() {
        IntList potentials = candidates;
        int size = potentials.size();
        if (size == 0) {
            this.setState(CrackingState.FAIL);
            if (isDebug()) LOGGER.info("0 match, this should not happen");
        } else if (size == 1) {
            this.setState(CrackingState.CRACKED);
        } else {
            this.setState(CrackingState.MULTICHOICE);
            if (isDebug()) LOGGER.info("candidates size: {}", potentials.size());
        }
    }

    private static int getPower(World world, BlockPos position) {
        float power = 0;
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if ((j != 0 || k != 0) && world.isAirBlock(position.add(k, 0, j)) && world.isAirBlock(position.add(k, 1, j))) {
                    power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j * 2));
                    power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j * 2));
                    if (k != 0 && j != 0) {
                        power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j));
                        power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j));
                        power += ForgeHooks.getEnchantPower(world, position.add(k, 0, j * 2));
                        power += ForgeHooks.getEnchantPower(world, position.add(k, 1, j * 2));
                    }
                }
            }
        }
        return (int) power;
    }

    private boolean matches(ItemStack stack, int power, int xpSeed) {
        return matches(world, position, this.data, stack, this.rand, power, xpSeed);
    }

    public static boolean matches(World world, BlockPos position, EnchantingTableData data, ItemStack stack, Random rand, int power, int xpSeed) {
        int[] enchantLevel = data.enchantLevel;
        int[] clueId = data.clueId;
        int[] clueLevel = data.clueLevel;

        rand.setSeed(xpSeed);

        for (int slot = 0; slot < 3; ++slot) {
            int level = EnchantmentHelper.calcItemStackEnchantability(rand, slot, power, stack);
            if (level < slot + 1) {
                level = 0;
            }
            level = ForgeEventFactory.onEnchantmentLevelSet(world, position, slot, power, stack, level);
            if (level != enchantLevel[slot]) {
                return false;
            }
        }

        for (int slot = 0; slot < 3; ++slot) {
            int level = enchantLevel[slot];
            if (level > 0) {
                List<EnchantmentData> list = getEnchantmentList(rand, stack, slot, level, xpSeed);// here the rand is set seed again
                if (!list.isEmpty()) {
                    EnchantmentData clue = list.get(rand.nextInt(list.size()));
                    if (Enchantment.getEnchantmentID(clue.enchantment) != clueId[slot] || clue.enchantmentLevel != clueLevel[slot]) {
                        return false;
                    }
                } else if (clueId[slot] != -1 || clueLevel[slot] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot, int level) {
        return getEnchantmentList(this.rand, stack, enchantSlot, level, this.getSeed());
    }

    @NotNull
    public static List<EnchantmentData> getEnchantmentList(Random rand, ItemStack stack, int enchantSlot, int level, int seed) {
        rand.setSeed(seed + enchantSlot);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, stack, level, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(rand.nextInt(list.size()));
        }

        return list;
    }
}
