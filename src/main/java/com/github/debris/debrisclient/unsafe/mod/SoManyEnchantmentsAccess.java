package com.github.debris.debrisclient.unsafe.mod;

import com.shultrea.rin.attributes.EnchantAttribute;
import com.shultrea.rin.util.FromEnchTableThreadLocal;
import net.minecraft.entity.player.EntityPlayer;

public class SoManyEnchantmentsAccess {
    public static void setThreadLocals(EntityPlayer player) {
        EnchantAttribute.attributeThreadLocal.set(player.getEntityAttribute(EnchantAttribute.ENCHANTFOCUS));
        FromEnchTableThreadLocal.set(true);
    }
}
