package com.github.debris.debrisclient.unsafe.baubles;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.BountifulBaublesAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class BountifulBaublesReforgingTask extends AbstractReforgingTask {
    private final Slot bauble;

    public BountifulBaublesReforgingTask(Minecraft client) {
        super(client);
        this.bauble = EnumSection.BountifulBaublesReforging.get().getFirstSlot();
    }

    @Nonnull
    @Override
    protected NBTTagCompound getBaubleTag() {
        NBTTagCompound tag = this.bauble.getStack().getTagCompound();
        return tag == null ? new NBTTagCompound() : tag;
    }

    @Override
    protected boolean canReforge() {
        return BountifulBaublesAccess.canReforge(this.client.currentScreen);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    protected boolean isGoodTag(NBTTagCompound tag) {
        String name = tag.getString("baubleModifier");
        if (DCConfig.ReforgingWhiteListBB.getStrings().stream().anyMatch(name::equals)) return true;

        String translate = BountifulBaublesAccess.translate(name);
        if (DCConfig.ReforgingWhiteListBB.getStrings().stream().anyMatch(translate::equals)) return true;

        return false;
    }
}
