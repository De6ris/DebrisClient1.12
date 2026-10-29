package com.github.debris.debrisclient.feat.enchant;

import com.github.debris.debrisclient.ModReference;
import net.minecraft.item.ItemStack;

public interface EnchantSystem {
    static EnchantSystem getInstance() {
        if (ModReference.hasMod(ModReference.ENCHANTMENT_CONTROL)) return new EnchantmentControl();
        return new Vanilla();
    }

    static int getPunishment(ItemStack itemStack) {
        return itemStack.getRepairCost();
    }

    int asOperation(int punishment);

    int asPunishment(int operation);

    default int asOperation(ItemStack itemStack) {
        return this.asOperation(getPunishment(itemStack));
    }

    class Vanilla implements EnchantSystem {
        @Override
        public int asOperation(int punishment) {
            int cursor = 0;
            int loop = 0;
            while (cursor != punishment && loop < 31) {
                cursor = 2 * cursor + 1;
                loop++;
            }
            return loop;
        }

        @Override
        public int asPunishment(int operation) {
            int cursor = 0;
            for (int i = 0; i < operation; i++) {
                cursor = 2 * cursor + 1;
            }
            return cursor;
        }
    }

    class EnchantmentControl implements EnchantSystem {
        @Override
        public int asOperation(int punishment) {
            return (int) Math.sqrt(punishment);
        }

        @Override
        public int asPunishment(int operation) {
            return operation * operation;
        }
    }
}
