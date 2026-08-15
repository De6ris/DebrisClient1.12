package com.github.debris.debrisclient.util;

import com.github.debris.debrisclient.localization.GeneralText;
import net.minecraft.util.text.ITextComponent;

public class ComponentUtil {
    public static ITextComponent successOrFail(boolean b) {
        return b ? GeneralText.SUCCESS.translate() : GeneralText.FAIL.translate();
    }
}
