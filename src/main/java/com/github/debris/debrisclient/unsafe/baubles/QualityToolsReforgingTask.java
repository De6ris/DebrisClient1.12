package com.github.debris.debrisclient.unsafe.baubles;

import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.QualityColor;
import com.github.debris.debrisclient.feat.QualityLevel;
import com.github.debris.debrisclient.inventory.section.EnumSection;
import com.github.debris.debrisclient.unsafe.mod.QualityToolsAccess;
import com.github.debris.debrisclient.util.StringUtil;
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
        QualityLevel targetLevel = DCConfig.ReforgingLevel.getEnumValue();
        if (targetLevel != QualityLevel.NO_LEVEL) {
            String color = tag.getString("Color");
            QualityColor present = QualityColor.fromString((color));
            if (present == QualityColor.UNKNOWN) return true;
            if (targetLevel.matches(present)) return true;
        }

        String name = tag.getString("Name");
        if (DCConfig.ReforgingWhiteListQT.getStrings().stream().anyMatch(name::contains)) return true;
        String translate = StringUtil.translate(name);
        if (DCConfig.ReforgingWhiteListQT.getStrings().stream().anyMatch(translate::contains)) return true;

        return false;
    }
}
