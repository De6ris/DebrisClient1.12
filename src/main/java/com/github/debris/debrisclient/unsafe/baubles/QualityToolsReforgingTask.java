package com.github.debris.debrisclient.unsafe.baubles;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.QualityColor;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.QualityToolsAccess;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class QualityToolsReforgingTask extends AbstractReforgingTask {
    private final Slot tool;

    public QualityToolsReforgingTask(Minecraft client) {
        super(client);
        this.tool = EnumSection.QualityToolsReforgingTool.get().getFirstSlot();
    }

    @Nonnull
    @Override
    protected NBTTagCompound getBaubleTag() {
        return QualityToolsHelper.getQualityTag(this.tool.getStack());
    }

    @Override
    protected boolean canReforge() {
        return QualityToolsAccess.canReforge(this.client.currentScreen);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    protected boolean isGoodTag(NBTTagCompound tag) {
        String name = tag.getString("Name");
        String color = tag.getString("Color");
        QualityColor present = QualityColor.parse(color);
        QualityColor target = DCConfig.ReforgingLevel.getEnumValue();
        if (present.betterOrEqual(target)) return true;
        if (DCConfig.ReforgingWhiteListQT.getStrings().stream().anyMatch(name::contains)) return true;
        return false;
    }
}
