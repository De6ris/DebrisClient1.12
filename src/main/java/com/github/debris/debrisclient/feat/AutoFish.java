package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.task.Task;
import com.github.debris.debrisclient.feat.task.TaskQueue;
import com.github.debris.debrisclient.feat.task.TaskResult;
import com.github.debris.debrisclient.unsafe.mod.FishingMBAccess;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AutoFish {
    public static void onTick(Minecraft client) {
        if (!Predicates.inGameNoGui(client)) return;
        if (!DCConfig.AutoFish.getBooleanValue()) return;
        if (!ModReference.hasMod(ModReference.FISHINGMADEBETTER)) return;
        FishingMBAccess.onTick(client);
    }

    public static void onRetrieve() {
        Minecraft client = Minecraft.getMinecraft();
        if (!Predicates.inGameNoGui(client)) return;
        if (!DCConfig.AutoFish.getBooleanValue()) return;

        TaskQueue.schedule(getRethrowTask(), 10);// wait for fish to land
    }

    private static Task getRethrowTask() {
        return client -> {
            ItemStack stack = client.player.getHeldItemMainhand();
            if (FishingMBAccess.isFishingRod(stack)) {
                AccessorUtil.use(client);
                return TaskResult.SUCCESS;
            }
            return TaskResult.FAIL;
        };
    }
}
